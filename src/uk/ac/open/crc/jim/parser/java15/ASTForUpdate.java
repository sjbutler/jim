/* Generated By:JJTree: Do not edit this line. ASTForUpdate.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTForUpdate extends SimpleNode {
  public ASTForUpdate(int id) {
    super(id);
  }

  public ASTForUpdate(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTForUpdate(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTForUpdate(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5afd1162372d139905036d50311a187c (do not edit this line) */