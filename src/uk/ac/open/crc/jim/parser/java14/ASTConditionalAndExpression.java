/* Generated By:JJTree: Do not edit this line. ASTConditionalAndExpression.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTConditionalAndExpression extends SimpleNode {
  public ASTConditionalAndExpression(int id) {
    super(id);
  }

  public ASTConditionalAndExpression(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTConditionalAndExpression(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTConditionalAndExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=90e7f7f56abdd8eb90f3ff1d7005e010 (do not edit this line) */
