/* Generated By:JJTree: Do not edit this line. ASTCaseLabel.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTCaseLabel extends SimpleNode {
  public ASTCaseLabel(int id) {
    super(id);
  }

  public ASTCaseLabel(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTCaseLabel(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTCaseLabel(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=cdc0288635e76761666e96c765d6c525 (do not edit this line) */
