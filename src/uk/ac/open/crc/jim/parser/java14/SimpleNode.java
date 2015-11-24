/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=* */
package uk.ac.open.crc.jim.parser.java14;

public class SimpleNode implements Node {
  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected Java14Parser parser;
  protected String text;
  protected int modifiers;
  protected Token firstToken;
  protected Token lastToken;

  public SimpleNode(int i) {
    id = i;
    modifiers = 0; // needs to be set, just in case it is wroongly invoked
  }

  public SimpleNode(Java14Parser p, int i) {
    this(i);
    parser = p;
  }

  // ------------- introduced methods
  public void    setText( String text ) {
      this.text = text;
  }

  public String getText() {
      return text;
  }

  public void setModifiers(int modifiers) {
      this.modifiers = modifiers;
  }

  public int getModifiers() {
      return modifiers;
  }


  public Token jjtGetFirstToken() { return firstToken; }
  public void jjtSetFirstToken(Token token) { this.firstToken = token; }
  public Token jjtGetLastToken() { return lastToken; }
  public void jjtSetLastToken(Token token) { this.lastToken = token; }

  // ------------- introduced methods

  public static Node jjtCreate(int id) {
    return new SimpleNode(id);
  }

  public static Node jjtCreate(Java14Parser p, int id) {
    return new SimpleNode(p, id);
  }


  @Override
  public int getId() {
      return this.id;
  }
  
  @Override
  public void jjtOpen() {
  }

  @Override
  public void jjtClose() {
  }

  @Override
  public void jjtSetParent(Node n) { parent = n; }
  @Override
  public Node jjtGetParent() { return parent; }

  @Override
  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  @Override
  public Node jjtGetChild(int i) {
    return children[i];
  }

  @Override
  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) { this.value = value; }
  public Object jjtGetValue() { return value; }

  /** Accept the visitor. **/
  @Override
  public Object jjtAccept(Java14ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

  /** Accept the visitor. **/
  public Object childrenAccept(Java14ParserVisitor visitor, Object data) {
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        children[i].jjtAccept(visitor, data);
      }
    }
    return data;
  }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  @Override
  public String toString() { return Java14ParserTreeConstants.jjtNodeName[id]; }
  public String toString(String prefix) { return prefix + toString(); }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
  SimpleNode n = (SimpleNode)children[i];
  if (n != null) {
    n.dump(prefix + " ");
  }
      }
    }
  }
}

/* JavaCC - OriginalChecksum=e341826403a86963b72e623a0a435599 (do not edit this line) */