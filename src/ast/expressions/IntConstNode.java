package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing an integer constant.
 */
public class IntConstNode extends ExpressionNode {
    private int value;
    
    public IntConstNode(int lineNumber, int value) {
        super(lineNumber);
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
