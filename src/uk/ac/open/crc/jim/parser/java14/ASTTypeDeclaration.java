/* Generated By:JJTree: Do not edit this line. ASTTypeDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTTypeDeclaration extends SimpleNode {
  public ASTTypeDeclaration(int id) {
    super(id);
  }

  public ASTTypeDeclaration(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTTypeDeclaration(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTTypeDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=46974eea7eb794cd0c9a94d824401af6 (do not edit this line) */
