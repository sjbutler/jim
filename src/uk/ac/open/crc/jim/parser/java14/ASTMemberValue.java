/* Generated By:JJTree: Do not edit this line. ASTMemberValue.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTMemberValue extends SimpleNode {
  public ASTMemberValue(int id) {
    super(id);
  }

  public ASTMemberValue(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTMemberValue(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTMemberValue(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7a0fc6443a4440c08e135f4c12996df1 (do not edit this line) */
