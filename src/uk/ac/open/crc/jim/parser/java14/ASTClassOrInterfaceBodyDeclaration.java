/* Generated By:JJTree: Do not edit this line. ASTClassOrInterfaceBodyDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTClassOrInterfaceBodyDeclaration extends SimpleNode {
  public ASTClassOrInterfaceBodyDeclaration(int id) {
    super(id);
  }

  public ASTClassOrInterfaceBodyDeclaration(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTClassOrInterfaceBodyDeclaration(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTClassOrInterfaceBodyDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=44012bad29766a3c7d760acd8ff66db0 (do not edit this line) */
