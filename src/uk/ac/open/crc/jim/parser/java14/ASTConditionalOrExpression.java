/* Generated By:JJTree: Do not edit this line. ASTConditionalOrExpression.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTConditionalOrExpression extends SimpleNode {
  public ASTConditionalOrExpression(int id) {
    super(id);
  }

  public ASTConditionalOrExpression(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTConditionalOrExpression(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTConditionalOrExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8a85a426d11682189ff4bf4da7d3fc7d (do not edit this line) */
