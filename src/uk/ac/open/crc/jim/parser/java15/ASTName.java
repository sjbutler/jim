/* Generated By:JJTree: Do not edit this line. ASTName.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTName extends SimpleNode {
  public ASTName(int id) {
    super(id);
  }

  public ASTName(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTName(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTName(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=76dbb238890ab8ca56441dc0ff389c8e (do not edit this line) */
