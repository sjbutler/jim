/* Generated By:JJTree: Do not edit this line. ASTNameList.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTNameList extends SimpleNode {
  public ASTNameList(int id) {
    super(id);
  }

  public ASTNameList(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTNameList(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTNameList(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6a8eaf5b8e4270a32bcdd0ff34fec9f7 (do not edit this line) */