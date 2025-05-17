package ast;

import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a COOL program (the root of the AST).
 */
public class ProgramNode extends ASTNode {
    private List<ClassNode> classes;
    
    public ProgramNode(int lineNumber) {
        super(lineNumber);
        this.classes = new ArrayList<>();
    }
    
    public void addClass(ClassNode classNode) {
        classes.add(classNode);
    }
    
    public List<ClassNode> getClasses() {
        return classes;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Program {\n");
        for (ClassNode classNode : classes) {
            result.append("  ").append(classNode.toString().replace("\n", "\n  ")).append("\n");
        }
        result.append("}");
        return result.toString();
    }
}
