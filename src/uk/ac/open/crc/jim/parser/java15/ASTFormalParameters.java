/* Generated By:JJTree: Do not edit this line. ASTFormalParameters.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTFormalParameters extends SimpleNode {
  public ASTFormalParameters(int id) {
    super(id);
  }

  public ASTFormalParameters(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTFormalParameters(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTFormalParameters(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7b3b856611f3bba24406ddf1e3e5e886 (do not edit this line) */
