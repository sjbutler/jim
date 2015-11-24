/*
 Copyright (C) 2013-2015 The Open University

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

package uk.ac.open.crc.jim.parser.java17;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.open.crc.idtk.Modifier;
import uk.ac.open.crc.idtk.Species;
import uk.ac.open.crc.idtk.TypeName;
import uk.ac.open.crc.jim.parser.java17.JavaParser.VariableDeclaratorIdContext;
import uk.ac.open.crc.jim.parser.java17.JavaParser.VariableModifierContext;
import uk.ac.open.crc.jim.persistence.EntityStore;
import uk.ac.open.crc.jim.Settings;
import uk.ac.open.crc.jimdb.RawProgramEntity;

/**
 * A visitor implementation for the ANTLR4 Java grammar.
 *
 */
public class Java17VisitorImplementation extends JavaBaseVisitor<String> {

    /**
     * An illegal Java identifier name used to positively mark program entities
     * that are anonymous.
     */
    private static final String ANONYMOUS = "#anonymous#";

    private static final String VOID = "void";

    private static final TypeName NO_TYPE = new TypeName( "#no type#" );

    private static final Logger LOGGER
            = LoggerFactory.getLogger( Java17VisitorImplementation.class );

    private static final int COLUMN_ADJUSTMENT = 1; // ANTLR counts from 0

    private final String javaFileName;
    private final EntityStore identifierStore;

    /**
     * A store for the FQNs of imported types
     */
    private final ArrayList<String> imports;

    /**
     * A store of FQNs of types declared within the parsed file
     */
    private final ArrayList<String> locallyDeclaredTypes;

    private final LocationTracker locationTracker;

    /**
     * To maintain a list of current modifiers. Must be managed with infinite
     * care. (if used)
     */
    private final ArrayList<String> modifierList;

    public Java17VisitorImplementation (
            String javaFileName,
            EntityStore entityStore ) {
        super();

        this.javaFileName = javaFileName;
        this.identifierStore = entityStore;

        this.imports = new ArrayList<>();
        this.locallyDeclaredTypes = new ArrayList<>();

        // now initialise the location tracker
        // sadly it cannot be dropped through
        // but we should be able to replicate the original behaviour.
        this.locationTracker = new LocationTracker();

        this.modifierList = new ArrayList<>();
    }

    /*
     *  Package name visitor method 
     *  -- must update LocationTracker instance
     */
    @Override
    public String visitPackageDeclaration ( JavaParser.PackageDeclarationContext context ) {

        this.locationTracker.setPackageName( context.qualifiedName().getText() );

        return visitChildren( context );  // surely this is an unnecessary call: there are no children to visit.
    }

    // imports
    @Override
    public String visitImportDeclaration ( JavaParser.ImportDeclarationContext context ) {

        // production is: 'import' 'static'? qualifiedName ('.' '*')? ';'
        String importName = context.qualifiedName().getText();

        // need to check the end of statement to see if it is '.*' wildcard
        // lacks a certain finesse, but sophistication would require more 
        // grammar productions and additional code
        if ( context.getText().endsWith( ".*;" ) ) {
            importName += ".*";
        }

        this.imports.add( importName );

        return visitChildren( context );  // surely this is an unnecessary call: there are no children to visit.
    }

    // annotation, class, enum | interface declaration
    // this method is a generic production and is used to
    // prepare to collect the modifiers. 
    @Override
    public String visitTypeDeclaration ( JavaParser.TypeDeclarationContext context ) {
        // set the modifier list to a known state
        this.modifierList.clear();

        return visitChildren( context ); // data is collected in each of the productions
    }

    @Override
    public String visitModifier ( JavaParser.ModifierContext context ) {

        if ( context.classOrInterfaceModifier() == null ) {
            this.modifierList.add( context.getText() );
        }

        return visitChildren( context );  // forces an annotation to be visited.
    }

    @Override
    public String visitClassOrInterfaceModifier ( 
            JavaParser.ClassOrInterfaceModifierContext context ) {

        if ( context.annotation() == null ) {
            this.modifierList.add( context.getText() );
        }

        return visitChildren( context );  // forces an annotation to be visited.
    }

    @Override
    public String visitVariableModifier ( 
            JavaParser.VariableModifierContext context ) {

        if ( context.annotation() == null ) {
            this.modifierList.add( context.getText() );
        }

        return visitChildren( context ); // forces an annotation to be visited.
    }

    // ----  type declarations
//    classDeclaration
//    :   'class' Identifier typeParameters?
//        ('extends' type)?
//        ('implements' typeList)?
//        classBody
//    ;
    @Override
    public String visitClassDeclaration ( JavaParser.ClassDeclarationContext context ) {
        // read the modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        String identifierNameString;
        if ( context.Identifier() != null ) {
            identifierNameString = context.Identifier().getText();
        }
        else {
            identifierNameString = ANONYMOUS;
        }

        if ( context.typeParameters() != null ) {
            // type parameters are present
        }

        Species species = getSpeciesFor( context );

        ArrayList<String> extendsList = getExtendsListFor( context );
        ArrayList<String> implementsList = getTypeNameListFor( context.typeList() );

        ArrayList<TypeName> superClassList = resolvedTypeListFor( extendsList );

        ArrayList<TypeName> superTypeList = resolvedTypeListFor( implementsList );

        // sort out tracker state
        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();

        TypeName typeName; // assign if exists, otherwise mark as having no type
        if ( !ANONYMOUS.equals( identifierNameString ) ) {
            this.locationTracker.pushType( identifierNameString );
            typeName = getResolvedTypeNameFor( this.locationTracker.packageName
                    + "." + this.locationTracker.getLocalTypeName() );
            this.locallyDeclaredTypes.add( this.locationTracker.packageName
                    + "." + this.locationTracker.getLocalTypeName() );
        }
        else {
            typeName = NO_TYPE;
        }

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                species,
                typeName,
                false, // not an array
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                superClassList,
                superTypeList,
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        // clear the modifier list before visiting the children
        this.modifierList.clear();
        // walk the remainder of this branch
        visitChildren( context.classBody() );

        this.locationTracker.pop();
        if ( !ANONYMOUS.equals( identifierNameString ) ) {
            this.locationTracker.popType( identifierNameString );
        }

        return "";
    }

    // enumDeclaration
//    enumDeclaration
//    :   ENUM Identifier ('implements' typeList)?
//        '{' enumConstants? ','? enumBodyDeclarations? '}'
//    ;
    @Override
    public String visitEnumDeclaration ( JavaParser.EnumDeclarationContext context ) {
        // read the modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // grab the identifier
        String identifierNameString = context.Identifier().getText();

        ArrayList<String> implementsList = getTypeNameListFor( context.typeList() );
        ArrayList<TypeName> superTypeList = resolvedTypeListFor( implementsList );

        // sort out tracker state
        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();

        TypeName typeName; // assign if exists, otherwise mark as having no type
        this.locationTracker.pushType( identifierNameString );
        typeName = getResolvedTypeNameFor(
                this.locationTracker.packageName
                + "."
                + this.locationTracker.getLocalTypeName() );
        this.locallyDeclaredTypes.add(
                this.locationTracker.packageName
                + "."
                + this.locationTracker.getLocalTypeName() );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.ENUMERATION,
                typeName,
                false, // not an array
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null,
                superTypeList,
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        // clear the modifier list before visiting the children
        this.modifierList.clear();
        visitChildren( context ); // could be more specific

        this.locationTracker.pop();
        this.locationTracker.popType( identifierNameString );

        return "";
    }

//  interfaceDeclaration
//    :   'interface' Identifier typeParameters? ('extends' typeList)? interfaceBody
//    ;
    @Override
    public String visitInterfaceDeclaration ( JavaParser.InterfaceDeclarationContext context ) {
        // read the modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // grab the identifier
        String identifierNameString = context.Identifier().getText();
        // ignore the type parameters -- but process when we implement that aspect
        ArrayList<String> extendsList = getTypeNameListFor( context.typeList() );
        ArrayList<TypeName> superTypeList = resolvedTypeListFor( extendsList );

        // determine the species
        Species species = getSpeciesFor( context );

        // sort out tracker state
        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();

        TypeName typeName; // assign if exists, otherwise mark as having no type
        this.locationTracker.pushType( identifierNameString );
        typeName = getResolvedTypeNameFor(
                this.locationTracker.packageName
                + "."
                + this.locationTracker.getLocalTypeName() );
        this.locallyDeclaredTypes.add(
                this.locationTracker.packageName
                + "."
                + this.locationTracker.getLocalTypeName() );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                species,
                typeName,
                false, // not an array
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null,
                superTypeList,
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        // clear the modifier list before visiting the children
        this.modifierList.clear();
        visitChildren( context.interfaceBody() );

        this.locationTracker.pop();
        this.locationTracker.popType( identifierNameString );

        return "";
    }

//    annotationTypeDeclaration
//    :   '@' 'interface' Identifier annotationTypeBody
//    ;
    // modifiers are found in the parent production so will be 
    // available if they have been collected correctly
    @Override
    public String visitAnnotationTypeDeclaration ( JavaParser.AnnotationTypeDeclarationContext context ) {
        // read the modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // grab the identifier
        String identifierNameString = context.Identifier().getText();

        // sort out tracker state
        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();
        this.locationTracker.pushType( identifierNameString );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.ANNOTATION,
                NO_TYPE,
                false, // not an array
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null,
                null,
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        this.modifierList.clear();
        visitChildren( context.annotationTypeBody() );

        this.locationTracker.pop();
        this.locationTracker.popType( identifierNameString );

        return "";
    }

    // ---- end of type declarations
    // ---- method declarations
    // constructor
//    constructorDeclaration
//    :   Identifier formalParameters ('throws' qualifiedNameList)?
//        constructorBody
//    ;
    // modifiers part of parent production
    @Override
    public String visitConstructorDeclaration ( JavaParser.ConstructorDeclarationContext context ) {
        // read the modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // grab the identifier
        String identifierNameString = context.Identifier().getText();

        // sort out tracker state
        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();

        // this needed to recover the class type
        // just in case we are a nested class.
        String localType = this.locationTracker.getLocalTypeName();
        // should we be running a comparison with the type name?
        TypeName typeName = getResolvedTypeNameFor(
                this.locationTracker.getPackageName() + "." + localType );

        String methodSignature = createMethodSignatureFromFormalParameters(
                context.formalParameters() );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.CONSTRUCTOR,
                typeName,
                false, // not an array
                methodSignature, // method signature
                modifiers,
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        this.modifierList.clear();

        visitChildren( context );

        this.locationTracker.pop();

        return "";
    }

    // -- genericConstructorDeclaration 
    // -- wraps the above with additional type parameters
    // -- that are not collected, so no visitor implemented.
//    methodDeclaration
//    :   (type|'void') Identifier formalParameters ('[' ']')*
//        ('throws' qualifiedNameList)?
//        (   methodBody
//        |   ';'
//        )
//    ;
    @Override
    public String visitMethodDeclaration ( JavaParser.MethodDeclarationContext context ) {
        // collect the modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // grab the identifier
        String identifierNameString = context.Identifier().getText();

        // sort out tracker state
        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();

        // this needed to recover the class type
        // just in case we are a nested class.
        String returnTypeString;
        boolean isArrayReturnType;
        if ( context.type() != null ) {
            returnTypeString = getTypeName( context.type() );
            isArrayReturnType = isArrayDeclaration( context.type() );
        }
        else {
            returnTypeString = VOID;
            isArrayReturnType = false;
        }

        TypeName typeName = getResolvedTypeNameFor( returnTypeString );

        String methodSignature
                = createMethodSignatureFromFormalParameters( context.formalParameters() );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.METHOD,
                typeName,
                isArrayReturnType,
                methodSignature, // method signature
                modifiers,
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.getParent().start.getLine(), // the parent contains the modifiers & thus the whole production
                context.getParent().start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(), // however, end of this rule should be conincident with end of parent
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        this.modifierList.clear();
        visitChildren( context );  // formal arguments, throws list and body

        this.locationTracker.pop();

        return "";
    }

//    interfaceMethodDeclaration
//    :   (type|'void') Identifier formalParameters ('[' ']')*
//        ('throws' qualifiedNameList)?
//        ';'
//    ;    
    @Override
    public String visitInterfaceMethodDeclaration ( JavaParser.InterfaceMethodDeclarationContext context ) {
        // collect the modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // grab the identifier
        String identifierNameString = context.Identifier().getText();

        // sort out tracker state
        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();

        // this needed to recover the class type
        // just in case we are a nested class.
        String returnTypeString;
        boolean isArrayReturnType;
        if ( context.type() != null ) {
            returnTypeString = getTypeName( context.type() );
            isArrayReturnType = isArrayDeclaration( context.type() );
        }
        else {
            returnTypeString = VOID;
            isArrayReturnType = false;
        }

        TypeName typeName = getResolvedTypeNameFor( returnTypeString );

        String methodSignature
                = createMethodSignatureFromFormalParameters( context.formalParameters() );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.METHOD,
                typeName,
                isArrayReturnType,
                methodSignature, // method signature
                modifiers,
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.getParent().start.getLine(), // the parent contains the modifiers & thus the whole production
                context.getParent().start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(), // however, end of this rule should be conincident with end of parent
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        this.modifierList.clear();
        visitChildren( context );  // formal arguments and throws list

        this.locationTracker.pop();

        return "";
    }

    // There are also two generic method declaration productions that 
    // wrap methodDeclaration
    // both permit the inclusion of typeParameters which are currently ignored
    //
    // ---- end of method declarations
    // ---- initializer block
    // initializer blocks need to be detected to understand what they 
    // are as they are not explicitly declared in the grammar
    // So, the 'block' needs to be visited and its parent and neighbour
    // checked 
    // see -- classBodyDeclaration (covers classes and enums, and annotations.)
    // In other words, if block is a child of classBodyDeclaration it is an initialiser
    // and we are interested in it. 
    //
//    classBodyDeclaration
//    :   ';'
//    |   'static'? block
//    |   modifier* memberDeclaration
//    ;
    //
    @Override
    public String visitBlock ( JavaParser.BlockContext context ) {

        ParserRuleContext parent = context.getParent();
        if ( parent instanceof JavaParser.ClassBodyDeclarationContext ) {
            String identifierNameString = ANONYMOUS;
            String parentUid = this.locationTracker.getContainerUid(); // i.e. the containing class
            String entityUid = this.locationTracker.pushContainer();

            // The only possible modifier is static, but it is declared as 
            // a lexical constant in the grammar. Need to test if it is present
            ArrayList<Modifier> modifiers = new ArrayList<>();

            // Need to establish that this test is correct
            if ( "static".equals( parent.start.getText() ) ) {
                modifiers.add( Modifier.STATIC );
            }

            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    this.locationTracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.INITIALISER,
                    NO_TYPE, // no type name
                    false, // not an array
                    null, // no method signature
                    modifiers,
                    false, // not a loop control variable
                    null, // no superclasses
                    null, // no implemented interfaces
                    context.start.getLine(),
                    context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                    context.stop.getLine(),
                    context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

            this.identifierStore.add( programEntity );

            this.modifierList.clear();
            visitChildren( context );

            this.locationTracker.pop();

            return "";
        }
        else {
            visitChildren( context );
            return "";
        }
    }

    // ---- end of initializer block
    // ---- leaf declarations
//    fieldDeclaration
//    :   type variableDeclarators ';'
//    ;
    //    variableDeclarators
    //    :   variableDeclarator (',' variableDeclarator)*
    //    ;
    //
    //    variableDeclarator
    //    :   variableDeclaratorId ('=' variableInitializer)?
    //    ;
    //
    //    variableDeclaratorId
    //    :   Identifier ('[' ']')*
    //    ;
    // awkward declaration and variableDeclarators is used in other contexts
    //
    //
    @Override
    public String visitFieldDeclaration ( JavaParser.FieldDeclarationContext context ) {
        // sort out modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // sort out the type
        TypeName typeName = getResolvedTypeNameFor( getTypeName( context.type() ) );
        boolean isArrayDeclaration = isArrayDeclaration( context.type() );

        // iterate over the list of declarations
        context.variableDeclarators().variableDeclarator()
                .stream().forEach( declaration -> {
            String parentUid = this.locationTracker.getContainerUid();
            String entityUid = this.locationTracker.getUidForLeafEntity();
            VariableDeclaratorIdContext variableIdentifierContext
                    = declaration.variableDeclaratorId();
            String identifierNameString
                    = declaration.variableDeclaratorId().Identifier().getText();
            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    this.locationTracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.FIELD,
                    typeName,
                    isArrayDeclaration,
                    null, // method signature
                    modifiers,
                    false, // not a loop control variable
                    null, // no superclasses
                    null, // no supertypes
                    variableIdentifierContext.start.getLine(), // can only approximate without making assumptions that might not hold
                    variableIdentifierContext.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                    variableIdentifierContext.stop.getLine(), // end of the name, no more as we don't know what is next
                    variableIdentifierContext.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT + identifierNameString.length() );

            this.identifierStore.add( programEntity );
        });

        this.modifierList.clear();
        visitChildren( context );  // pointless atm

        return "";
    }

    // 
    // interface constant
    // -- constDeclaration & constantDeclarator 
    // distinguished by grammar authors for some reason
    @Override
    public String visitConstDeclaration ( JavaParser.ConstDeclarationContext context ) {
        // sort out modifiers
        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // sort out the type
        TypeName typeName = getResolvedTypeNameFor( getTypeName( context.type() ) );
        boolean isArrayDeclaration = isArrayDeclaration( context.type() );

        // iterate over the list of declarations
        context.constantDeclarator().stream().forEach( declaration -> {
            String parentUid = this.locationTracker.getContainerUid();
            String entityUid = this.locationTracker.getUidForLeafEntity();
            String identifierNameString = declaration.Identifier().getText();
            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    this.locationTracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.FIELD,
                    typeName,
                    isArrayDeclaration,
                    null, // method signature
                    modifiers,
                    false, // not a loop control variable
                    null, // no superclasses
                    null, // no supertypes
                    declaration.start.getLine(), // can only approximate as cannot get desired level of detail
                    declaration.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                    declaration.stop.getLine(), // end of declaration -- may be end of name, may be end of initialiser
                    declaration.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

            this.identifierStore.add( programEntity );
        });

        this.modifierList.clear();
        visitChildren( context );  // pointless atm

        return "";
    }

//    formalParameter
//    :   variableModifier* type variableDeclaratorId
//    ;
    @Override
    public String visitFormalParameter ( JavaParser.FormalParameterContext context ) {
        this.modifierList.clear();
        List<VariableModifierContext> variableModifiers = context.variableModifier();
        if ( variableModifiers != null ) {
            variableModifiers.stream().forEach( (variableModifier) -> {
                visit( variableModifier );
            } );
        }

        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // sort out the type
        TypeName typeName = getResolvedTypeNameFor( getTypeName( context.type() ) );
        boolean isArrayDeclaration = isArrayDeclaration( context.type() );

        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.getUidForLeafEntity();

        String identifierNameString
                = context.variableDeclaratorId().Identifier().getText();

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.FORMAL_ARGUMENT,
                typeName,
                isArrayDeclaration,
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        return "";
    }

//    lastFormalParameter
//    :   variableModifier* type '...' variableDeclaratorId
//    ;
    @Override
    public String visitLastFormalParameter ( JavaParser.LastFormalParameterContext context ) {
        this.modifierList.clear();
        List<VariableModifierContext> variableModifiers = context.variableModifier();
        if ( variableModifiers != null ) {
            variableModifiers.stream().forEach( (variableModifier) -> {
                visit( variableModifier );
            } );
        }

        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // sort out the type
        TypeName typeName = getResolvedTypeNameFor( getTypeName( context.type() ) );
        boolean isArrayDeclaration = isArrayDeclaration( context.type() );

        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.getUidForLeafEntity();

        String identifierNameString
                = context.variableDeclaratorId().Identifier().getText();

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.FORMAL_ARGUMENT,
                typeName,
                isArrayDeclaration,
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        return "";
    }

    // local variable
    // -- localVariableDeclaration
    @Override
    public String visitLocalVariableDeclaration ( 
            JavaParser.LocalVariableDeclarationContext context ) {
        this.modifierList.clear();
        List<VariableModifierContext> variableModifiers = context.variableModifier();
        if ( variableModifiers != null ) {
            variableModifiers.stream().forEach( (variableModifier) -> {
                visit( variableModifier );
            } );
        }

        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // sort out the type
        TypeName typeName = getResolvedTypeNameFor( getTypeName( context.type() ) );
        boolean isArrayDeclaration = isArrayDeclaration( context.type() );
        boolean isLoopControlVariable
                = ( context.getParent() instanceof JavaParser.ForInitContext );
//        List<VariableDeclaratorContext> declarations
//                = 
                
        context.variableDeclarators().variableDeclarator()
                .stream().forEach( declaration -> {
            String parentUid = this.locationTracker.getContainerUid();
            String entityUid = this.locationTracker.getUidForLeafEntity();

            String identifierNameString
                    = declaration.variableDeclaratorId().Identifier().getText();

            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    this.locationTracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.LOCAL_VARIABLE,
                    typeName,
                    isArrayDeclaration,
                    null, // no method signature
                    modifiers,
                    isLoopControlVariable,
                    null, // no superclasses
                    null, // no supertypes
                    declaration.variableDeclaratorId().start.getLine(),
                    declaration.variableDeclaratorId().start.getCharPositionInLine() 
                            + COLUMN_ADJUSTMENT,
                    declaration.variableDeclaratorId().stop.getLine(),
                    declaration.variableDeclaratorId().stop.getCharPositionInLine() 
                            + COLUMN_ADJUSTMENT + identifierNameString.length() );

            this.identifierStore.add( programEntity );
        });

        return "";
    }

    // -- enhancedForControl --- ignores localVariableDeclaration
    @Override
    public String visitEnhancedForControl ( JavaParser.EnhancedForControlContext context ) {
        this.modifierList.clear();
        List<VariableModifierContext> variableModifiers = context.variableModifier();
        if ( variableModifiers != null ) {
            variableModifiers.stream().forEach( (variableModifier) -> {
                visit( variableModifier );
            } );
        }

        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        // sort out the type
        TypeName typeName = getResolvedTypeNameFor( getTypeName( context.type() ) );
        boolean isArrayDeclaration = isArrayDeclaration( context.type() );

        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.getUidForLeafEntity();

        String identifierNameString = context.Identifier().getText();

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.LOCAL_VARIABLE,
                typeName,
                isArrayDeclaration,
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        return "";
    }

    // enum member
    // -- enumConstant
    // In practice we grab the identifier and store it, and walk any class body.
    // NB in practice this is often a leaf node, but that is not always the case.
    @Override
    public String visitEnumConstant ( JavaParser.EnumConstantContext context ) {
        this.modifierList.clear(); // there are no modifiers, so this is just housekeeping.

        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.pushContainer();

        String identifierNameString = context.Identifier().getText();

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.ENUMERATION_CONSTANT,
                NO_TYPE,
                false, // can't be an array
                null, // no method signature
                null, // no modifiers
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        visitChildren( context ); // walk the child class body

        this.locationTracker.pop();
        return "";
    }

    // annotation member
    // -- elementValuePair
//    elementValuePair
//    :   Identifier '=' elementValue
    @Override
    public String visitElementValuePair ( JavaParser.ElementValuePairContext context ) {
        this.modifierList.clear(); // there are no modifiers, so this is just housekeeping.

        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.getUidForLeafEntity();

        String identifierNameString = context.Identifier().getText();

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.ANNOTATION_MEMBER,
                NO_TYPE,
                false, // can't be an array
                null, // method signature
                null, // no modifiers
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.stop.getLine(),
                context.stop.getCharPositionInLine() + COLUMN_ADJUSTMENT );

        this.identifierStore.add( programEntity );

        return "";
    }

    // try with resources
    // new in Java 1.7
    // each resource declaration is made separately, so we visit those
//    resources
//    :   variableModifier* classOrInterfaceType variableDeclaratorId '=' expression
//    ;
    //
    @Override
    public String visitResource ( JavaParser.ResourceContext context ) {
        // collect the modifiers
        this.modifierList.clear();
        List<VariableModifierContext> variableModifiers = context.variableModifier();
        if ( variableModifiers != null ) {
            variableModifiers.stream().forEach( (variableModifier) -> {
                visit( variableModifier );
            } );
        }

        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        String parentUid = this.locationTracker.getContainerUid();
        String entityUid = this.locationTracker.getUidForLeafEntity();

        String identifierNameString = context.variableDeclaratorId().Identifier().getText();

        // this may be risky -- may need to extract name with more care
        // for particular circumstances.
        TypeName typeName = getResolvedTypeNameFor( context.classOrInterfaceType().getText() );

        RawProgramEntity programEntity = new RawProgramEntity(
                this.javaFileName,
                this.locationTracker.getPackageName(),
                parentUid,
                entityUid,
                identifierNameString,
                Species.LOCAL_VARIABLE,
                typeName,
                false, // can't be an array
                null, // no method signature
                modifiers,
                false, // not a loop control variable
                null, // no superclasses
                null, // no supertypes
                context.start.getLine(),
                context.start.getCharPositionInLine() + COLUMN_ADJUSTMENT,
                context.variableDeclaratorId().stop.getLine(),
                context.variableDeclaratorId().stop.getCharPositionInLine() + COLUMN_ADJUSTMENT + identifierNameString.length() );

        this.identifierStore.add( programEntity );
        return "";
    }

    // multiple types in catch 
    // new in Java 1.7
//  catchClause
//    :   'catch' '(' variableModifier* catchType Identifier ')' block
//    ;
//
//  catchType
//    :   qualifiedName ('|' qualifiedName)*
//    ;
    // need to test the number of qualifiedName children to store each type separately.
    @Override
    public String visitCatchClause ( JavaParser.CatchClauseContext context ) {
        // collect the modifiers
        this.modifierList.clear();
        List<VariableModifierContext> variableModifiers = context.variableModifier();
        if ( variableModifiers != null ) {
            variableModifiers.stream().forEach( (variableModifier) -> {
                visit( variableModifier );
            } );
        }

        ArrayList<Modifier> modifiers = getModifiersFromList( this.modifierList );

        String parentUid = this.locationTracker.getContainerUid();

        String identifierNameString = context.Identifier().getText();

        // now recover the type(s) which are stored as qualifiedNames
        context.catchType().qualifiedName()
                .stream().forEach( qualifiedName -> {
            TypeName typeName = getResolvedTypeNameFor( qualifiedName.getText() );
            String entityUid = this.locationTracker.getUidForLeafEntity();
            RawProgramEntity programEntity = new RawProgramEntity(
                    this.javaFileName,
                    this.locationTracker.getPackageName(),
                    parentUid,
                    entityUid,
                    identifierNameString,
                    Species.LOCAL_VARIABLE,
                    typeName,
                    false, // can't be an array
                    null, // method signature
                    modifiers,
                    false, // not a loop control variable
                    null, // no superclasses
                    null, // no supertypes
                    context.Identifier().getSymbol().getLine(),
                    context.Identifier().getSymbol().getCharPositionInLine() 
                            + COLUMN_ADJUSTMENT,
                    context.Identifier().getSymbol().getLine(),
                    context.Identifier().getSymbol().getCharPositionInLine() 
                            + COLUMN_ADJUSTMENT + identifierNameString.length() );

            this.identifierStore.add( programEntity );

        });

        return "";
    }

    // ---- end of leaf declarations
    // ----------- helper methods -------
    private ArrayList<Modifier> getModifiersFromList ( ArrayList<String> modifierList ) {
        ArrayList<Modifier> modifiers = new ArrayList<>();

        modifierList.stream().forEach( modifierString -> {
            modifiers.add( Modifier.getModifierFor( modifierString ) );
        } );

        return modifiers;
    }

    private Species getSpeciesFor ( JavaParser.ClassDeclarationContext context ) {
        Species species;
        ParserRuleContext parent = context.getParent();
        ParserRuleContext grandParent = parent.getParent(); // at worst this will be compilationUnit
        if ( parent instanceof JavaParser.TypeDeclarationContext ) {
            if ( grandParent instanceof JavaParser.CompilationUnitContext ) {
                // top level class is a child of typeDeclaration in compilationUnit
                species = Species.CLASS;
            }
            else if ( grandParent instanceof JavaParser.BlockStatementContext ) {
                // only other grandParent is blockStatement
                species = Species.LOCAL_CLASS;
            }
            else {
                // sanity check -- we should never be here
                // intended for development
                LOGGER.error(
                        "Class declaration found in unexpected context in {} at line: {}",
                        this.javaFileName, context.start.getLine() );
                throw new IllegalStateException( "Class found in unexpected context" );
            }
        }
        else if ( parent instanceof JavaParser.MemberDeclarationContext
                || parent instanceof JavaParser.InterfaceMemberDeclarationContext ) {
            // member class is a child of memberDeclaration in class or interface
            species = Species.MEMBER_CLASS;
        }
        else if ( parent instanceof JavaParser.AnnotationTypeElementRestContext ) {
            // this definition has worked in the JavaCC visitor.
            // There may, however, be a need to change it.
            species = Species.MEMBER_CLASS;
        }
        else {
            // second sanity check -- we should never be here
            // intended for development
            LOGGER.error(
                    "Class declaration found in unexpected context in {} at line: {}",
                    this.javaFileName, context.start.getLine() );
            throw new IllegalStateException( "Class found in unexpected context" );
        }

        return species;
    }

    private Species getSpeciesFor ( JavaParser.InterfaceDeclarationContext context ) {
        Species species;
        ParserRuleContext parent = context.getParent();
        // need to know if the interface is top-level or nested.
        // this hinges on the parent 
        if ( parent instanceof JavaParser.TypeDeclarationContext ) {
            species = Species.INTERFACE;
        }
        else {
            species = Species.NESTED_INTERFACE;
        }

        return species;
    }

    /**
     * Creates a list of the raw type strings found in the {@code typeList} production.
     *
     * @param typeList a {@code typeList} production.
     * @return a list of strings containing the verbatim types, including generics
     * given as types in the {@code typeList} production;
     */
    private ArrayList<String> getListOfTypes ( JavaParser.TypeListContext typeList ) {
        ArrayList<String> listOfTypes = new ArrayList<>();

        typeList.type().stream().forEach( (type) -> {
            listOfTypes.add( type.getText() );
        } );

        return listOfTypes;
    }

    private ArrayList<String> getExtendsListFor ( 
            JavaParser.ClassDeclarationContext context ) {
        ArrayList<String> extendsList = new ArrayList<>();
        if ( context.type() != null ) {
            // then a class is being extended
            extendsList.add( context.type().getText() );
        }

        return extendsList;
    }

    private ArrayList<String> getTypeNameListFor ( 
            JavaParser.TypeListContext context ) {
        ArrayList<String> extendsList;
        if ( context != null ) {
            // one or more interfaces are implemented
            extendsList = getListOfTypes( context );
        }
        else {
            extendsList = new ArrayList<>();
        }

        return extendsList;
    }

    private ArrayList<TypeName> resolvedTypeListFor ( 
            ArrayList<String> typeDeclarationList ) {
        ArrayList<TypeName> typeList = new ArrayList<>();

        typeDeclarationList.stream().forEach( (name) -> {
            typeList.add( getResolvedTypeNameFor( name ) );
        } );

        return typeList;
    }

    private String createMethodSignatureFromFormalParameters ( 
            JavaParser.FormalParametersContext parametersContext ) {
        StringBuilder signature = new StringBuilder( "(" );

        // The context is not null, but might have no children
        JavaParser.FormalParameterListContext parameterListContext = 
                parametersContext.formalParameterList();
        if ( parameterListContext != null ) { // i.e. there are formal parameters
            List<JavaParser.FormalParameterContext> parameterList = 
                    parameterListContext.formalParameter();
            for ( JavaParser.FormalParameterContext formalParameter : parameterList ) {
                signature.append( formalParameter.type().getText() );
                signature.append( ";" );  // delimiter for later parsing
            }

            JavaParser.LastFormalParameterContext lastFormalParameter = 
                    parameterListContext.lastFormalParameter();
            if ( lastFormalParameter != null ) {
                signature.append( lastFormalParameter.type().getText() ).append( "..." );
                signature.append( ";" );  // delimiter for later parsing
            }
        }

        signature.append( ")" );

        return signature.toString();
    }

    // ----- type resolution routines -----
    // TODO -- review and revise
    // Shuldn't this be somewhere central as it is a common routine? idtk-j?
    /**
     * Tries to resolve the type name argument. The worst case scenario is that
     * the type is instantiated with the string used in the declaration!
     *
     * <p>
     * Inputs vary between fully resolved types and single strings. There may
     * also be generics and nested type names. The resources available for
     * resolution are imports, types declared within the file - though there is
     * no look forward, and java.lang repository. There may also be some package
     * local types cached somewhere.</p>
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
            unadornedType = typeNameString.substring( 0, typeNameString.indexOf( "<" ) );
            generics = typeNameString.substring( typeNameString.indexOf( "<" ) );
        }

        // first we need to know what we have.
        // pick the low hanging fruit first
        if ( typeNameString.matches( "^[a-z][a-z0-9]*\\..*" ) ) {
            // then we have something that may well be an fqn

            // to instantiate, or check the caches, that is the question
            // we instatiate - this method is about resolution
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

    private String getTypeName ( JavaParser.TypeContext context ) {
        String typeName;
        if ( context.classOrInterfaceType() != null ) {
            typeName = context.classOrInterfaceType().getText();
        }
        else {
            typeName = context.primitiveType().getText();
        }

        return typeName;
    }

    private boolean isArrayDeclaration ( JavaParser.TypeContext context ) {
        return context.getText().contains( "[" );
    }

    // ----------- helper class --------
    /**
     * Provides a mechanism for tracking the containing program entities.
     *
     */
    private class LocationTracker {

        private final int HASH_LENGTH = 40;

        private MessageDigest messageDigest;

        private final String projectName;
        private String packageName;
        private String packageDigest;
        private String fileDigest;

        private final ArrayDeque<String> stack;

        private final ArrayList<String> typeStack;

        private final SerialNumberGenerator serialNumberGenerator;

        private LocationTracker () {
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
                    + " "
                    + Settings.getInstance().get( "project.version" );

            this.packageName = "";   // give this a default value as package is not always set explicitly
            // NB: the package name is set to the correct value following the package declaration 
            this.packageDigest = null;

            this.fileDigest = null;

            this.serialNumberGenerator = new SerialNumberGenerator();
        }

        String getPackageName () {
            return this.packageName;
        }

        String getPackageDigest () {
            if ( this.packageDigest == null ) {
                this.packageDigest = digest( this.projectName + " " + this.packageName );
            }
            return this.packageDigest;
        }

        void setPackageName ( String packageName ) {
            this.packageName = packageName;
        }

        private String getFileDigest () {
            if ( this.fileDigest == null ) {
                this.fileDigest = digest(
                        this.projectName
                        + " "
                        + this.packageName
                        + " "
                        + javaFileName );
            }
            return this.fileDigest;
        }

        /**
         *
         * @param containerName must be the signature for methods and
         *                      constructors.
         * @return the digest of the container name
         */
        String pushContainer () {
            String containerUid = getFileDigest()
                    + "-"
                    + serialNumberGenerator.getNext();
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
            String containingClassName = this.typeStack.remove( this.typeStack.size() - 1 );
            if ( !containingClassName.equals( typeName ) ) {
                LOGGER.warn(
                        "Mismatched container type: inappropriate type pop in: {0}",
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
         * @return the UID of the current parent/container entity
         */
        String getContainerUid () {
            String containerUid;
            if ( this.stack.isEmpty() ) {
                containerUid = getPackageDigest();
            }
            else {
                containerUid = this.stack.peekFirst(); // this code begs an interesting question -- why is the package digest not put on the stack automatically?
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
