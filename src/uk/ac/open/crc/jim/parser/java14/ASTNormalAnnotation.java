/* Generated By:JJTree: Do not edit this line. ASTNormalAnnotation.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTNormalAnnotation extends SimpleNode {
  public ASTNormalAnnotation(int id) {
    super(id);
  }

  public ASTNormalAnnotation(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTNormalAnnotation(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTNormalAnnotation(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ba63110f4fd79a5ee4fd80731bbaf56f (do not edit this line) */