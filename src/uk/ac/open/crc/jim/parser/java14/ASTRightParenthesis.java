/* Generated By:JJTree: Do not edit this line. ASTRightParenthesis.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTRightParenthesis extends SimpleNode {
  public ASTRightParenthesis(int id) {
    super(id);
  }

  public ASTRightParenthesis(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTRightParenthesis(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTRightParenthesis(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d31fc0cd5abc7d3ee1b794c50c90a9f6 (do not edit this line) */
