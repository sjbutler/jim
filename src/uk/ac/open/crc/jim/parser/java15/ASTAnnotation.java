/* Generated By:JJTree: Do not edit this line. ASTAnnotation.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTAnnotation extends SimpleNode {
  public ASTAnnotation(int id) {
    super(id);
  }

  public ASTAnnotation(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTAnnotation(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTAnnotation(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5d49c5edc104c1b81d835d4edc4af773 (do not edit this line) */
