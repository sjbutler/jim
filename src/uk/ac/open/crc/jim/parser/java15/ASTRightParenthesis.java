/* Generated By:JJTree: Do not edit this line. ASTRightParenthesis.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTRightParenthesis extends SimpleNode {
  public ASTRightParenthesis(int id) {
    super(id);
  }

  public ASTRightParenthesis(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTRightParenthesis(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTRightParenthesis(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c2c54dd66fc72d9abc2c31a2b6cfb684 (do not edit this line) */
