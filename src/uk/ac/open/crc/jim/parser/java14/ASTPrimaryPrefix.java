/* Generated By:JJTree: Do not edit this line. ASTPrimaryPrefix.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTPrimaryPrefix extends SimpleNode {
  public ASTPrimaryPrefix(int id) {
    super(id);
  }

  public ASTPrimaryPrefix(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTPrimaryPrefix(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTPrimaryPrefix(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2d66a44c6ed4b883b505b2065ad4a084 (do not edit this line) */
