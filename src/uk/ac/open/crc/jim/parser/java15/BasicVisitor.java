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

/**
 * Provides a default implementation of each method in the Java15ParserVisitor 
 * interface as a convenience for subclasses.
 *
 *
 * @author Simon Butler <simon@facetus.org.uk>
 * @version $id$
 */
public class BasicVisitor implements Java15ParserVisitor {

    public BasicVisitor() {
        
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    // extract the package name for recording purposes
    public Object visit(ASTPackageDeclaration node, Object data) {
        
        walkChildren(node, data);
        return data;
    }


    @Override
    public Object visit(ASTImportDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTModifiers node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTTypeDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTExtendsList node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTImplementsList node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTEnumDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTEnumBody node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTEnumConstant node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTTypeParameters node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTTypeParameter node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTTypeBound node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTClassOrInterfaceBody node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTClassOrInterfaceBodyDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTVariableDeclarator node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTVariableInitializer node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTArrayInitializer node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }


    @Override
    public Object visit(ASTMethodDeclarator node, Object data) {

        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTFormalParameters node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTFormalParameter node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTConstructorDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTExplicitConstructorInvocation node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTInitializer node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTType node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTReferenceType node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTClassOrInterfaceType node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTTypeArguments node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTTypeArgument node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTWildcardBounds node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTPrimitiveType node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTResultType node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTName node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTNameList node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAssignmentOperator node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTConditionalExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTConditionalOrExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTConditionalAndExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTInclusiveOrExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTExclusiveOrExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAndExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTEqualityExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTInstanceOfExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTRelationalExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTShiftExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAdditiveExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMultiplicativeExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTUnaryExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTPreIncrementExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTPreDecrementExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTUnaryExpressionNotPlusMinus node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTCastLookahead node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTPostfixExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTCastExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMemberSelector node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTPrimaryPrefix node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTPrimarySuffix node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTLiteral node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTStringLiteral node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTCharacterLiteral node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTFloatingPointLiteral node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTNullLiteral node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTArgumentList node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTArrayDimsAndInits node, Object data) {
        walkChildren(node, data);
        return data;
    }


    @Override
    public Object visit(ASTStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAssertStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTLabeledStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTBlock node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTBlockStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTEmptyStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTStatementExpression node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTSwitchLabel node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTWhileStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTDoStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTForStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTForInit node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTStatementExpressionList node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTForUpdate node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTBreakStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTContinueStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTThrowStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTSynchronizedStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTTryStatement node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTCatchClause node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTRUNSIGNEDSHIFT node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTRSIGNEDSHIFT node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAnnotation node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTNormalAnnotation node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMarkerAnnotation node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTSingleMemberAnnotation node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMemberValuePairs node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMemberValuePair node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMemberValue node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTMemberValueArrayInitializer node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAnnotationTypeDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAnnotationTypeBody node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTAnnotationTypeMemberDeclaration node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTDefaultValue node, Object data) {
        walkChildren(node, data);
        return data;
    }

    @Override
    public Object visit(ASTIdentifier node, Object data) {
      walkChildren(node, data);
      return data;
    }

    @Override
    public Object visit(ASTCaseLabel node, Object data) {
      walkChildren(node, data);
      return data;
    }

    @Override
    public Object visit(ASTDefaultLabel node, Object data) {
      walkChildren(node, data);
      return data;
    }


    //---------- extended productions for Halstead --------------

    @Override
    public Object visit(ASTLeftParenthesis node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTRightParenthesis node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTLeftBrace node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTRightBrace node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTLeftAngle node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTRightAngle node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTLeftSquare node, Object data) {
        // always a leaf
        return data;
    }


    @Override
    public Object visit(ASTRightSquare node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTColon node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTSemiColon node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTComma node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTDot node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTAtSign node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTStarWildcard node, Object data) {
        // always a leaf
        return data;
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {
        // always a leaf
        return data;
    }

    //-------------------- METHODS --------------------

    protected void walkChildren(Node node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Node child = node.jjtGetChild(i);
            child.jjtAccept(this, data);
        }
    }



    // now create a signature by concatenating the types
    // of the formal parameters
    protected String getParametersSignature(Node node) {
         StringBuilder signature = new StringBuilder("(");

         for (int i = 0; i < node.jjtGetNumChildren(); i++) {
             Node child = node.jjtGetChild(i);
             if (child instanceof ASTFormalParameters) {
                 for (int j = 0; j < child.jjtGetNumChildren(); j++) {
                     Node grandchild = child.jjtGetChild(j);
                     for (int k = 0; k < grandchild.jjtGetNumChildren(); k++) {
                         Node greatGrandchild = grandchild.jjtGetChild(k);
                         if (greatGrandchild instanceof ASTType) {
                             signature.append(this.getNameFromType((ASTType)greatGrandchild));
                             if ( "...".equals(((ASTType) greatGrandchild).getText()) ) {
                                 signature.append("...");
                             }
                             signature.append(";"); // delimiter to allow parsing later
                         }
                     }
                 }
             }
         }

         signature.append(")");

         return signature.toString();
    }


    private String getNameFromType(ASTType node) {
        String name = "";
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Node child = node.jjtGetChild(i);
            if (child instanceof ASTPrimitiveType) {
                name = ((ASTPrimitiveType) child).getText();
                break;
            }

            if (child instanceof ASTReferenceType) {
                for (int j = 0; j < child.jjtGetNumChildren(); j++) {
                    Node grandchild = child.jjtGetChild(j);
                    if (grandchild instanceof ASTPrimitiveType) {
                        name = ((ASTPrimitiveType) grandchild).getText();
                        break;
                    }

                    if (grandchild instanceof ASTClassOrInterfaceType) {
                        for (int k = 0; k < grandchild.jjtGetNumChildren(); k++) {
                            Node greatGrandchild = grandchild.jjtGetChild(k);
                            if (greatGrandchild instanceof ASTIdentifier) {
                                // don't forget the parameter may have been declared
                                // without an import
                                if (!name.equals("")) {
                                    name += ".";
                                }
                                name += ((ASTIdentifier) greatGrandchild).getText();
                            }
                        }
                    }
                }
            }
        }

        return name;
    }

    
}
