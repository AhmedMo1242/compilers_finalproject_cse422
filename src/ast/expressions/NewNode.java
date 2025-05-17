package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing a new object creation expression.
 */
public class NewNode extends ExpressionNode {
    private String typeName;
    
    public NewNode(int lineNumber, String typeName) {
        super(lineNumber);
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    @Override
    public String toString() {
        return "new " + typeName;
    }
}
