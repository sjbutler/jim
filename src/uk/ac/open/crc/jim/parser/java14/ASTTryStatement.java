/* Generated By:JJTree: Do not edit this line. ASTTryStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTTryStatement extends SimpleNode {
  public ASTTryStatement(int id) {
    super(id);
  }

  public ASTTryStatement(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTTryStatement(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTTryStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=cffc77ea5b52929cc30c4b486ba29699 (do not edit this line) */
