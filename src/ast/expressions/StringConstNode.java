package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing a string constant.
 */
public class StringConstNode extends ExpressionNode {
    private String value;
    
    public StringConstNode(int lineNumber, String value) {
        super(lineNumber);
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "\"" + value.replace("\"", "\\\"") + "\"";
    }
}
