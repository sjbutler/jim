/* Generated By:JJTree: Do not edit this line. ASTTypeParameter.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTTypeParameter extends SimpleNode {
  public ASTTypeParameter(int id) {
    super(id);
  }

  public ASTTypeParameter(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTTypeParameter(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTTypeParameter(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=93817b047306b3f0dffaf19ee5c9cf83 (do not edit this line) */
