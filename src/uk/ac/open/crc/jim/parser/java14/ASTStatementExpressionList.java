/* Generated By:JJTree: Do not edit this line. ASTStatementExpressionList.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTStatementExpressionList extends SimpleNode {
  public ASTStatementExpressionList(int id) {
    super(id);
  }

  public ASTStatementExpressionList(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTStatementExpressionList(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTStatementExpressionList(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=78b4c7c17df47ca17d556b08d89ce013 (do not edit this line) */