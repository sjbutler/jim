/* Generated By:JJTree: Do not edit this line. ASTStringLiteral.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTStringLiteral extends SimpleNode {
  public ASTStringLiteral(int id) {
    super(id);
  }

  public ASTStringLiteral(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTStringLiteral(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTStringLiteral(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=4f8e7de4b65fc2b24ec9a1bbd5b97c22 (do not edit this line) */
