/* Generated By:JJTree: Do not edit this line. ASTConstructorDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTConstructorDeclaration extends SimpleNode {
  public ASTConstructorDeclaration(int id) {
    super(id);
  }

  public ASTConstructorDeclaration(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTConstructorDeclaration(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTConstructorDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=a896737b271bd00b532461e9982a3e71 (do not edit this line) */