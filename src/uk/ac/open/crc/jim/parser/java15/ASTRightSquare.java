/* Generated By:JJTree: Do not edit this line. ASTRightSquare.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTRightSquare extends SimpleNode {
  public ASTRightSquare(int id) {
    super(id);
  }

  public ASTRightSquare(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTRightSquare(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTRightSquare(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2c78bc54fdfc6910d8568e2d457805fc (do not edit this line) */
