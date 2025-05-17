package ast;

/**
 * Abstract base class for all expression nodes in the AST.
 */
public abstract class ExpressionNode extends ASTNode {
    public ExpressionNode(int lineNumber) {
        super(lineNumber);
    }
}
