/* Generated By:JJTree: Do not edit this line. ASTDefaultLabel.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTDefaultLabel extends SimpleNode {
  public ASTDefaultLabel(int id) {
    super(id);
  }

  public ASTDefaultLabel(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTDefaultLabel(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTDefaultLabel(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3e4db9f3925b6fe0e54bedd970cb2f24 (do not edit this line) */
