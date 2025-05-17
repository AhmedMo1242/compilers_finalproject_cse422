package ast;

/**
 * AST node representing an attribute (class variable) definition in COOL.
 */
public class AttributeNode extends FeatureNode {
    private ExpressionNode initialization;
    
    public AttributeNode(int lineNumber, String name, String type) {
        super(lineNumber, name, type);
    }
    
    public void setInitialization(ExpressionNode initialization) {
        this.initialization = initialization;
    }
    
    public ExpressionNode getInitialization() {
        return initialization;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Attribute " + getName() + ": " + getType());
        if (initialization != null) {
            result.append(" <- ").append(initialization.toString());
        }
        return result.toString();
    }
}
