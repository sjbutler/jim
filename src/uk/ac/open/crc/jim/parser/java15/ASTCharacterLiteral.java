/* Generated By:JJTree: Do not edit this line. ASTCharacterLiteral.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTCharacterLiteral extends SimpleNode {
  public ASTCharacterLiteral(int id) {
    super(id);
  }

  public ASTCharacterLiteral(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTCharacterLiteral(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTCharacterLiteral(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3cdffbdfb3294f7b953439fe2d86af7d (do not edit this line) */