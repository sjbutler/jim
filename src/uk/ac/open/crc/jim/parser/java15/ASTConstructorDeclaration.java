/* Generated By:JJTree: Do not edit this line. ASTConstructorDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTConstructorDeclaration extends SimpleNode {
  public ASTConstructorDeclaration(int id) {
    super(id);
  }

  public ASTConstructorDeclaration(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTConstructorDeclaration(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTConstructorDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=287b710ce951b47d96ad3f68f65bf83b (do not edit this line) */