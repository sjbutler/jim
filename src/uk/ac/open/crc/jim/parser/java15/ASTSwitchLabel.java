/* Generated By:JJTree: Do not edit this line. ASTSwitchLabel.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTSwitchLabel extends SimpleNode {
  public ASTSwitchLabel(int id) {
    super(id);
  }

  public ASTSwitchLabel(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTSwitchLabel(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTSwitchLabel(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=312f38d31a9dd4531c166db2ddeee7ab (do not edit this line) */
