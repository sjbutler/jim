/* Generated By:JJTree: Do not edit this line. ASTEnumBody.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTEnumBody extends SimpleNode {
  public ASTEnumBody(int id) {
    super(id);
  }

  public ASTEnumBody(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTEnumBody(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTEnumBody(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c168820c65db0f3aed8f153e799f2389 (do not edit this line) */
