/* Generated By:JJTree: Do not edit this line. ASTInclusiveOrExpression.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTInclusiveOrExpression extends SimpleNode {
  public ASTInclusiveOrExpression(int id) {
    super(id);
  }

  public ASTInclusiveOrExpression(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTInclusiveOrExpression(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTInclusiveOrExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5dac92c5c22a4a13c856677df7f104ae (do not edit this line) */