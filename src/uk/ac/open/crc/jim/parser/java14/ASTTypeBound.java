/* Generated By:JJTree: Do not edit this line. ASTTypeBound.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTTypeBound extends SimpleNode {
  public ASTTypeBound(int id) {
    super(id);
  }

  public ASTTypeBound(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTTypeBound(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTTypeBound(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=4dbd0df5a646dc7f6277f06ac4cad7e6 (do not edit this line) */
