/* Generated By:JJTree: Do not edit this line. ASTForInit.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTForInit extends SimpleNode {
  public ASTForInit(int id) {
    super(id);
  }

  public ASTForInit(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTForInit(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTForInit(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=cc8024943843300c6341f5251d71c91d (do not edit this line) */
