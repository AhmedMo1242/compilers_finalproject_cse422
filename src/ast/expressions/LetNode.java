package ast.expressions;

import ast.ExpressionNode;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a let expression with local variable declarations.
 */
public class LetNode extends ExpressionNode {
    private List<LetBinding> bindings;
    private ExpressionNode body;
    
    public LetNode(int lineNumber) {
        super(lineNumber);
        this.bindings = new ArrayList<>();
    }
    
    public void addVariable(String name, String type, ExpressionNode init) {
        bindings.add(new LetBinding(name, type, init));
    }
    
    public void setBody(ExpressionNode body) {
        this.body = body;
    }
    
    public List<LetBinding> getBindings() {
        return bindings;
    }
    
    public ExpressionNode getBody() {
        return body;
    }
    
    public int getVarCount() {
        return bindings.size();
    }
    
    public String getVarName(int index) {
        if (index >= 0 && index < bindings.size()) {
            return bindings.get(index).getName();
        }
        return null;
    }
    
    public String getVarType(int index) {
        if (index >= 0 && index < bindings.size()) {
            return bindings.get(index).getType();
        }
        return null;
    }
    
    public ExpressionNode getVarInit(int index) {
        if (index >= 0 && index < bindings.size()) {
            return bindings.get(index).getInit();
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("let ");
        
        for (int i = 0; i < bindings.size(); i++) {
            if (i > 0) {
                result.append(",\n    ");
            }
            result.append(bindings.get(i).toString());
        }
        
        result.append(" in\n");
        result.append("  ").append(body.toString().replace("\n", "\n  "));
        
        return result.toString();
    }
    
    /**
     * Inner class representing a single variable binding in a let expression
     */
    public static class LetBinding {
        private String name;
        private String type;
        private ExpressionNode init;
        
        public LetBinding(String name, String type, ExpressionNode init) {
            this.name = name;
            this.type = type;
            this.init = init;
        }
        
        public String getName() {
            return name;
        }
        
        public String getType() {
            return type;
        }
        
        public ExpressionNode getInit() {
            return init;
        }
        
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(name).append(" : ").append(type);
            if (init != null) {
                result.append(" <- ").append(init.toString());
            }
            return result.toString();
        }
    }
}
