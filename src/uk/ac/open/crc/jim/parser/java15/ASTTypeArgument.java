/* Generated By:JJTree: Do not edit this line. ASTTypeArgument.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTTypeArgument extends SimpleNode {
  public ASTTypeArgument(int id) {
    super(id);
  }

  public ASTTypeArgument(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTTypeArgument(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTTypeArgument(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e091c88a9fb9d020cca948294c7744e8 (do not edit this line) */
