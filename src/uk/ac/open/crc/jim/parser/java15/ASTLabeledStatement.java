/* Generated By:JJTree: Do not edit this line. ASTLabeledStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTLabeledStatement extends SimpleNode {
  public ASTLabeledStatement(int id) {
    super(id);
  }

  public ASTLabeledStatement(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLabeledStatement(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTLabeledStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=24b56a5c0426bf4d6fc96b5f8271a085 (do not edit this line) */