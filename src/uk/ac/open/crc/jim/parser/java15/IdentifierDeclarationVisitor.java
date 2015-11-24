/*
 Copyright (C) 2010-2015 The Open University

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package uk.ac.open.crc.jim.parser.java15;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.open.crc.idtk.Modifier;
import uk.ac.open.crc.idtk.Species;
import uk.ac.open.crc.idtk.TypeName;
import uk.ac.open.crc.jim.parser.java15.Java15Parser.ModifierSet;
import uk.ac.open.crc.jim.persistence.EntityStoreSingleton;
import uk.ac.open.crc.jim.Settings;
import uk.ac.open.crc.jimdb.RawProgramEntity;

/**
 * Visitor implementation for Java 1.5 parser.
 *
 */
public class IdentifierDeclarationVisitor extends BasicVisitor {

    /**
     * An illegal Java identifier name used to positively mark
     * program entities that are anonymous.
     */
    private static final String ANONYMOUS = "#anonymous#";

    private static final TypeName NO_TYPE = new TypeName( "#no type#" );

    private static final Logger LOGGER
            = LoggerFactory.getLogger( IdentifierDeclarationVisitor.class );

    private final String javaFileName;
    private final EntityStoreSingleton identifierStore;

    /**
     * A store for the FQNs of imported types
     */
    private final ArrayList<String> imports;

    /**
     * A store of FQNs of types declared within the parsed file
     */
    private final ArrayList<String> locallyDeclaredTypes;

    public IdentifierDeclarationVisitor (
            String javaFileName,
            EntityStoreSingleton entityStore ) {
        super();

        this.javaFileName = javaFileName;
        this.identifierStore = entityStore;

        this.imports = new ArrayList<>();
        this.locallyDeclaredTypes = new ArrayList<>();
    }

    // inject the tracker at the top of the CU tree
    @Override
    public Object visit ( ASTCompilationUnit node, Object data ) {
        walkChildren( node, new DtLocationTracker() );
        return data;
    }

    @Override
    // extract the package name for recording purposes
    public Object visit ( ASTPackageDeclaration node, Object data ) {
        ArrayList<String> fragments = new ArrayList<>();
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTName ) {
                for ( int j = 0; j < child.jjtGetNumChildren(); j++ ) {
                    // extract the identifiers
                    Node grandChild = child.jjtGetChild( j );
                    if ( grandChild instanceof ASTIdentifier ) {
                        ASTIdentifier ident = (ASTIdentifier) grandChild;
                        fragments.add( ident.getText() );
                    }
                }
            }
        }

        // now build the package name from the extracted components
        StringBuilder packageName = new StringBuilder();
        String[] parts = fragments.toArray( new String[0] );
        for ( int k = 0; k < parts.length; k++ ) {
            packageName.append( parts[k] );
            if ( k < ( parts.length - 1 ) ) {
                // we don't want a dot after the final fragment
                packageName.append( "." );
            }
        }

        ( (DtLocationTracker) data ).setPackageName( packageName.toString() );

        return data;
    }

    @Override
    public Object visit ( ASTImportDeclaration node, Object data ) {
        imports.add( getNameFor( node ) );
        return data;
    }

    // -------- Leaf Entities -----------------------
    @Override
    public Object visit ( ASTLabeledStatement node, Object data ) {
        // grammar for a labelled statement is:
        // Identifier() Colon() Statement()
        // the identifier is all that matters - if it does at all
        // as labels are largely arbitrary and implementation domain.
        String identifierNameString = "";
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                identifierNameString = ( (ASTIdentifier) child ).getText();
                break;
            }
        }

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();
        String entityUid = tracker.getUidForLeafEntity();

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.LABEL,
                NO_TYPE, // labels have no type
                false, // can't be an array
                null, // no method signature
                null, // no modifiers
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        walkChildren( node, data );
        return data;
    }

    @Override
    public Object visit ( ASTLocalVariableDeclaration node, Object data ) {
        // the grammar for the local variable declaration is :
        // Type() VariableDeclarator() ( Comma() VariableDeclarator() )* SemiColon()
        // so may be one type and multiple names
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Modifier> modifiers = getModifierListFor( node );

        String typeNameString = "";
        boolean isArrayDeclaration = false;

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTType ) {
                typeNameString = getTypeName( (ASTType) child );
                isArrayDeclaration = isArrayDeclaration( (ASTType) child );
            }
            else if ( child instanceof ASTVariableDeclarator ) {
                names.add( getIdentifierName( (ASTVariableDeclarator) child ) );
            }
        }

        boolean isLoopControlVariable = ( node.jjtGetParent() instanceof ASTForInit );

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();

        TypeName typeName = getResolvedTypeNameFor( typeNameString );

        for ( String identifierNameString : names ) {
            String entityUid = tracker.getUidForLeafEntity();
            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    tracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.LOCAL_VARIABLE,
                    typeName,
                    isArrayDeclaration,
                    null, // no method signature
                    modifiers,
                    isLoopControlVariable,
                    null, // no super class(es)
                    null, // no interfaces implemented or extended
                    node.firstToken.beginLine,
                    node.firstToken.beginColumn,
                    node.lastToken.endLine,
                    node.lastToken.endColumn );

            this.identifierStore.add( programEntity );
        }

        walkChildren( node, data );
        return data;
    }

    @Override
    public Object visit ( ASTFieldDeclaration node, Object data ) {
        // grammar for the field declaration is :
        // Type() VariableDeclarator() ( Comma() VariableDeclarator() )* SemiColon()
        // so could be one type and multiple names
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Modifier> modifiers = getModifierListFor( node );
        String typeNameString = "";
        boolean isArrayDeclaration = false;

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTType ) {
                typeNameString = getTypeName( (ASTType) child );
                isArrayDeclaration = isArrayDeclaration( (ASTType) child );
            }
            else if ( child instanceof ASTVariableDeclarator ) {
                names.add( getIdentifierName( (ASTVariableDeclarator) child ) );
            }
        }

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();

        TypeName typeName = getResolvedTypeNameFor( typeNameString );

        for ( String identifierNameString : names ) {
            String entityUid = tracker.getUidForLeafEntity();
            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    tracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.FIELD,
                    typeName,
                    isArrayDeclaration,
                    null, // no method signature
                    modifiers,
                    false, // not a loop control variable
                    null, // no super class(es)
                    null, // no interfaces implemented or extended
                    node.firstToken.beginLine,
                    node.firstToken.beginColumn,
                    node.lastToken.endLine,
                    node.lastToken.endColumn );

            this.identifierStore.add( programEntity );
        }

        walkChildren( node, data );
        return data;
    }

    @Override
    public Object visit ( ASTFormalParameter node, Object data ) {
        ArrayList<Modifier> modifiers = getModifierListFor( node );
        String typeNameString = "";
        String identifierNameString = "";
        boolean isArray = false;

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTType ) {
                typeNameString = getTypeName( (ASTType) child );
                isArray = isArrayDeclaration( (ASTType) child );
            }
            else if ( child instanceof ASTVariableDeclaratorId ) {
                identifierNameString = 
                        getIdentifierName( (ASTVariableDeclaratorId) child );
            }

            // NB varargs '...' notation is included in the method 
            // signature, but not recorded with the type.
            // Be aware that this may be a source of problems.
        }

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();

        String entityUid = tracker.getUidForLeafEntity();

        TypeName typeName = getResolvedTypeNameFor( typeNameString );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.FORMAL_ARGUMENT,
                typeName,
                isArray,
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        walkChildren( node, data );
        return data;
    }

    @Override
    public Object visit ( ASTAnnotationTypeMemberDeclaration node, Object data ) {
        // AnnotationTypeMemberDeclarationcan contain multiple types
        // and we're only interested in the member declarations that form
        // the fields of the annotation. The others wil be visited later.
        ArrayList<Modifier> modifiers
                = getModifierListFor( (ASTAnnotationTypeMemberDeclaration) node );
        String typeNameString = "";
        String identifierNameString = "";

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTType ) {
                typeNameString = getTypeName( (ASTType) child );
            }
            else if ( child instanceof ASTIdentifier ) {
                identifierNameString = ( (ASTIdentifier) child ).getText();
            }
            else {
                // plenty of other things such as enums etc that
                // will be visited later
            }
        }

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();
        String entityUid = tracker.getUidForLeafEntity();

        TypeName typeName = getResolvedTypeNameFor( typeNameString );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.ANNOTATION_MEMBER,
                typeName,
                false, // cant be an array
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        walkChildren( node, data );
        return data;
    }

    // ------------ Container Entities
    @Override
    public Object visit ( ASTAnnotationTypeDeclaration node, Object data ) {
        String identifierNameString = "";
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                identifierNameString = ( (ASTIdentifier) child ).getText();
            }
        }

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();
        String entityUid = tracker.pushContainer();
        tracker.pushType( identifierNameString );

        ArrayList<Modifier> modifiers
                = getModifierListFor( (ASTAnnotationTypeDeclaration) node );

        // NB: currently ignores the type of the annotation
        // strictly it should be "Annotation", but ...
        String typeNameString = "";   // annotation is a type with no inheritance

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.ANNOTATION,
                NO_TYPE, // this ignores the type name of the annotation - see above
                false, // not an array
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        // continue walking the tree
        walkChildren( node, data );

        // now pop this container off the stack
        tracker.pop();
        tracker.popType( identifierNameString );

        return data;
    }

    @Override
    public Object visit ( ASTConstructorDeclaration node, Object data ) {
        String methodSignature = getParametersSignature( node );
        String identifierNameString = "";

        String typeNameString = "";
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                typeNameString = ( (ASTIdentifier) child ).getText();
                identifierNameString = typeNameString;
                // constructor name and type are equivalent
            }
        }

        ArrayList<Modifier> modifierList = 
                getModifierListFor( (ASTConstructorDeclaration) node );

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();
        String entityUid = tracker.pushContainer();

        // this need to recover the class type
        // just in case we are a nested class.
        String localTypes = tracker.getLocalTypeName();
        // should we be running a comparison with the type name?
        // how do we handle generics?
        TypeName typeName = 
                getResolvedTypeNameFor( tracker.getPackageName() + "." + localTypes );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.CONSTRUCTOR,
                typeName,
                false, // not an array
                methodSignature,
                modifierList,
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        walkChildren( node, data );

        tracker.pop();

        return data;
    }

    @Override
    public Object visit ( ASTMethodDeclaration node, Object data ) {
        // extract method name & signature
        String methodName = "";
        String methodSignature = "";
        Node child, grandChild;
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            child = node.jjtGetChild( i );
            if ( child instanceof ASTMethodDeclarator ) {
                for ( int j = 0; j < child.jjtGetNumChildren(); j++ ) {
                    grandChild = child.jjtGetChild( j );
                    if ( grandChild instanceof ASTIdentifier ) {
                        methodName = ( (ASTIdentifier) grandChild ).getText();
                        break;
                    }
                }
                methodSignature = getParametersSignature( child );
            }
        }

        String identifierNameString = methodName;

        boolean returnsArray = false;

        String typeNameString = null;
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            child = node.jjtGetChild( i );
            if ( child instanceof ASTResultType ) {
                if ( "void".equals( ( (ASTResultType) child ).getText() ) ) {
                    typeNameString = "void";
                }
                else {
                    // now there should be a Type as a child
                    // and we need to go digging.
                    for ( int j = 0; j < child.jjtGetNumChildren(); j++ ) {
                        grandChild = child.jjtGetChild( j );
                        if ( grandChild instanceof ASTType ) {
                            typeNameString = getTypeName( (ASTType) grandChild );
                            returnsArray = isArrayDeclaration( (ASTType) grandChild );
                        }
                        break;
                    }
                }
                break;
            }
        }

        ArrayList<Modifier> modifierList = 
                getModifierListFor( (ASTMethodDeclaration) node );

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();
        String entityUid = tracker.pushContainer();

        TypeName typeName = getResolvedTypeNameFor( typeNameString );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.METHOD,
                typeName,
                returnsArray,
                methodSignature,
                modifierList,
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        walkChildren( node, data );

        tracker.pop();

        return data;
    }

    @Override
    public Object visit ( ASTEnumDeclaration node, Object data ) {

        String identifierNameString = "";
        String typeNameString = "";
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                identifierNameString = ( (ASTIdentifier) child ).getText();
                typeNameString = identifierNameString; // the enum is a type
            }
        }

        ArrayList<Modifier> modifierList
                = getModifierListFor( (ASTEnumDeclaration) node );

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();
        String entityUid = tracker.pushContainer();
        tracker.pushType( typeNameString );

        TypeName typeName = new TypeName(
                tracker.getPackageName()
                + "."
                + tracker.getLocalTypeName() );

        this.locallyDeclaredTypes.add(
                tracker.getPackageName()
                + "."
                + tracker.getLocalTypeName() );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.ENUMERATION,
                typeName,
                false, // can't be an array
                null, // no method signature
                modifierList,
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        // continue walking the tree
        walkChildren( node, data );

        tracker.pop();
        tracker.popType( typeNameString );
        return data;
    }

    @Override
    public Object visit ( ASTEnumConstant node, Object data ) {
        // retrieve the identifier
        String identifierNameString = null;
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                identifierNameString = ( (ASTIdentifier) child ).getText();
            }
        }

        // recover the type name, i.e. the containing enum name.
        String typeNameString = null;
        Node grandParent = node.jjtGetParent().jjtGetParent();
        for ( int i = 0; i < grandParent.jjtGetNumChildren(); i++ ) {
            Node child = grandParent.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                typeNameString = ( (ASTIdentifier) child ).getText();
            }
        }

        ArrayList<Modifier> modifierList = 
                getModifierListFor( (ASTEnumConstant) node );

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentDigest = tracker.getContainerUid();

        String entityUid = tracker.pushContainer();

        TypeName typeName = getResolvedTypeNameFor( typeNameString );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentDigest,
                entityUid,
                identifierNameString,
                Species.ENUMERATION_CONSTANT,
                typeName,
                false, // definitely not an array
                null, // no method signature
                modifierList,
                false, // not a loop control variable
                null, // no super class(es)
                null, // no interfaces implemented or extended
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        // continue walking the tree
        walkChildren( node, data );

        tracker.pop();

        return data;
    }

    @Override
    public Object visit ( ASTClassOrInterfaceDeclaration node, Object data ) {

        boolean nested = false;

        String identifierNameString = "";
        String typeNameString = "";
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                identifierNameString = ( (ASTIdentifier) child ).getText();
                typeNameString = identifierNameString; // a class and an interface are both types
            }
        }

        Species species = null;
        Node parent = node.jjtGetParent();
        if ( parent instanceof ASTTypeDeclaration ) {
            // for a top-level class ClassOrInterfaceDeclaration 
            // is a child of TypeDeclaration
            species = Species.CLASS;
        }
        else if ( parent instanceof ASTClassOrInterfaceBodyDeclaration ) {
            // for a member class ClassOrInterfaceDeclaration 
            // is a child of ClassOrInterfaceBodyDeclaration
            species = Species.MEMBER_CLASS;
            nested = true;
        }
        else if ( parent instanceof ASTBlockStatement ) {
            // identifying characteristic of local class is parent 
            // is a BlockStatement
            nested = true;
            // ok we're a local class
            species = Species.LOCAL_CLASS;

            // NB any name mangling will be left to programs 
            // extracting data from the database
        }
        else if ( parent instanceof ASTAnnotationTypeMemberDeclaration ) {
            // need to test for classes declared inside annotations
            species = Species.MEMBER_CLASS;
            nested = true;
        }

        // recover the extends and implements lists for this declaration
        ArrayList<String> extendsList = getExtendsList( node );
        ArrayList<String> implementsList = getImplementsList( node );

        // determine the species -- need to change to interface, if that is
        // what the declaration is
        if ( node.getText().equals( "interface" ) ) {
            if ( nested == false ) {
                species = Species.INTERFACE;
            }
            else {
                species = Species.NESTED_INTERFACE;
            }
        }

        ArrayList<Modifier> modifierList
                = getModifierListFor( (ASTClassOrInterfaceDeclaration) node );

        DtLocationTracker tracker = (DtLocationTracker) data;
        String parentUid = tracker.getContainerUid();
        String entityUid = tracker.pushContainer();
        TypeName typeName; // assign if exists, otherwise mark as having no type
        if ( !ANONYMOUS.equals( identifierNameString ) ) {
            tracker.pushType( identifierNameString );
            typeName = getResolvedTypeNameFor(
                    tracker.packageName + "." + tracker.getLocalTypeName() );
            this.locallyDeclaredTypes.add(
                    tracker.packageName + "." + tracker.getLocalTypeName() );
        }
        else {
            typeName = NO_TYPE;
        }

        ArrayList<TypeName> superClassList = new ArrayList<>();

        ArrayList<TypeName> superTypeList = new ArrayList<>();

        if ( species == Species.INTERFACE
                || species == Species.NESTED_INTERFACE ) {
            extendsList.stream().forEach( (name) -> {
                superTypeList.add( getResolvedTypeNameFor( name ) );
            } );
        }
        else {
            extendsList.stream().forEach( (name) -> {
                superClassList.add( getResolvedTypeNameFor( name ) );
            } );
            implementsList.stream().forEach( (name) -> {
                superTypeList.add( getResolvedTypeNameFor( name ) );
            } );
        }

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                species,
                typeName,
                false, // not an array
                null, // no method signature
                modifierList,
                false, // not a loop control variable
                superClassList,
                superTypeList,
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        // continue walking the tree
        walkChildren( node, data );

        tracker.pop();
        if ( !ANONYMOUS.equals( identifierNameString ) ) {
            tracker.popType( identifierNameString );
        }

        return data;
    }

    @Override
    public Object visit ( ASTClassOrInterfaceBody node, Object data ) {
        DtLocationTracker tracker = (DtLocationTracker) data;
        // this production is the source of anonymous classes
        // in general use or where they are used in EnumConstant.
        // However, it is also the common container for all 
        // class bodies 
        // So, inspect the parent to determine context.
        Node parent = node.jjtGetParent();
        boolean isAnonymous = ( parent instanceof ASTEnumConstant
                || parent instanceof ASTAllocationExpression );

        // now record the container
        if ( isAnonymous ) {
            String identifierNameString = ANONYMOUS;
            String parentUid = tracker.getContainerUid();
            String entityUid = tracker.pushContainer();

            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    tracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.LOCAL_CLASS,
                    NO_TYPE,
                    false, // not an array
                    null, // no method signature
                    null, // no modifiers
                    false, // not a loop control variable
                    null, // no superclasses
                    null, // no implemented interfaces
                    node.firstToken.beginLine,
                    node.firstToken.beginColumn,
                    node.lastToken.endLine,
                    node.lastToken.endColumn );

            this.identifierStore.add( programEntity );
        }

        // visit the children whatever
        walkChildren( node, data );

        if ( isAnonymous ) {
            tracker.pop();
        }

        return data;
    }

    // initialiser
    @Override
    public Object visit ( ASTInitializer node, Object data ) {
        DtLocationTracker tracker = (DtLocationTracker) data;
        String identifierNameString = ANONYMOUS;
        String parentUid = tracker.getContainerUid(); // i.e. the containing class
        String entityUid = tracker.pushContainer();

        ArrayList<Modifier> modifierList = 
                getModifierListFor( (ASTInitializer) node );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                tracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.INITIALISER,
                NO_TYPE, // no type name
                false, // not an array
                null, // no method signature
                modifierList, // no modifiers
                false, // not a loop control variable
                null, // no superclasses
                null, // no implemented interfaces
                node.firstToken.beginLine,
                node.firstToken.beginColumn,
                node.lastToken.endLine,
                node.lastToken.endColumn );

        this.identifierStore.add( programEntity );

        walkChildren( node, data );

        // pop the container off the tracker
        tracker.pop();

        return data;
    }

    // ---------- utility methods ------------
    // all are overloads of getTypeName, or getIdentfierName for
    // various node types
    // ** these may be better placed in the superclass **
    /**
     * Retrieves the type name from a type node.
     *
     * @param node a node
     * @return a type name for the node
     */
    private String getTypeName ( ASTType node ) {
        String typeNameString;

        Node child = node.jjtGetChild( 0 ); // there should be only one child
        if ( child instanceof ASTPrimitiveType ) {
            typeNameString
                    = ( (ASTPrimitiveType) child ).getText();
        }
        else {
            // we have a ReferenceType
            typeNameString = getTypeName( (ASTReferenceType) child );
        }

        return typeNameString;
    }

    /**
     * Retrieves the type name from a reference type node. This method is called
     * only as a helper by other methods.
     *
     * @param node a reference type node
     * @return A <code>Strong</code> containing the reference type name.
     */
    private String getTypeName ( ASTReferenceType node ) {
        StringBuilder typeName = new StringBuilder();

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTPrimitiveType ) {
                typeName.append( ( (ASTPrimitiveType) child ).getText() );
            }
            else if ( child instanceof ASTClassOrInterfaceType ) {
                typeName.append( getTypeName( (ASTClassOrInterfaceType) child ) );
            }
            else if ( child instanceof ASTLeftSquare || child instanceof ASTRightSquare ) {
                //ignore as token is expected;
            }
            else {
                LOGGER.warn(
                        "Unexpected child node found in ASTReferenceType node: "
                                + "line {} col {}",
                        node.firstToken.beginLine, node.firstToken.beginColumn );
            }
        }

        return typeName.toString();
    }

    private String getTypeName ( ASTClassOrInterfaceType node ) {
        StringBuilder typeName = new StringBuilder();

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );

            if ( child instanceof ASTIdentifier ) {
                typeName.append( ( (ASTIdentifier) child ).getText() );
            }
            else if ( child instanceof ASTDot ) {
                typeName.append( "." );
            }
            else if ( child instanceof ASTTypeArguments ) {
                typeName.append( getTypeName( (ASTTypeArguments) child ) );
            }
            else {
                LOGGER.warn(
                        "Unexpected child node found in ASTClassOrInterfaceType "
                                + "node: line {} col {}",
                        node.firstToken.beginLine, node.firstToken.beginColumn );
            }

        }

        return typeName.toString();
    }

    private String getTypeName ( ASTTypeArguments node ) {
        StringBuilder typeName = new StringBuilder();

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTLeftAngle ) {
                typeName.append( "<" );
            }
            else if ( child instanceof ASTRightAngle ) {
                typeName.append( ">" );
            }
            else if ( child instanceof ASTTypeArgument ) {
                typeName.append( getTypeName( (ASTTypeArgument) child ) );
            }
            else if ( child instanceof ASTComma ) {
                typeName.append( "," );
            }
            else {
                LOGGER.warn(
                        "Unexpected child node found in ASTTypeArguments node: "
                                + "line {} col {}",
                        node.firstToken.beginLine, node.firstToken.beginColumn );
            }
        }

        return typeName.toString();
    }

    private String getTypeName ( ASTTypeArgument node ) {
        StringBuilder typeName = new StringBuilder();

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTReferenceType ) {
                typeName.append( getTypeName( (ASTReferenceType) child ) );
            }
            else if ( node.getText().equals( "?" ) ) {
                typeName.append( "?" );
            }
            else if ( child instanceof ASTWildcardBounds ) {
                typeName.append( getTypeName( (ASTWildcardBounds) child ) );
            }
            else {
                LOGGER.warn(
                        "Unexpected child node found in ASTTypeArgument node: "
                                + "line {} col {}",
                        node.firstToken.beginLine, node.firstToken.beginColumn );
            }
        }

        return typeName.toString();
    }

    private String getTypeName ( ASTWildcardBounds node ) {
        StringBuilder typeName = new StringBuilder();

        typeName.append( node.getText() ); // set to either "extends" or "super"

        Node child = node.jjtGetChild( 0 ); // there is but one child node
        typeName.append( getTypeName( (ASTReferenceType) child ) );

        return typeName.toString();
    }

    private String getIdentifierName ( ASTVariableDeclarator node ) {
        StringBuilder identifierName = new StringBuilder();

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTVariableDeclaratorId ) {
                identifierName.append( getIdentifierName( (ASTVariableDeclaratorId) child ) );
            }
        }

        return identifierName.toString();
    }

    private boolean isArrayDeclaration ( ASTType node ) {
        boolean isArray = false;

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTReferenceType ) {
                isArray = isArrayDeclaration( (ASTReferenceType) child );
                break;
            }
        }

        return isArray;
    }

    private String getIdentifierName ( ASTVariableDeclaratorId node ) {
        StringBuilder identifierName = new StringBuilder();

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                identifierName.append( ( (ASTIdentifier) child ).getText() );
            }
        }

        return identifierName.toString();
    }

    private boolean isArrayDeclaration ( ASTReferenceType node ) {
        boolean isArray = false;

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTLeftSquare ) {
                isArray = true;
                break;
            }
        }

        return isArray;
    }

    // these methods recover names
    // and are used for processing imports
    private String getNameFor ( ASTImportDeclaration node ) {
        // Grammar is
        //void ImportDeclaration():
        // {}
        // {
        //   "import" [ "static" ] Name() [ Dot() StarWildcard() ] SemiColon()
        // }

        // so, process the name node
        StringBuilder output = new StringBuilder();
        for ( Integer i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTName ) {
                output.append( getNameFor( (ASTName) child ) );
            }
            else if ( child instanceof ASTDot ) {
                output.append( "." );
            }
            else if ( child instanceof ASTStarWildcard ) {
                output.append( "*" );
            }
        }

        return output.toString();
    }

    private String getNameFor ( ASTName node ) {
        // Grammar is:
        // Identifier()
        //  ( LOOKAHEAD(2) Dot() Identifier()
        //  )*
        StringBuilder output = new StringBuilder();
        for ( Integer i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTIdentifier ) {
                output.append( ( (ASTIdentifier) child ).getText() );
            }
            else if ( child instanceof ASTDot ) {
                output.append( "." );
            }
        }

        return output.toString();
    }

    // ----- type resolution routines -----
    /**
     * Tries to resolve the type name argument. The worst case scenario is
     * that the type is instantiated with the string used in the declaration!
     *
     * <p>
     * Inputs vary between fully resolved types and single strings. There
     * may also be generics and nested type names. The resources available for
     * resolution are imports, types declared within the file - though there is
     * no look forward, and java.lang repository. There may also be some
     * package local types cached somewhere.</p>
     *
     * @param typeNameString
     *
     * @return an instance of TypeName representing the argument.
     */
    private TypeName getResolvedTypeNameFor ( String typeNameString ) {
        // strip out the generics - we don't need them for some tests

        int leftAngleIndex = typeNameString.indexOf( "<" );

        String unadornedType;
        String generics = "";

        if ( leftAngleIndex == -1 ) {
            unadornedType = typeNameString;
        }
        else {
            // so we now need to remove any generics
            unadornedType = typeNameString.substring( 0, leftAngleIndex );
            generics = typeNameString.substring( leftAngleIndex );
        }

        // first we need to know what we have.
        // pick the low hanging fruit first
        if ( typeNameString.matches( "^[a-z][a-z0-9]*\\..*" ) ) {
            // then we have something that may well be an fqn

            // to instantiate, or check the caches, that is the question
            // instatiate - this method is about resolution
            // instantiation may be wasteful, but if we already have the
            // information there is no need to resolve it.
            return new TypeName( typeNameString );

        }
        else {

            // (a) check local type names
            ArrayList<String> candidateLocalTypes = new ArrayList<>();
            for ( String locallyDeclaredType : this.locallyDeclaredTypes ) {
                if ( locallyDeclaredType.endsWith( "." + unadornedType ) ) { // the dot ensures a precise match
                    candidateLocalTypes.add( locallyDeclaredType );
                }
            }

            if ( candidateLocalTypes.size() == 1 ) {
                String fullTypeName = candidateLocalTypes.get( 0 );
                if ( !generics.isEmpty() ) {
                    fullTypeName = fullTypeName + generics;
                }
                return new TypeName( fullTypeName );
            }

            // now monitor the possible issue of multiple matches
            if ( candidateLocalTypes.size() >= 2 ) {
                LOGGER.info(
                        "More than one possible explicit type name resolution "
                        + "found for: {} in file local types in file: {}",
                        typeNameString, this.javaFileName );
            }

            // (b) check imports
            ArrayList<String> matchedImports = new ArrayList<>();

            for ( String importedTypeName : imports ) {
                if ( importedTypeName.endsWith( "." + unadornedType ) ) { // need the dot to ensure a prescise match, and avoid matching ClassNode with InnerClassNode - better to be conservative.
                    matchedImports.add( importedTypeName );
                }
            }

            if ( matchedImports.size() == 1 ) {
                String fullTypeName = matchedImports.get( 0 );
                if ( !generics.isEmpty() ) {
                    fullTypeName = fullTypeName + generics;
                }
                return new TypeName( fullTypeName );
            }

            // need to consider whether there will ever be a need
            // to disambiguate.
            if ( matchedImports.size() >= 2 ) {
                LOGGER.info(
                        "More than one possible explicit type name resolution "
                                + "found for: {} in imported types in file: {}",
                        typeNameString, this.javaFileName );
            }

            // (c) check within local package types
            // (d) check java.lang - maybe this should be sooner?
        }

        // if we get here, we have only the passed in type name to go on
        return new TypeName( typeNameString );
    }

    // ----- methods for retrieving imports data -------
    private List<String> getAllImports () {
        return this.imports;
    }

    private List<String> getStarredImports () {
        ArrayList<String> packages = new ArrayList<>();

        this.imports.stream().filter( (importedClass) -> ( importedClass.endsWith( ".*" ) ) ).forEach( (importedClass) -> {
            packages.add( importedClass );
        } );

        return packages;
    }

    private List<String> getStarredImportsAsPackages () {
        ArrayList<String> packages = new ArrayList<>();

        this.imports.stream().filter( (importedClass) -> ( importedClass.endsWith( ".*" ) ) ).forEach( (importedClass) -> {
            packages.add( importedClass.substring( 0, importedClass.length() - 2 ) );
        } );

        return packages;
    }

    private List<String> getUnambiguousImports () {
        ArrayList<String> packages = new ArrayList<>();

        this.imports.stream().filter( (importedClass) -> ( !importedClass.endsWith( ".*" ) ) ).forEach( (importedClass) -> {
            packages.add( importedClass );
        } );

        return packages;
    }

    // these methods recover the names of super classes and
    // super interfaces from class or interface declaration nodes
    //
    // the names are class/interface names only. All fqn details and generics
    // are removed.
    //
    private ArrayList<String> getExtendsList ( ASTClassOrInterfaceDeclaration node ) {

        ArrayList<String> extendsList = new ArrayList<>();
        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTExtendsList ) {
                for ( int j = 0; j < child.jjtGetNumChildren(); j++ ) {
                    Node grandChild = child.jjtGetChild( j );
                    if ( grandChild instanceof ASTClassOrInterfaceType ) {
                        extendsList.add( getTypeName( (ASTClassOrInterfaceType) grandChild ) );
                    }
                }
            }
        }

        return extendsList;
    }

    private ArrayList<String> getImplementsList ( ASTClassOrInterfaceDeclaration node ) {
        ArrayList<String> implementsList = new ArrayList<>();

        for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
            Node child = node.jjtGetChild( i );
            if ( child instanceof ASTImplementsList ) {
                for ( int j = 0; j < child.jjtGetNumChildren(); j++ ) {
                    Node grandChild = child.jjtGetChild( j );
                    if ( grandChild instanceof ASTClassOrInterfaceType ) {
                        implementsList.add( getTypeName( (ASTClassOrInterfaceType) grandChild ) );
                    }
                }
            }
        }

        return implementsList;
    }

    /**
     * Returns a list of the stored modifiers for this node.
     *
     * @param node Any node that might have modifiers
     * @return a list of modifiers (Originally this was constrained to public,
     * protected, private, abstract and final. All modifiers were added 16xi12
     * for completeness.).
     */
    private ArrayList<Modifier> getModifierListFor ( SimpleNode node ) {
        ArrayList<Modifier> modifierList = new ArrayList<>();
        Integer modifiers = node.getModifiers();
        ModifierSet modifierSet = new ModifierSet();

        if ( modifierSet.isPublic( modifiers ) ) {
            modifierList.add( Modifier.PUBLIC );
        }

        if ( modifierSet.isProtected( modifiers ) ) {
            modifierList.add( Modifier.PROTECTED );
        }

        if ( modifierSet.isPrivate( modifiers ) ) {
            modifierList.add( Modifier.PRIVATE );
        }

        if ( modifierSet.isFinal( modifiers ) ) {
            modifierList.add( Modifier.FINAL );
        }

        if ( modifierSet.isAbstract( modifiers ) ) {
            modifierList.add( Modifier.ABSTRACT );
        }

        if ( modifierSet.isStatic( modifiers ) ) {
            modifierList.add( Modifier.STATIC );
        }

        if ( modifierSet.isSynchronized( modifiers ) ) {
            modifierList.add( Modifier.SYNCHRONIZED );
        }

        if ( modifierSet.isVolatile( modifiers ) ) {
            modifierList.add( Modifier.VOLATILE );
        }

        if ( modifierSet.isNative( modifiers ) ) {
            modifierList.add( Modifier.NATIVE );
        }

        if ( modifierSet.isStrictfp( modifiers ) ) {
            modifierList.add( Modifier.STRICTFP );
        }

        if ( modifierSet.isTransient( modifiers ) ) {
            modifierList.add( Modifier.TRANSIENT );
        }

        return modifierList;
    }

    // ----------- helper class --------
    /**
     * Provides a mechanism for tracking the containing program entities.
     *
     */
    private class DtLocationTracker {

        private final int HASH_LENGTH = 40;

        private MessageDigest messageDigest;

        private final String projectName;
        private String packageName;
        private String packageDigest;
        private String fileDigest;

        private ArrayDeque<String> stack;

        private ArrayList<String> typeStack;

        private final SerialNumberGenerator serialNumberGenerator;

        DtLocationTracker () {
            this.stack = new ArrayDeque<>();
            this.typeStack = new ArrayList<>();

            try {
                messageDigest = MessageDigest.getInstance( "SHA" );
            }
            catch ( NoSuchAlgorithmException e ) {
                LOGGER.error(
                        "Fatal: digest algorithm not found : {}",
                        e.getMessage() );
            }

            this.projectName = Settings.getInstance().get( "project.name" )
                    + " " + Settings.getInstance().get( "project.version" );

            this.packageName = "";   // give this a default value as package is not always set explicitly
            // NB: the package name is set to the correct value after the package declaration 
            this.packageDigest = null;

            this.fileDigest = null;

            this.serialNumberGenerator = new SerialNumberGenerator();
        }

        String getPackageName () {
            return this.packageName;
        }

        String getPackageDigest () {
            if ( this.packageDigest == null ) {
                this.packageDigest
                        = digest( this.projectName + " " + this.packageName );
            }
            return this.packageDigest;
        }

        void setPackageName ( String packageName ) {
            this.packageName = packageName;
        }

        private String getFileDigest () {
            if ( this.fileDigest == null ) {
                this.fileDigest = digest( this.projectName
                        + " " + this.packageName
                        + " " + javaFileName );
            }
            return this.fileDigest;
        }

        /**
         * Pushes a container ID on the tracker stack.
         *
         * @return the digest of the new container
         */
        String pushContainer () {
            String containerUid = getFileDigest()
                    + "-" + serialNumberGenerator.getNext();
            this.stack.push( containerUid );
            return containerUid;
        }

        /**
         * Discard a container on exit from the entity.
         */
        void pop () {
            this.stack.pop();
        }

        void pushType ( String typeName ) {
            this.typeStack.add( typeName );
        }

        void popType ( String typeName ) {
            String containingClassName
                    = this.typeStack.remove( this.typeStack.size() - 1 );
            if ( !containingClassName.equals( typeName ) ) {
                LOGGER.warn(
                        "Mismatched container type: inappropriate type pop in: {}",
                        javaFileName );
            }
        }

        /**
         * Retrieves the local class name for any location in the file. This
         * provides program entities on the tree with a means of understanding
         * the level of type nesting. This is intended to be used in constructor
         * nodes
         *
         * @return <code>null</code> if not within a type, otherwise the type
         * name which may be nested & the components separated by dots.
         */
        String getLocalTypeName () {
            if ( typeStack.isEmpty() ) {
                return null;
            }
            if ( typeStack.size() == 1 ) {
                return typeStack.get( 0 );
            }
            else {
                StringBuilder typeName = new StringBuilder();

                for ( int index = 0; index < typeStack.size(); index++ ) {
                    typeName.append( typeStack.get( index ) );
                    if ( index < ( typeStack.size() - 1 ) ) {
                        typeName.append( "." );
                    }
                }

                return typeName.toString();
            }
        }

        /**
         * Creates a UID for a leaf entity.
         */
        String getUidForLeafEntity () {
            return getFileDigest() + "-" + this.serialNumberGenerator.getNext();
        }

        /**
         * Retrieves the UID of the current container.
         *
         * @return the UID of the current container
         */
        String getContainerUid () {
            String containerUid;
            if ( this.stack.isEmpty() ) {
                containerUid = getPackageDigest();
            }
            else {
                containerUid = this.stack.peekFirst();
            }

            return containerUid;
        }

        /**
         * Generates a digest string in hex for the input string.
         *
         * @param input a string to hash.
         * @return the hash for the string.
         */
        private String digest ( String input ) {
            //  ensure the digest instance is reset
            messageDigest.reset();

            //  now compute the digest
            byte[] bytes = messageDigest.digest( input.getBytes() );

            //  write the hex digits out to a String
            StringBuilder output = new StringBuilder( HASH_LENGTH );
            for ( byte b : bytes ) {
                output.append( String.format( "%02x", b ) );
            }

            return output.toString();
        }

        /**
         * Responsible for creating integers as a serial number.
         */
        private class SerialNumberGenerator {

            private int serialNumber;

            SerialNumberGenerator () {
                this.serialNumber = 0;
            }

            int getNext () {
                this.serialNumber++;
                return this.serialNumber;
            }
        }

    }

}
