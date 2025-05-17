package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing a variable reference.
 */
public class IdentifierNode extends ExpressionNode {
    private String name;
    
    public IdentifierNode(int lineNumber, String name) {
        super(lineNumber);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
