/* Generated By:JJTree: Do not edit this line. ASTBreakStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTBreakStatement extends SimpleNode {
  public ASTBreakStatement(int id) {
    super(id);
  }

  public ASTBreakStatement(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTBreakStatement(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTBreakStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c7f01ac3828cf1842f371400a590b57d (do not edit this line) */