/* Generated By:JJTree: Do not edit this line. ASTBooleanLiteral.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTBooleanLiteral extends SimpleNode {
  public ASTBooleanLiteral(int id) {
    super(id);
  }

  public ASTBooleanLiteral(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTBooleanLiteral(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTBooleanLiteral(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=30dc911e222993678f2c490fbbab4f08 (do not edit this line) */