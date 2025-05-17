package ir;

import ast.*;
import ast.expressions.*;
import semantic.SemanticAnalyzer;

import java.util.*;

/**
 * IR Generator for COOL language
 * Generates Three Address Code (TAC) from the AST
 */
public class IRGenerator {
    private List<TACInstruction> instructions;
    private IRContext context;
    private Map<String, List<TACInstruction>> methods;
    private String currentClass;
    
    public IRGenerator() {
        this.instructions = new ArrayList<>();
        this.context = new IRContext();
        this.methods = new HashMap<>();
    }

    /**
     * Generate IR for a program
     * @param program The AST root node
     * @return The generated IR
     */
    public List<TACInstruction> generate(ProgramNode program) {
        // Add program header
        instructions.add(TACInstruction.createComment("COOL Program IR"));
        
        // Process each class
        for (ClassNode classNode : program.getClasses()) {
            generateClass(classNode);
        }
        
        // Generate main program entry
        generateMainEntry();
        
        return getAllInstructions();
    }
    
    /**
     * Generate code for a class
     */
    private void generateClass(ClassNode classNode) {
        currentClass = classNode.getName();
        
        // Add class header
        instructions.add(TACInstruction.createComment("Class: " + currentClass));
        
        // Process attributes
        for (FeatureNode feature : classNode.getFeatures()) {
            if (feature instanceof AttributeNode) {
                generateAttribute((AttributeNode) feature);
            }
        }
        
        // Process methods
        for (FeatureNode feature : classNode.getFeatures()) {
            if (feature instanceof MethodNode) {
                generateMethod((MethodNode) feature);
            }
        }
    }
    
    /**
     * Generate code for a class attribute
     */
    private void generateAttribute(AttributeNode attr) {
        String attrName = currentClass + "." + attr.getName();
        
        // Handle initialization if present
        if (attr.getInitialization() != null) {
            List<TACInstruction> attrInitInstructions = new ArrayList<>();
            
            // Generate initialization expression
            String exprResult = generateExpression(attr.getInitialization(), attrInitInstructions);
            
            // Add assignment instruction
            attrInitInstructions.add(new TACInstruction(attrName, exprResult));
            
            // Store this in class initializer code
            String initMethod = currentClass + "_init";
            methods.computeIfAbsent(initMethod, k -> new ArrayList<>()).addAll(attrInitInstructions);
        }
    }
    
    /**
     * Generate code for a method
     */
    private void generateMethod(MethodNode method) {
        String methodName = currentClass + "." + method.getName();
        List<TACInstruction> methodInstructions = new ArrayList<>();
        
        // Add method header comment
        methodInstructions.add(TACInstruction.createComment("Method: " + methodName));
        
        // Handle parameters - in COOL, the receiver (self) is implicit first parameter
        methodInstructions.add(TACInstruction.createComment("Parameters:"));
        for (FormalNode param : method.getParameters()) {
            methodInstructions.add(TACInstruction.createComment("  " + param.getName() + " : " + param.getType()));
        }
        
        // Generate code for method body
        String resultTemp = generateExpression(method.getBody(), methodInstructions);
        
        // Add return value assignment
        methodInstructions.add(new TACInstruction("return", resultTemp));
        
        // Store method instructions
        methods.put(methodName, methodInstructions);
    }
    
    /**
     * Generate code for an expression and return the temp/var holding the result
     */
    private String generateExpression(ExpressionNode expr, List<TACInstruction> instrs) {
        if (expr instanceof IntConstNode) {
            // For integer constants, just return the value as string
            return String.valueOf(((IntConstNode) expr).getValue());
        }
        else if (expr instanceof StringConstNode) {
            // For string constants, return quoted string
            return "\"" + ((StringConstNode) expr).getValue() + "\"";
        }
        else if (expr instanceof BoolConstNode) {
            // For boolean constants, return "true" or "false"
            return ((BoolConstNode) expr).getValue() ? "true" : "false";
        }
        else if (expr instanceof IdentifierNode) {
            // For identifiers, just return the name
            return ((IdentifierNode) expr).getName();
        }
        else if (expr instanceof AssignNode) {
            return generateAssign((AssignNode) expr, instrs);
        }
        else if (expr instanceof BlockNode) {
            return generateBlock((BlockNode) expr, instrs);
        }
        else if (expr instanceof IfNode) {
            return generateIf((IfNode) expr, instrs);
        }
        else if (expr instanceof WhileNode) {
            return generateWhile((WhileNode) expr, instrs);
        }
        else if (expr instanceof BinaryOpNode) {
            return generateBinaryOp((BinaryOpNode) expr, instrs);
        }
        else if (expr instanceof MethodCallNode) {
            return generateMethodCall((MethodCallNode) expr, instrs);
        }
        else if (expr instanceof NewNode) {
            return generateNew((NewNode) expr, instrs);
        }
        else {
            // For unsupported expressions, add a comment and return dummy value
            instrs.add(TACInstruction.createComment("Unsupported expression type: " + 
                                                  expr.getClass().getSimpleName()));
            return "error";
        }
    }
    
    /**
     * Generate code for assignment
     */
    private String generateAssign(AssignNode node, List<TACInstruction> instrs) {
        String exprResult = generateExpression(node.getExpression(), instrs);
        String varName = node.getVariableName();
        
        instrs.add(new TACInstruction(varName, exprResult));
        
        return exprResult; // Assignment returns the assigned value
    }
    
    /**
     * Generate code for a block
     */
    private String generateBlock(BlockNode node, List<TACInstruction> instrs) {
        String result = "void"; // Default return value
        
        for (ExpressionNode expr : node.getExpressions()) {
            result = generateExpression(expr, instrs);
        }
        
        return result; // Block returns the value of the last expression
    }
    
    /**
     * Generate code for if-then-else
     */
    private String generateIf(IfNode node, List<TACInstruction> instrs) {
        String condResult = generateExpression(node.getCondition(), instrs);
        String resultTemp = context.newTemp();
        
        String elseLabel = context.newLabel();
        String endLabel = context.newLabel();
        
        // Jump to else if condition is false
        instrs.add(new TACInstruction(condResult, elseLabel, false));
        
        // Then branch
        String thenResult = generateExpression(node.getThenBranch(), instrs);
        instrs.add(new TACInstruction(resultTemp, thenResult));
        instrs.add(new TACInstruction(endLabel));
        
        // Else branch
        instrs.add(TACInstruction.createLabel(elseLabel));
        String elseResult = generateExpression(node.getElseBranch(), instrs);
        instrs.add(new TACInstruction(resultTemp, elseResult));
        
        // End of if
        instrs.add(TACInstruction.createLabel(endLabel));
        
        return resultTemp;
    }
    
    /**
     * Generate code for while loop
     */
    private String generateWhile(WhileNode node, List<TACInstruction> instrs) {
        String condLabel = context.newLabel();
        String endLabel = context.newLabel();
        
        // Loop condition
        instrs.add(TACInstruction.createLabel(condLabel));
        String condResult = generateExpression(node.getCondition(), instrs);
        
        // Exit loop if condition is false
        instrs.add(new TACInstruction(condResult, endLabel, false));
        
        // Loop body
        generateExpression(node.getBody(), instrs);
        
        // Jump back to condition
        instrs.add(new TACInstruction(condLabel));
        
        // End of loop
        instrs.add(TACInstruction.createLabel(endLabel));
        
        return "void";
    }
    
    /**
     * Generate code for binary operation
     */
    private String generateBinaryOp(BinaryOpNode node, List<TACInstruction> instrs) {
        String leftResult = generateExpression(node.getLeftOperand(), instrs);
        String rightResult = generateExpression(node.getRightOperand(), instrs);
        String resultTemp = context.newTemp();
        
        String op;
        switch (node.getOperator()) {
            case PLUS: op = "+"; break;
            case MINUS: op = "-"; break;
            case MULTIPLY: op = "*"; break;
            case DIVIDE: op = "/"; break;
            case LESS_THAN: op = "<"; break;
            case LESS_EQUAL: op = "<="; break;
            case EQUAL: op = "=="; break;
            default: op = "?"; break;
        }
        
        instrs.add(new TACInstruction(resultTemp, leftResult, op, rightResult));
        
        return resultTemp;
    }
    
    /**
     * Generate code for method call
     */
    private String generateMethodCall(MethodCallNode node, List<TACInstruction> instrs) {
        String resultTemp = context.newTemp();
        
        // Prepare argument list
        List<String> args = new ArrayList<>();
        
        // Get receiver object (explicit or implicit 'self')
        if (node.getObject() != null) {
            args.add(generateExpression(node.getObject(), instrs));
        } else {
            args.add("self");
        }
        
        // Add regular arguments
        for (ExpressionNode arg : node.getArguments()) {
            args.add(generateExpression(arg, instrs));
        }
        
        // Determine method name (with static dispatch handling)
        String methodName;
        if (node.getStaticType() != null) {
            methodName = node.getStaticType() + "." + node.getMethodName();
        } else {
            // In a real compiler, we'd handle dynamic dispatch here
            methodName = "dispatch." + node.getMethodName();
        }
        
        // Create call instruction
        instrs.add(TACInstruction.createCall(resultTemp, methodName, args));
        
        return resultTemp;
    }
    
    /**
     * Generate code for object creation
     */
    private String generateNew(NewNode node, List<TACInstruction> instrs) {
        String resultTemp = context.newTemp();
        
        // Allocate memory for the object
        instrs.add(TACInstruction.createComment("Allocate " + node.getTypeName()));
        instrs.add(TACInstruction.createCall(resultTemp, "new_" + node.getTypeName(), Collections.emptyList()));
        
        // Initialize the object
        List<String> args = new ArrayList<>();
        args.add(resultTemp);
        instrs.add(TACInstruction.createCall(null, node.getTypeName() + "_init", args));
        
        return resultTemp;
    }
    
    /**
     * Generate main program entry point
     */
    private void generateMainEntry() {
        instructions.add(TACInstruction.createComment("Program entry point"));
        instructions.add(TACInstruction.createLabel("main"));
        
        // Create Main object
        String mainObj = context.newTemp();
        instructions.add(TACInstruction.createCall(mainObj, "new_Main", Collections.emptyList()));
        
        // Initialize Main object
        List<String> initArgs = new ArrayList<>();
        initArgs.add(mainObj);
        instructions.add(TACInstruction.createCall(null, "Main_init", initArgs));
        
        // Call main method
        List<String> mainArgs = new ArrayList<>();
        mainArgs.add(mainObj);
        String result = context.newTemp();
        instructions.add(TACInstruction.createCall(result, "Main.main", mainArgs));
    }
    
    /**
     * Get all instructions combined (methods and main program)
     */
    private List<TACInstruction> getAllInstructions() {
        List<TACInstruction> allInstructions = new ArrayList<>();
        
        // Add each method's instructions
        for (Map.Entry<String, List<TACInstruction>> entry : methods.entrySet()) {
            String methodName = entry.getKey();
            List<TACInstruction> methodInstructions = entry.getValue();
            
            allInstructions.add(TACInstruction.createLabel(methodName));
            allInstructions.addAll(methodInstructions);
            allInstructions.add(TACInstruction.createComment("End of " + methodName));
        }
        
        // Add main program instructions
        allInstructions.addAll(instructions);
        
        return allInstructions;
    }
}
