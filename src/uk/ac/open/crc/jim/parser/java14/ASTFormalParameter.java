/* Generated By:JJTree: Do not edit this line. ASTFormalParameter.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTFormalParameter extends SimpleNode {
  public ASTFormalParameter(int id) {
    super(id);
  }

  public ASTFormalParameter(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTFormalParameter(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTFormalParameter(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3d840eb2e007023e712bd134c3371379 (do not edit this line) */
