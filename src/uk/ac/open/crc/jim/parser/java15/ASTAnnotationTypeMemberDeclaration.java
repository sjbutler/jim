/* Generated By:JJTree: Do not edit this line. ASTAnnotationTypeMemberDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTAnnotationTypeMemberDeclaration extends SimpleNode {
  public ASTAnnotationTypeMemberDeclaration(int id) {
    super(id);
  }

  public ASTAnnotationTypeMemberDeclaration(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTAnnotationTypeMemberDeclaration(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTAnnotationTypeMemberDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e56868dbfc7904370df869777be35623 (do not edit this line) */