package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing an if-then-else expression.
 */
public class IfNode extends ExpressionNode {
    private ExpressionNode condition;
    private ExpressionNode thenExpr;
    private ExpressionNode elseExpr;
    
    public IfNode(int lineNumber, ExpressionNode condition, 
                  ExpressionNode thenExpr, ExpressionNode elseExpr) {
        super(lineNumber);
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }
    
    public ExpressionNode getCondition() {
        return condition;
    }
    
    public ExpressionNode getThenExpr() {
        return thenExpr;
    }
    
    public ExpressionNode getElseExpr() {
        return elseExpr;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("if ");
        result.append(condition).append(" then\n");
        result.append("  ").append(thenExpr.toString().replace("\n", "\n  ")).append("\n");
        result.append("else\n");
        result.append("  ").append(elseExpr.toString().replace("\n", "\n  ")).append("\n");
        result.append("fi");
        return result.toString();
    }
}
