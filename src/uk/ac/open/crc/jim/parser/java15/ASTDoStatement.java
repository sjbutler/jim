/* Generated By:JJTree: Do not edit this line. ASTDoStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTDoStatement extends SimpleNode {
  public ASTDoStatement(int id) {
    super(id);
  }

  public ASTDoStatement(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTDoStatement(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTDoStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=caaf6990127bd392cb9ef947b022ffba (do not edit this line) */
