/* Generated By:JJTree: Do not edit this line. ASTComma.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTComma extends SimpleNode {
  public ASTComma(int id) {
    super(id);
  }

  public ASTComma(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTComma(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTComma(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e5938790544f0636b3af10634b77a2db (do not edit this line) */
