/* Generated By:JJTree: Do not edit this line. ASTImplementsList.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTImplementsList extends SimpleNode {
  public ASTImplementsList(int id) {
    super(id);
  }

  public ASTImplementsList(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTImplementsList(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTImplementsList(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=69f61aee83a9141437147fb6bd74217f (do not edit this line) */
