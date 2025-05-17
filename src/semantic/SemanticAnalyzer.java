package semantic;

import ast.*;
import ast.expressions.*;

import java.util.*;

/**
 * Semantic analyzer for COOL language.
 * Responsible for type checking, scope resolution, and other semantic validations.
 */
public class SemanticAnalyzer {
    private SymbolTable symbolTable;
    private List<SemanticError> errors;
    
    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
        this.errors = new ArrayList<>();
    }
    
    /**
     * Analyze the program for semantic errors.
     * @param program The AST root node
     * @return List of semantic errors found
     */
    public List<SemanticError> analyze(ProgramNode program) {
        // First pass: collect all class definitions
        for (ClassNode classNode : program.getClasses()) {
            defineClass(classNode);
        }
        
        // Check for cycles in the inheritance graph
        checkInheritanceCycles();
        
        if (!errors.isEmpty()) {
            return errors;
        }
        
        // Second pass: analyze class features (methods and attributes)
        for (ClassNode classNode : program.getClasses()) {
            analyzeClassFeatures(classNode);
        }
        
        if (!errors.isEmpty()) {
            return errors;
        }
        
        // Third pass: type check method bodies and expressions
        for (ClassNode classNode : program.getClasses()) {
            checkClassImplementation(classNode);
        }
        
        // Check for Main class and main method
        checkMainClassExists();
        
        return errors;
    }
    
    /**
     * First pass: Define all classes in the symbol table
     */
    private void defineClass(ClassNode classNode) {
        String className = classNode.getName();
        String parentName = classNode.getParent();
        
        // Check if the class is already defined
        if (symbolTable.classExists(className)) {
            addError(classNode.getLineNumber(), "Class " + className + " is redefined");
            return;
        }
        
        // Create class in symbol table
        symbolTable.addClass(className, parentName);
    }
    
    /**
     * Check for cycles in the inheritance graph
     */
    private void checkInheritanceCycles() {
        Set<String> classes = symbolTable.getAllClassNames();
        
        for (String className : classes) {
            // Skip basic classes
            if (className.equals("Object") || className.equals("IO") || 
                className.equals("Int") || className.equals("String") || 
                className.equals("Bool")) {
                continue;
            }
            
            // Check if parent class exists
            String parentName = symbolTable.getParentClassName(className);
            if (parentName != null && !symbolTable.classExists(parentName)) {
                addError(0, "Class " + className + " inherits from undefined class " + parentName);
                continue;
            }
            
            // Check for inheritance from basic types
            if (parentName != null && (parentName.equals("Int") || 
                parentName.equals("String") || parentName.equals("Bool"))) {
                addError(0, "Class " + className + " cannot inherit from " + parentName);
                continue;
            }
            
            // Check for cycles
            Set<String> visited = new HashSet<>();
            visited.add(className);
            String current = parentName;
            
            while (current != null && !current.equals("Object")) {
                if (visited.contains(current)) {
                    addError(0, "Inheritance cycle detected involving class " + className);
                    break;
                }
                
                visited.add(current);
                current = symbolTable.getParentClassName(current);
                
                // Check if parent exists
                if (current != null && !symbolTable.classExists(current)) {
                    addError(0, "Class " + className + " inherits from undefined class " + current);
                    break;
                }
            }
        }
    }
    
    /**
     * Second pass: Analyze class features (methods and attributes)
     */
    private void analyzeClassFeatures(ClassNode classNode) {
        String className = classNode.getName();
        symbolTable.enterScope(className);
        
        // First define all attributes to make them available for method bodies
        for (FeatureNode feature : classNode.getFeatures()) {
            if (feature instanceof AttributeNode) {
                AttributeNode attr = (AttributeNode) feature;
                defineAttribute(attr, className);
            }
        }
        
        // Then define all methods
        for (FeatureNode feature : classNode.getFeatures()) {
            if (feature instanceof MethodNode) {
                MethodNode method = (MethodNode) feature;
                defineMethod(method, className);
            }
        }
        
        symbolTable.exitScope();
    }
    
    /**
     * Define an attribute in the symbol table
     */
    private void defineAttribute(AttributeNode attr, String className) {
        String attrName = attr.getName();
        String attrType = attr.getType();
        
        // Check if attribute type exists
        if (!symbolTable.classExists(attrType)) {
            addError(attr.getLineNumber(), "Attribute " + attrName + " has undefined type " + attrType);
            return;
        }
        
        // Check if attribute is already defined in this class
        if (symbolTable.attributeExistsInClass(className, attrName)) {
            addError(attr.getLineNumber(), "Attribute " + attrName + " is already defined in class " + className);
            return;
        }
        
        // Check if attribute is inherited
        if (symbolTable.attributeExistsInParent(className, attrName)) {
            addError(attr.getLineNumber(), "Attribute " + attrName + " cannot be redefined in class " + className + " because it is inherited");
            return;
        }
        
        // Add attribute to symbol table
        symbolTable.addAttribute(className, attrName, attrType);
    }
    
    /**
     * Define a method in the symbol table
     */
    private void defineMethod(MethodNode method, String className) {
        String methodName = method.getName();
        String returnType = method.getReturnType();
        List<FormalNode> parameters = method.getParameters();
        
        // Check if return type exists
        if (!returnType.equals("SELF_TYPE") && !symbolTable.classExists(returnType)) {
            addError(method.getLineNumber(), "Method " + methodName + " has undefined return type " + returnType);
            return;
        }
        
        // Check parameter types
        Set<String> paramNames = new HashSet<>();
        List<String> paramTypes = new ArrayList<>();
        
        for (FormalNode param : parameters) {
            String paramName = param.getName();
            String paramType = param.getType();
            
            // Check for duplicate parameter names
            if (paramNames.contains(paramName)) {
                addError(param.getLineNumber(), "Parameter " + paramName + " is multiply defined in method " + methodName);
                return;
            }
            paramNames.add(paramName);
            
            // Check if parameter type exists
            if (!symbolTable.classExists(paramType)) {
                addError(param.getLineNumber(), "Parameter " + paramName + " has undefined type " + paramType);
                return;
            }
            
            // Cannot use SELF_TYPE as a parameter type
            if (paramType.equals("SELF_TYPE")) {
                addError(param.getLineNumber(), "Parameter " + paramName + " cannot have type SELF_TYPE");
                return;
            }
            
            paramTypes.add(paramType);
        }
        
        // Check method override rules if method exists in parent
        MethodInfo parentMethod = symbolTable.getMethodFromParent(className, methodName);
        if (parentMethod != null) {
            // Check return type
            if (!returnType.equals(parentMethod.getReturnType())) {
                addError(method.getLineNumber(), "Method " + methodName + 
                         " in class " + className + 
                         " has return type " + returnType + 
                         " different from overridden method in " + 
                         parentMethod.getClassName() + " with type " + 
                         parentMethod.getReturnType());
                return;
            }
            
            // Check parameter count
            List<String> parentParamTypes = parentMethod.getParameterTypes();
            if (paramTypes.size() != parentParamTypes.size()) {
                addError(method.getLineNumber(), "Method " + methodName + 
                         " in class " + className + 
                         " has " + paramTypes.size() + " parameters but overridden method in " + 
                         parentMethod.getClassName() + " has " + parentParamTypes.size() + " parameters");
                return;
            }
            
            // Check parameter types
            for (int i = 0; i < paramTypes.size(); i++) {
                if (!paramTypes.get(i).equals(parentParamTypes.get(i))) {
                    addError(method.getLineNumber(), "Parameter " + (i+1) + 
                             " of method " + methodName + 
                             " in class " + className + 
                             " has type " + paramTypes.get(i) + 
                             " different from overridden method in " + 
                             parentMethod.getClassName() + " with type " + 
                             parentParamTypes.get(i));
                    return;
                }
            }
        }
        
        // Add method to symbol table
        symbolTable.addMethod(className, methodName, returnType, paramTypes);
    }
    
    /**
     * Third pass: Type check method bodies and expressions
     */
    private void checkClassImplementation(ClassNode classNode) {
        String className = classNode.getName();
        symbolTable.enterScope(className);
        
        // Check attribute initializations
        for (FeatureNode feature : classNode.getFeatures()) {
            if (feature instanceof AttributeNode) {
                AttributeNode attr = (AttributeNode) feature;
                if (attr.getInitialization() != null) {
                    String initType = typeCheckExpression(attr.getInitialization(), className);
                    String attrType = attr.getType();
                    
                    if (!isConforming(initType, attrType, className)) {
                        addError(attr.getLineNumber(), "Type " + initType + 
                                 " of initialization expression does not conform to declared type " + 
                                 attrType + " of attribute " + attr.getName());
                    }
                }
            }
        }
        
        // Check method implementations
        for (FeatureNode feature : classNode.getFeatures()) {
            if (feature instanceof MethodNode) {
                MethodNode method = (MethodNode) feature;
                checkMethodImplementation(method, className);
            }
        }
        
        symbolTable.exitScope();
    }
    
    /**
     * Check method implementation for type correctness
     */
    private void checkMethodImplementation(MethodNode method, String className) {
        String methodName = method.getName();
        
        // Create a new scope for method parameters
        symbolTable.enterMethod(methodName);
        
        // Add parameters to scope
        for (FormalNode param : method.getParameters()) {
            symbolTable.addVariable(param.getName(), param.getType());
        }
        
        // Type check method body
        if (method.getBody() != null) {
            String bodyType = typeCheckExpression(method.getBody(), className);
            String returnType = method.getReturnType();
            
            // Handle SELF_TYPE in return type
            if (returnType.equals("SELF_TYPE")) {
                returnType = className;
            }
            
            // Check if body type conforms to return type
            if (!isConforming(bodyType, returnType, className)) {
                addError(method.getLineNumber(), "Type " + bodyType + 
                         " of method body does not conform to declared return type " + 
                         method.getReturnType());
            }
        }
        
        symbolTable.exitMethod();
    }
    
    /**
     * Type check an expression and return its type
     */
    private String typeCheckExpression(ExpressionNode expr, String className) {
        if (expr instanceof IntConstNode) {
            return "Int";
        } 
        else if (expr instanceof BoolConstNode) {
            return "Bool";
        } 
        else if (expr instanceof StringConstNode) {
            return "String";
        } 
        else if (expr instanceof IdentifierNode) {
            return typeCheckIdentifier((IdentifierNode) expr, className);
        } 
        else if (expr instanceof AssignNode) {
            return typeCheckAssign((AssignNode) expr, className);
        } 
        else if (expr instanceof BlockNode) {
            return typeCheckBlock((BlockNode) expr, className);
        } 
        else if (expr instanceof IfNode) {
            return typeCheckIf((IfNode) expr, className);
        } 
        else if (expr instanceof WhileNode) {
            return typeCheckWhile((WhileNode) expr, className);
        } 
        else if (expr instanceof LetNode) {
            return typeCheckLet((LetNode) expr, className);
        } 
        else if (expr instanceof CaseNode) {
            return typeCheckCase((CaseNode) expr, className);
        } 
        else if (expr instanceof NewNode) {
            return typeCheckNew((NewNode) expr, className);
        } 
        else if (expr instanceof BinaryOpNode) {
            return typeCheckBinaryOp((BinaryOpNode) expr, className);
        } 
        else if (expr instanceof UnaryOpNode) {
            return typeCheckUnaryOp((UnaryOpNode) expr, className);
        } 
        else if (expr instanceof MethodCallNode) {
            return typeCheckMethodCall((MethodCallNode) expr, className);
        } 
        else {
            addError(expr.getLineNumber(), "Unknown expression type: " + expr.getClass().getSimpleName());
            return "Object";
        }
    }
    
    /**
     * Type check an identifier reference
     */
    private String typeCheckIdentifier(IdentifierNode node, String className) {
        String varName = node.getName();
        
        // Handle self reference
        if (varName.equals("self")) {
            return "SELF_TYPE";
        }
        
        // Look up variable in current scope
        String varType = symbolTable.getVariableType(varName);
        if (varType == null) {
            addError(node.getLineNumber(), "Undefined identifier: " + varName);
            return "Object";
        }
        
        return varType;
    }
    
    /**
     * Type check an assignment
     */
    private String typeCheckAssign(AssignNode node, String className) {
        String varName = node.getVariableName();
        String exprType = typeCheckExpression(node.getExpression(), className);
        
        // Cannot assign to self
        if (varName.equals("self")) {
            addError(node.getLineNumber(), "Cannot assign to 'self'");
            return exprType;
        }
        
        // Check if variable exists
        String varType = symbolTable.getVariableType(varName);
        if (varType == null) {
            addError(node.getLineNumber(), "Assignment to undefined identifier: " + varName);
            return exprType;
        }
        
        // Check if expression type conforms to variable type
        if (!isConforming(exprType, varType, className)) {
            addError(node.getLineNumber(), "Type " + exprType + 
                     " of assigned expression does not conform to declared type " + 
                     varType + " of identifier " + varName);
        }
        
        return exprType;
    }
    
    /**
     * Type check a block expression
     */
    private String typeCheckBlock(BlockNode node, String className) {
        String blockType = "Object";
        
        for (ExpressionNode expr : node.getExpressions()) {
            blockType = typeCheckExpression(expr, className);
        }
        
        // Type of block is the type of its last expression
        return blockType;
    }
    
    /**
     * Type check an if-then-else expression
     */
    private String typeCheckIf(IfNode node, String className) {
        // Check that condition is Bool
        String condType = typeCheckExpression(node.getCondition(), className);
        if (!condType.equals("Bool")) {
            addError(node.getLineNumber(), "Condition of if-statement has type " + 
                     condType + " instead of Bool");
        }
        
        // Type check branches
        String thenType = typeCheckExpression(node.getThenBranch(), className);
        String elseType = typeCheckExpression(node.getElseBranch(), className);
        
        // Type of if-then-else is the join (least upper bound) of the branch types
        return leastUpperBound(thenType, elseType, className);
    }
    
    /**
     * Type check a while-loop expression
     */
    private String typeCheckWhile(WhileNode node, String className) {
        // Check that condition is Bool
        String condType = typeCheckExpression(node.getCondition(), className);
        if (!condType.equals("Bool")) {
            addError(node.getLineNumber(), "Condition of while-statement has type " + 
                     condType + " instead of Bool");
        }
        
        // Type check the loop body (but we don't care about its type)
        typeCheckExpression(node.getBody(), className);
        
        // Type of a while-loop is always Object
        return "Object";
    }
    
    /**
     * Type check a let expression
     */
    private String typeCheckLet(LetNode node, String className) {
        symbolTable.enterLet();
        
        // Process all variable declarations
        for (int i = 0; i < node.getVarCount(); i++) {
            String varName = node.getVarName(i);
            String varType = node.getVarType(i);
            
            // Check that declared type exists
            if (!symbolTable.classExists(varType)) {
                addError(node.getLineNumber(), "Let variable " + varName + 
                         " has undefined type " + varType);
                varType = "Object"; // Use Object as fallback
            }
            
            // Check initialization if present
            if (node.getVarInit(i) != null) {
                String initType = typeCheckExpression(node.getVarInit(i), className);
                
                if (!isConforming(initType, varType, className)) {
                    addError(node.getLineNumber(), "Type " + initType + 
                             " of initialization does not conform to declared type " + 
                             varType + " of identifier " + varName);
                }
            }
            
            // Add variable to current scope
            symbolTable.addVariable(varName, varType);
        }
        
        // Type check the body
        String bodyType = typeCheckExpression(node.getBody(), className);
        
        symbolTable.exitLet();
        
        // Type of let expression is the type of its body
        return bodyType;
    }
    
    /**
     * Type check a case expression
     */
    private String typeCheckCase(CaseNode node, String className) {
        // Type check the expression being cased on
        String exprType = typeCheckExpression(node.getExpr(), className);
        
        List<String> branchTypes = new ArrayList<>();
        Set<String> caseTypes = new HashSet<>();
        
        // Type check each branch
        for (int i = 0; i < node.getBranchCount(); i++) {
            String varName = node.getBranchVariable(i);
            String declType = node.getBranchType(i);
            
            // Check that declared type exists
            if (!symbolTable.classExists(declType)) {
                addError(node.getLineNumber(), "Case branch type " + declType + " is undefined");
                continue;
            }
            
            // Check for duplicate types in case branches
            if (caseTypes.contains(declType)) {
                addError(node.getLineNumber(), "Duplicate branch type " + declType + " in case expression");
            }
            caseTypes.add(declType);
            
            // Enter scope for this branch
            symbolTable.enterCase();
            symbolTable.addVariable(varName, declType);
            
            // Type check branch expression
            String branchType = typeCheckExpression(node.getBranchExpr(i), className);
            branchTypes.add(branchType);
            
            symbolTable.exitCase();
        }
        
        // The type of a case expression is the join of all branch types
        if (branchTypes.isEmpty()) {
            return "Object";
        } else {
            String resultType = branchTypes.get(0);
            for (int i = 1; i < branchTypes.size(); i++) {
                resultType = leastUpperBound(resultType, branchTypes.get(i), className);
            }
            return resultType;
        }
    }
    
    /**
     * Type check a new expression
     */
    private String typeCheckNew(NewNode node, String className) {
        String typeName = node.getTypeName();
        
        // Handle SELF_TYPE special case
        if (typeName.equals("SELF_TYPE")) {
            return "SELF_TYPE";
        }
        
        // Check that the type exists
        if (!symbolTable.classExists(typeName)) {
            addError(node.getLineNumber(), "new used with undefined class " + typeName);
            return "Object";
        }
        
        return typeName;
    }
    
    /**
     * Type check a binary operation
     */
    private String typeCheckBinaryOp(BinaryOpNode node, String className) {
        String leftType = typeCheckExpression(node.getLeftOperand(), className);
        String rightType = typeCheckExpression(node.getRightOperand(), className);
        
        switch (node.getOperator()) {
            case PLUS:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
                // Arithmetic operations require Int operands
                if (!leftType.equals("Int")) {
                    addError(node.getLineNumber(), "Left operand of " + node.getOperator() + 
                             " has type " + leftType + " instead of Int");
                }
                if (!rightType.equals("Int")) {
                    addError(node.getLineNumber(), "Right operand of " + node.getOperator() + 
                             " has type " + rightType + " instead of Int");
                }
                return "Int";
                
            case LESS_THAN:
            case LESS_EQUAL:
                // Comparison operations require Int operands
                if (!leftType.equals("Int")) {
                    addError(node.getLineNumber(), "Left operand of " + node.getOperator() + 
                             " has type " + leftType + " instead of Int");
                }
                if (!rightType.equals("Int")) {
                    addError(node.getLineNumber(), "Right operand of " + node.getOperator() + 
                             " has type " + rightType + " instead of Int");
                }
                return "Bool";
                
            case EQUAL:
                // Equality can be used with any types
                // But if either is a basic type (Int, String, Bool), both must be the same type
                if ((leftType.equals("Int") || leftType.equals("String") || leftType.equals("Bool")) &&
                    !leftType.equals(rightType)) {
                    addError(node.getLineNumber(), "Cannot compare " + leftType + 
                             " with " + rightType);
                }
                return "Bool";
                
            default:
                addError(node.getLineNumber(), "Unknown binary operator: " + node.getOperator());
                return "Object";
        }
    }
    
    /**
     * Type check a unary operation
     */
    private String typeCheckUnaryOp(UnaryOpNode node, String className) {
        String exprType = typeCheckExpression(node.getOperand(), className);
        
        switch (node.getOperator()) {
            case NOT:
                // NOT requires Bool operand
                if (!exprType.equals("Bool")) {
                    addError(node.getLineNumber(), "Operand of NOT has type " + 
                             exprType + " instead of Bool");
                }
                return "Bool";
                
            case NEGATE:
                // Unary minus requires Int operand
                if (!exprType.equals("Int")) {
                    addError(node.getLineNumber(), "Operand of ~ has type " + 
                             exprType + " instead of Int");
                }
                return "Int";
                
            case ISVOID:
                // ISVOID can be applied to any expression
                return "Bool";
                
            default:
                addError(node.getLineNumber(), "Unknown unary operator: " + node.getOperator());
                return "Object";
        }
    }
    
    /**
     * Type check a method call
     */
    private String typeCheckMethodCall(MethodCallNode node, String className) {
        // Get the type of the object on which the method is called
        String objectType;
        if (node.getObject() == null) {
            // Self dispatch (implicit self)
            objectType = "SELF_TYPE";
        } else {
            objectType = typeCheckExpression(node.getObject(), className);
        }
        
        // Resolve SELF_TYPE to actual class
        String actualType = objectType.equals("SELF_TYPE") ? className : objectType;
        
        // Handle static dispatch (@Type)
        if (node.getStaticType() != null) {
            String staticType = node.getStaticType();
            
            // Check if static type exists
            if (!symbolTable.classExists(staticType)) {
                addError(node.getLineNumber(), "Static dispatch to undefined class " + staticType);
                return "Object";
            }
            
            // Check if object conforms to static type
            if (!isConforming(objectType, staticType, className)) {
                addError(node.getLineNumber(), "Expression type " + objectType + 
                         " does not conform to declared static dispatch type " + staticType);
            }
            
            actualType = staticType;
        }
        
        // Get method info
        String methodName = node.getMethodName();
        MethodInfo methodInfo = symbolTable.getMethod(actualType, methodName);
        
        if (methodInfo == null) {
            addError(node.getLineNumber(), "Method " + methodName + 
                     " is not defined in class " + actualType + 
                     " or its ancestors");
            return "Object";
        }
        
        // Check number of arguments
        List<ExpressionNode> arguments = node.getArguments();
        List<String> paramTypes = methodInfo.getParameterTypes();
        
        if (arguments.size() != paramTypes.size()) {
            addError(node.getLineNumber(), "Method " + methodName + 
                     " called with wrong number of arguments: expected " + 
                     paramTypes.size() + ", got " + arguments.size());
            return handleReturnType(methodInfo.getReturnType(), objectType);
        }
        
        // Type check arguments
        for (int i = 0; i < arguments.size(); i++) {
            String argType = typeCheckExpression(arguments.get(i), className);
            String paramType = paramTypes.get(i);
            
            if (!isConforming(argType, paramType, className)) {
                addError(node.getLineNumber(), "Argument #" + (i+1) + 
                         " type " + argType + 
                         " does not conform to parameter type " + paramType);
            }
        }
        
        return handleReturnType(methodInfo.getReturnType(), objectType);
    }
    
    /**
     * Handle method return type, replacing SELF_TYPE with actual type
     */
    private String handleReturnType(String returnType, String objectType) {
        if (returnType.equals("SELF_TYPE")) {
            return objectType;
        }
        return returnType;
    }
    
    /**
     * Check if Main class exists and has a main method
     */
    private void checkMainClassExists() {
        if (!symbolTable.classExists("Main")) {
            addError(0, "Class Main is not defined");
            return;
        }
        
        MethodInfo mainMethod = symbolTable.getMethod("Main", "main");
        if (mainMethod == null) {
            addError(0, "No 'main' method in class Main");
        } else if (!mainMethod.getParameterTypes().isEmpty()) {
            addError(0, "main method in class Main should have no parameters");
        }
    }
    
    /**
     * Check if type t1 conforms to type t2 (t1 ≤ t2)
     */
    private boolean isConforming(String t1, String t2, String currentClass) {
        // Handle SELF_TYPE
        if (t1.equals("SELF_TYPE")) {
            if (t2.equals("SELF_TYPE")) {
                return true;
            }
            return isConforming(currentClass, t2, currentClass);
        }
        
        if (t2.equals("SELF_TYPE")) {
            return false; // t1 is not SELF_TYPE, so can't conform to SELF_TYPE
        }
        
        // Same type conforms
        if (t1.equals(t2)) {
            return true;
        }
        
        // Check inheritance hierarchy
        return symbolTable.isSubclassOf(t1, t2);
    }
    
    /**
     * Find the least upper bound (join) of two types in the inheritance hierarchy
     */
    private String leastUpperBound(String t1, String t2, String currentClass) {
        // Handle SELF_TYPE
        if (t1.equals("SELF_TYPE")) {
            t1 = currentClass;
        }
        
        if (t2.equals("SELF_TYPE")) {
            t2 = currentClass;
        }
        
        if (t1.equals(t2)) {
            return t1;
        }
        
        if (symbolTable.isSubclassOf(t1, t2)) {
            return t2;
        }
        
        if (symbolTable.isSubclassOf(t2, t1)) {
            return t1;
        }
        
        // Find nearest common ancestor
        String current = t1;
        while (current != null) {
            if (symbolTable.isSubclassOf(t2, current)) {
                return current;
            }
            current = symbolTable.getParentClassName(current);
        }
        
        // Fallback to Object
        return "Object";
    }
    
    /**
     * Add a semantic error
     */
    private void addError(int lineNumber, String message) {
        errors.add(new SemanticError(lineNumber, message));
    }
}
