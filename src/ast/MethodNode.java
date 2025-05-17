package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a method definition in COOL.
 */
public class MethodNode extends FeatureNode {
    private List<FormalNode> parameters;
    private ExpressionNode body;
    
    public MethodNode(int lineNumber, String name, String returnType) {
        super(lineNumber, name, returnType);
        this.parameters = new ArrayList<>();
    }
    
    public void addParameter(FormalNode parameter) {
        parameters.add(parameter);
    }
    
    public void setBody(ExpressionNode body) {
        this.body = body;
    }
    
    public List<FormalNode> getParameters() {
        return parameters;
    }
    
    public ExpressionNode getBody() {
        return body;
    }
    
    public String getReturnType() {
        return getType();
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Method " + getName() + "(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(parameters.get(i).toString());
        }
        result.append("): ").append(getType()).append(" {\n");
        
        // Handle null body - add null check
        if (body != null) {
            result.append("  ").append(body.toString().replace("\n", "\n  ")).append("\n");
        } else {
            result.append("  <empty body>\n");
        }
        
        result.append("}");
        return result.toString();
    }
}
