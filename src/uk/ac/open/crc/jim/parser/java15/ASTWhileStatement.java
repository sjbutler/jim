/* Generated By:JJTree: Do not edit this line. ASTWhileStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTWhileStatement extends SimpleNode {
  public ASTWhileStatement(int id) {
    super(id);
  }

  public ASTWhileStatement(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTWhileStatement(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTWhileStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=84c48882180619992202c80c3edbf7a2 (do not edit this line) */