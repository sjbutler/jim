/* Generated By:JJTree: Do not edit this line. ASTEqualityExpression.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTEqualityExpression extends SimpleNode {
  public ASTEqualityExpression(int id) {
    super(id);
  }

  public ASTEqualityExpression(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTEqualityExpression(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTEqualityExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3302ebd8a23a46d2d2c10be078d67cb1 (do not edit this line) */