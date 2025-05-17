package ast;

/**
 * AST node representing a formal parameter in a method declaration.
 */
public class FormalNode extends ASTNode {
    private String name;
    private String type;
    
    public FormalNode(int lineNumber, String name, String type) {
        super(lineNumber);
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return name + ": " + type;
    }
}
