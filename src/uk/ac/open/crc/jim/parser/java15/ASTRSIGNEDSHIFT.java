/* Generated By:JJTree: Do not edit this line. ASTRSIGNEDSHIFT.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTRSIGNEDSHIFT extends SimpleNode {
  public ASTRSIGNEDSHIFT(int id) {
    super(id);
  }

  public ASTRSIGNEDSHIFT(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTRSIGNEDSHIFT(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTRSIGNEDSHIFT(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=30be30217c73304f35b46357b6939e76 (do not edit this line) */
