/* Generated By:JJTree: Do not edit this line. ASTEmptyStatement.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTEmptyStatement extends SimpleNode {
  public ASTEmptyStatement(int id) {
    super(id);
  }

  public ASTEmptyStatement(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTEmptyStatement(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTEmptyStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=aa0fe0a58946fcee667a44fb1c08e757 (do not edit this line) */
