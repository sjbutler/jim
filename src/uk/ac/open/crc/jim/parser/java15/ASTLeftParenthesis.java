/* Generated By:JJTree: Do not edit this line. ASTLeftParenthesis.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTLeftParenthesis extends SimpleNode {
  public ASTLeftParenthesis(int id) {
    super(id);
  }

  public ASTLeftParenthesis(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLeftParenthesis(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTLeftParenthesis(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3a276369f7fa78a67773180e5b6f60dd (do not edit this line) */
