/* Generated By:JJTree: Do not edit this line. ASTLeftBrace.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTLeftBrace extends SimpleNode {
  public ASTLeftBrace(int id) {
    super(id);
  }

  public ASTLeftBrace(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLeftBrace(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTLeftBrace(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=193c6f1f498f70f6e3bdc51851d5f55b (do not edit this line) */