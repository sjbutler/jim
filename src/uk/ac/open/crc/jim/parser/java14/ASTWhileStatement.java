/* Generated By:JJTree: Do not edit this line. ASTWhileStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTWhileStatement extends SimpleNode {
  public ASTWhileStatement(int id) {
    super(id);
  }

  public ASTWhileStatement(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTWhileStatement(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTWhileStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b283fb02eb44c32b99d336da92d192e8 (do not edit this line) */