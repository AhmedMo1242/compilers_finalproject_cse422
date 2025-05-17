package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing a while loop expression.
 */
public class WhileNode extends ExpressionNode {
    private ExpressionNode condition;
    private ExpressionNode body;
    
    public WhileNode(int lineNumber, ExpressionNode condition, ExpressionNode body) {
        super(lineNumber);
        this.condition = condition;
        this.body = body;
    }
    
    public ExpressionNode getCondition() {
        return condition;
    }
    
    public ExpressionNode getBody() {
        return body;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("while ");
        result.append(condition).append(" loop\n");
        result.append("  ").append(body.toString().replace("\n", "\n  ")).append("\n");
        result.append("pool");
        return result.toString();
    }
}
