/* Generated By:JJTree: Do not edit this line. ASTMemberValuePair.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTMemberValuePair extends SimpleNode {
  public ASTMemberValuePair(int id) {
    super(id);
  }

  public ASTMemberValuePair(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTMemberValuePair(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTMemberValuePair(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=76aa67bc16824648389e518f8ab1176c (do not edit this line) */
