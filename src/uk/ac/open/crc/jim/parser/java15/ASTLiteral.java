/* Generated By:JJTree: Do not edit this line. ASTLiteral.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTLiteral extends SimpleNode {
  public ASTLiteral(int id) {
    super(id);
  }

  public ASTLiteral(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLiteral(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTLiteral(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=524bc602e3cf3d79a531aa453188ee43 (do not edit this line) */