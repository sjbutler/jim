/* Generated By:JJTree: Do not edit this line. ASTConditionalExpression.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTConditionalExpression extends SimpleNode {
  public ASTConditionalExpression(int id) {
    super(id);
  }

  public ASTConditionalExpression(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTConditionalExpression(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTConditionalExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=499a5d440ceb46e7c9773545ac3fa7d5 (do not edit this line) */
