/* Generated By:JJTree: Do not edit this line. ASTStatementExpressionList.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTStatementExpressionList extends SimpleNode {
  public ASTStatementExpressionList(int id) {
    super(id);
  }

  public ASTStatementExpressionList(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTStatementExpressionList(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTStatementExpressionList(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=cd73c25d67c8b8c2bf95a6e4fcabea0b (do not edit this line) */