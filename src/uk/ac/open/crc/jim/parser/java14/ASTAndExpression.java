/* Generated By:JJTree: Do not edit this line. ASTAndExpression.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTAndExpression extends SimpleNode {
  public ASTAndExpression(int id) {
    super(id);
  }

  public ASTAndExpression(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTAndExpression(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTAndExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2fb763ef895f2d4f78e116ed2f135f45 (do not edit this line) */
