/* Generated By:JJTree: Do not edit this line. ASTBlockStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTBlockStatement extends SimpleNode {
  public ASTBlockStatement(int id) {
    super(id);
  }

  public ASTBlockStatement(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTBlockStatement(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTBlockStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5d2b05e8fa4a5d355255691cdf274028 (do not edit this line) */
