/* Generated By:JJTree: Do not edit this line. ASTClassOrInterfaceDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTClassOrInterfaceDeclaration extends SimpleNode {
  public ASTClassOrInterfaceDeclaration(int id) {
    super(id);
  }

  public ASTClassOrInterfaceDeclaration(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTClassOrInterfaceDeclaration(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTClassOrInterfaceDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=78b197b64b5c44ed279161a59ee86d3a (do not edit this line) */
