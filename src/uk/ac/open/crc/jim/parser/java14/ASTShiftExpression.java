/* Generated By:JJTree: Do not edit this line. ASTShiftExpression.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTShiftExpression extends SimpleNode {
  public ASTShiftExpression(int id) {
    super(id);
  }

  public ASTShiftExpression(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTShiftExpression(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTShiftExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9284ea37e51db395f58498eda8bdad9f (do not edit this line) */