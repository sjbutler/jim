/* Generated By:JJTree: Do not edit this line. ASTCompilationUnit.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.open.crc.jim.parser.java15;

public
class ASTCompilationUnit extends SimpleNode {
  public ASTCompilationUnit(int id) {
    super(id);
  }

  public ASTCompilationUnit(Java15Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTCompilationUnit(id);
  }

  public static Node jjtCreate(Java15Parser p, int id) {
    return new ASTCompilationUnit(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(Java15ParserVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=eb309670085326d40d19f572407e1fd7 (do not edit this line) */
