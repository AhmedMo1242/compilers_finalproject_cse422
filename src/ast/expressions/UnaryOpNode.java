package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing a unary operation (e.g., ~, not, isvoid).
 */
public class UnaryOpNode extends ExpressionNode {
    public enum Op {
        NOT("not"), NEGATE("~"), ISVOID("isvoid");
        
        private String symbol;
        
        Op(String symbol) {
            this.symbol = symbol;
        }
        
        @Override
        public String toString() {
            return symbol;
        }
    }
    
    private Op operator;
    private ExpressionNode operand;
    
    public UnaryOpNode(int lineNumber, Op operator, ExpressionNode operand) {
        super(lineNumber);
        this.operator = operator;
        this.operand = operand;
    }
    
    public Op getOperator() {
        return operator;
    }
    
    public ExpressionNode getOperand() {
        return operand;
    }
    
    @Override
    public String toString() {
        return operator + "(" + operand + ")";
    }
}
