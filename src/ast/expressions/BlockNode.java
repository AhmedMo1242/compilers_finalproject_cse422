package ast.expressions;

import ast.ExpressionNode;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a block of expressions.
 */
public class BlockNode extends ExpressionNode {
    private List<ExpressionNode> expressions;
    
    public BlockNode(int lineNumber) {
        super(lineNumber);
        this.expressions = new ArrayList<>();
    }
    
    public void addExpression(ExpressionNode expr) {
        if (expr != null) {
            expressions.add(expr);
        }
    }
    
    public List<ExpressionNode> getExpressions() {
        return expressions;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{\n");
        for (ExpressionNode expr : expressions) {
            result.append("  ").append(expr.toString().replace("\n", "\n  ")).append(";\n");
        }
        result.append("}");
        return result.toString();
    }
}
