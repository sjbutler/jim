/* Generated By:JJTree: Do not edit this line. ASTMethodDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTMethodDeclaration extends SimpleNode {
  public ASTMethodDeclaration(int id) {
    super(id);
  }

  public ASTMethodDeclaration(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTMethodDeclaration(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTMethodDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8d1e47806fe3e3eaeb4486f8c6780a6a (do not edit this line) */
