package ast;

/**
 * Abstract base class for feature nodes (methods and attributes).
 */
public abstract class FeatureNode extends ASTNode {
    private String name;
    private String type;
    
    public FeatureNode(int lineNumber, String name, String type) {
        super(lineNumber);
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
}
