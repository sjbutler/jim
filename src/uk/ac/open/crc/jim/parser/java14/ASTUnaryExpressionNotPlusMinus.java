/* Generated By:JJTree: Do not edit this line. ASTUnaryExpressionNotPlusMinus.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTUnaryExpressionNotPlusMinus extends SimpleNode {
  public ASTUnaryExpressionNotPlusMinus(int id) {
    super(id);
  }

  public ASTUnaryExpressionNotPlusMinus(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTUnaryExpressionNotPlusMinus(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTUnaryExpressionNotPlusMinus(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0a94cb0e838508aac2824025f5da6c0d (do not edit this line) */