package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing an assignment expression.
 */
public class AssignNode extends ExpressionNode {
    private String variableName;       // Name of the variable being assigned to
    private ExpressionNode expression; // Value being assigned
    
    /**
     * Creates a new assignment expression node.
     * 
     * @param lineNumber Line number where this assignment appears
     * @param variableName Name of the variable being assigned
     * @param expression The expression whose value is assigned to the variable
     */
    public AssignNode(int lineNumber, String variableName, ExpressionNode expression) {
        super(lineNumber);
        this.variableName = variableName;
        this.expression = expression;
    }
    
    public String getVariableName() {
        return variableName;
    }
    
    public ExpressionNode getExpression() {
        return expression;
    }
    
    @Override
    public String toString() {
        return variableName + " <- " + expression.toString();
    }
}
