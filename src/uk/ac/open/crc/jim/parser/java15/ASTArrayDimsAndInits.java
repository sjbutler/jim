/* Generated By:JJTree: Do not edit this line. ASTArrayDimsAndInits.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTArrayDimsAndInits extends SimpleNode {
  public ASTArrayDimsAndInits(int id) {
    super(id);
  }

  public ASTArrayDimsAndInits(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTArrayDimsAndInits(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTArrayDimsAndInits(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=93bf5ad3ed563909b8486973a1d91210 (do not edit this line) */
