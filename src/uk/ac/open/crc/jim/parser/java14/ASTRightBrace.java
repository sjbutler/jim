/* Generated By:JJTree: Do not edit this line. ASTRightBrace.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTRightBrace extends SimpleNode {
  public ASTRightBrace(int id) {
    super(id);
  }

  public ASTRightBrace(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTRightBrace(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTRightBrace(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=cbfc7ebb9f4d4e506fcfec3bab593db8 (do not edit this line) */
