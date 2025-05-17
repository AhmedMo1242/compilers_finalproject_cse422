package ast.expressions;

import ast.ExpressionNode;

/**
 * AST node representing a binary operation expression (e.g., +, -, *, /, <, <=, =).
 */
public class BinaryOpNode extends ExpressionNode {
    public enum Operator {
        PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"), 
        LESS_THAN("<"), LESS_EQUAL("<="), EQUAL("="),
        GREATER_THAN(">"), GREATER_EQUAL(">=");
        
        private final String symbol;
        
        Operator(String symbol) {
            this.symbol = symbol;
        }
        
        @Override
        public String toString() {
            return symbol;
        }
    }
    
    private Operator operator;
    private ExpressionNode leftOperand;
    private ExpressionNode rightOperand;
    
    public BinaryOpNode(int lineNumber, Operator operator, 
                        ExpressionNode leftOperand, ExpressionNode rightOperand) {
        super(lineNumber);
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
    
    public Operator getOperator() {
        return operator;
    }
    
    public ExpressionNode getLeftOperand() {
        return leftOperand;
    }
    
    public ExpressionNode getRightOperand() {
        return rightOperand;
    }
    
    @Override
    public String toString() {
        return "(" + leftOperand + " " + operator + " " + rightOperand + ")";
    }
}
