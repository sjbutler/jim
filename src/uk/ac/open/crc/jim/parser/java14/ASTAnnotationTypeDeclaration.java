/* Generated By:JJTree: Do not edit this line. ASTAnnotationTypeDeclaration.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java14;

public
class ASTAnnotationTypeDeclaration extends SimpleNode {
  public ASTAnnotationTypeDeclaration(int id) {
    super(id);
  }

  public ASTAnnotationTypeDeclaration(Java14Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTAnnotationTypeDeclaration(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new ASTAnnotationTypeDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6cd3fd65b877ff7f3595d876cdbcc6d5 (do not edit this line) */
