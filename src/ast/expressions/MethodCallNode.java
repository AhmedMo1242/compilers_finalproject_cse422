package ast.expressions;

import ast.ExpressionNode;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a method call expression.
 */
public class MethodCallNode extends ExpressionNode {
    private ExpressionNode object;      // The object on which the method is called (null for static dispatch)
    private String methodName;          // Name of the method being called
    private List<ExpressionNode> arguments;  // Arguments passed to the method
    private String staticType;          // Type for static dispatch (@Type), can be null

    /**
     * Constructor for method calls.
     * 
     * @param lineNumber Line number where this method call appears
     * @param object The expression representing the object (can be null for static dispatch)
     * @param methodName Name of the method being called
     * @param staticType Explicit dispatch type (for @Type notation), can be null
     */
    public MethodCallNode(int lineNumber, ExpressionNode object, String methodName, String staticType) {
        super(lineNumber);
        this.object = object;
        this.methodName = methodName;
        this.staticType = staticType;
        this.arguments = new ArrayList<>();
    }
    
    public void addArgument(ExpressionNode arg) {
        arguments.add(arg);
    }
    
    public ExpressionNode getObject() {
        return object;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public List<ExpressionNode> getArguments() {
        return arguments;
    }
    
    public String getStaticType() {
        return staticType;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        // Object part
        if (object != null) {
            result.append(object.toString());
            
            // Static type if present
            if (staticType != null) {
                result.append("@").append(staticType);
            }
            
            result.append(".");
        }
        
        // Method and arguments
        result.append(methodName).append("(");
        
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(arguments.get(i).toString());
        }
        
        result.append(")");
        return result.toString();
    }
}
