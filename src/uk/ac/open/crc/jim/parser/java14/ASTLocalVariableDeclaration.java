/* Generated By:JJTree: Do not edit this line. ASTLocalVariableDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTLocalVariableDeclaration extends SimpleNode {
  public ASTLocalVariableDeclaration(int id) {
    super(id);
  }

  public ASTLocalVariableDeclaration(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLocalVariableDeclaration(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTLocalVariableDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=35b7679339ea67a73208f4e281c75619 (do not edit this line) */
