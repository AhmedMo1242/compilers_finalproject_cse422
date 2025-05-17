package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a class definition in COOL.
 */
public class ClassNode extends ASTNode {
    private String name;
    private String parentClass;
    private List<FeatureNode> features;
    
    public ClassNode(int lineNumber, String name, String parentClass) {
        super(lineNumber);
        this.name = name;
        this.parentClass = parentClass;
        this.features = new ArrayList<>();
    }
    
    public void addFeature(FeatureNode feature) {
        features.add(feature);
    }
    
    public String getName() {
        return name;
    }
    
    public String getParentClass() {
        return parentClass;
    }
    
    public String getParent() {
        return parentClass;
    }
    
    public List<FeatureNode> getFeatures() {
        return features;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Class " + name);
        if (parentClass != null) {
            result.append(" inherits ").append(parentClass);
        }
        result.append(" {\n");
        for (FeatureNode feature : features) {
            result.append("  ").append(feature.toString().replace("\n", "\n  ")).append("\n");
        }
        result.append("}");
        return result.toString();
    }
}
