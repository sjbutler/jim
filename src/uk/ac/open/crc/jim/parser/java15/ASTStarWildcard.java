/* Generated By:JJTree: Do not edit this line. ASTStarWildcard.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTStarWildcard extends SimpleNode {
  public ASTStarWildcard(int id) {
    super(id);
  }

  public ASTStarWildcard(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTStarWildcard(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTStarWildcard(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ea764a3b634853728b0304300f2464ca (do not edit this line) */
