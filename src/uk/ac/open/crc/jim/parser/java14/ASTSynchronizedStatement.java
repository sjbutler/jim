/* Generated By:JJTree: Do not edit this line. ASTSynchronizedStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTSynchronizedStatement extends SimpleNode {
  public ASTSynchronizedStatement(int id) {
    super(id);
  }

  public ASTSynchronizedStatement(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTSynchronizedStatement(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTSynchronizedStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9fc0d5aebc9da20743ca092ef375b8b6 (do not edit this line) */
