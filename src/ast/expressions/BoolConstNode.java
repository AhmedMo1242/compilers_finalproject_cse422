package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing a boolean constant.
 */
public class BoolConstNode extends ExpressionNode {
    private boolean value;
    
    public BoolConstNode(int lineNumber, boolean value) {
        super(lineNumber);
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value ? "true" : "false";
    }
}
