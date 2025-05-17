package ast;

/**
 * Base class for all Abstract Syntax Tree nodes.
 */
public abstract class ASTNode {
    private int lineNumber;
    
    public ASTNode(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    /**
     * Returns a string representation of the AST node for debugging and visualization.
     */
    @Override
    public abstract String toString();
}
