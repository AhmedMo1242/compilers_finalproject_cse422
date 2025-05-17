package ast.expressions;

import ast.ExpressionNode;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a case expression.
 */
public class CaseNode extends ExpressionNode {
    private ExpressionNode expression;
    private List<CaseBranch> branches;
    
    public CaseNode(int lineNumber, ExpressionNode expression) {
        super(lineNumber);
        this.expression = expression;
        this.branches = new ArrayList<>();
    }
    
    public void addBranch(String variableName, String typeName, ExpressionNode body) {
        branches.add(new CaseBranch(variableName, typeName, body));
    }
    
    public ExpressionNode getExpression() {
        return expression;
    }
    
    public List<CaseBranch> getBranches() {
        return branches;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("case ").append(expression).append(" of\n");
        
        for (CaseBranch branch : branches) {
            result.append("  ").append(branch.toString()).append(";\n");
        }
        
        result.append("esac");
        return result.toString();
    }
    
    /**
     * Inner class representing a single branch in a case expression.
     */
    public static class CaseBranch {
        private String variableName;
        private String typeName;
        private ExpressionNode body;
        
        public CaseBranch(String variableName, String typeName, ExpressionNode body) {
            this.variableName = variableName;
            this.typeName = typeName;
            this.body = body;
        }
        
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(variableName).append(" : ").append(typeName).append(" => ");
            if (body != null) {
                result.append(body.toString().replace("\n", "\n  "));
            } else {
                result.append("<empty>");
            }
            return result.toString();
        }
    }
}
