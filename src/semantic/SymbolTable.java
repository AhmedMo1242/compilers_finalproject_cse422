package semantic;

import java.util.*;

/**
 * Symbol table for semantic analysis.
 * Manages classes, methods, variables, and scopes.
 */
public class SymbolTable {
    // Class hierarchy information
    private Map<String, String> classParents; // class -> parent
    private Map<String, Map<String, String>> classAttributes; // class -> (name -> type)
    private Map<String, Map<String, MethodInfo>> classMethods; // class -> (name -> method)
    
    // Current scope stack
    private Stack<Map<String, String>> scopeStack; // name -> type
    
    // Current class being analyzed
    private String currentClass;
    
    public SymbolTable() {
        classParents = new HashMap<>();
        classAttributes = new HashMap<>();
        classMethods = new HashMap<>();
        scopeStack = new Stack<>();
        
        // Initialize basic classes
        initBasicClasses();
    }
    
    /**
     * Initialize the built-in COOL classes
     */
    private void initBasicClasses() {
        // Object class (root of hierarchy)
        addClass("Object", null);
        addMethod("Object", "abort", "Object", Collections.emptyList());
        addMethod("Object", "type_name", "String", Collections.emptyList());
        addMethod("Object", "copy", "SELF_TYPE", Collections.emptyList());
        
        // IO class
        addClass("IO", "Object");
        addMethod("IO", "out_string", "SELF_TYPE", Arrays.asList("String"));
        addMethod("IO", "out_int", "SELF_TYPE", Arrays.asList("Int"));
        addMethod("IO", "in_string", "String", Collections.emptyList());
        addMethod("IO", "in_int", "Int", Collections.emptyList());
        
        // Int class
        addClass("Int", "Object");
        
        // String class
        addClass("String", "Object");
        addMethod("String", "length", "Int", Collections.emptyList());
        addMethod("String", "concat", "String", Arrays.asList("String"));
        addMethod("String", "substr", "String", Arrays.asList("Int", "Int"));
        
        // Bool class
        addClass("Bool", "Object");
    }
    
    /**
     * Add a class to the symbol table
     */
    public void addClass(String className, String parentName) {
        // Default parent is Object
        if (parentName == null && !className.equals("Object")) {
            parentName = "Object";
        }
        
        classParents.put(className, parentName);
        classAttributes.put(className, new HashMap<>());
        classMethods.put(className, new HashMap<>());
    }
    
    /**
     * Add an attribute to a class
     */
    public void addAttribute(String className, String attrName, String attrType) {
        classAttributes.get(className).put(attrName, attrType);
    }
    
    /**
     * Add a method to a class
     */
    public void addMethod(String className, String methodName, String returnType, List<String> paramTypes) {
        classMethods.get(className).put(methodName, new MethodInfo(className, methodName, returnType, paramTypes));
    }
    
    /**
     * Check if a class exists
     */
    public boolean classExists(String className) {
        return classParents.containsKey(className);
    }
    
    /**
     * Get all class names
     */
    public Set<String> getAllClassNames() {
        return classParents.keySet();
    }
    
    /**
     * Get the parent class name
     */
    public String getParentClassName(String className) {
        return classParents.get(className);
    }
    
    /**
     * Check if a class is a subclass of another
     */
    public boolean isSubclassOf(String className, String ancestorName) {
        String current = className;
        while (current != null) {
            if (current.equals(ancestorName)) {
                return true;
            }
            current = classParents.get(current);
        }
        return false;
    }
    
    /**
     * Check if an attribute exists in the current class
     */
    public boolean attributeExistsInClass(String className, String attrName) {
        return classAttributes.get(className).containsKey(attrName);
    }
    
    /**
     * Check if an attribute exists in a parent class
     */
    public boolean attributeExistsInParent(String className, String attrName) {
        String parent = classParents.get(className);
        while (parent != null) {
            if (classAttributes.get(parent).containsKey(attrName)) {
                return true;
            }
            parent = classParents.get(parent);
        }
        return false;
    }
    
    /**
     * Get the method from a class or its ancestors
     */
    public MethodInfo getMethod(String className, String methodName) {
        String current = className;
        while (current != null) {
            if (classMethods.get(current).containsKey(methodName)) {
                return classMethods.get(current).get(methodName);
            }
            current = classParents.get(current);
        }
        return null;
    }
    
    /**
     * Get the method from a parent class
     */
    public MethodInfo getMethodFromParent(String className, String methodName) {
        String parent = classParents.get(className);
        while (parent != null) {
            if (classMethods.get(parent).containsKey(methodName)) {
                return classMethods.get(parent).get(methodName);
            }
            parent = classParents.get(parent);
        }
        return null;
    }
    
    /**
     * Enter a new class scope
     */
    public void enterScope(String className) {
        currentClass = className;
        scopeStack.push(new HashMap<>());
        
        // Add 'self' to the scope
        scopeStack.peek().put("self", "SELF_TYPE");
        
        // Add all attributes from this class and its ancestors
        String current = className;
        while (current != null) {
            for (Map.Entry<String, String> attr : classAttributes.get(current).entrySet()) {
                // Only add if not already in scope (don't shadow)
                if (!scopeStack.peek().containsKey(attr.getKey())) {
                    scopeStack.peek().put(attr.getKey(), attr.getValue());
                }
            }
            current = classParents.get(current);
        }
    }
    
    /**
     * Exit the current scope
     */
    public void exitScope() {
        if (!scopeStack.isEmpty()) {
            scopeStack.pop();
        }
        
        // Reset current class if we exited last scope
        if (scopeStack.isEmpty()) {
            currentClass = null;
        }
    }
    
    /**
     * Enter a method scope
     */
    public void enterMethod(String methodName) {
        scopeStack.push(new HashMap<>());
    }
    
    /**
     * Exit a method scope
     */
    public void exitMethod() {
        if (!scopeStack.isEmpty()) {
            scopeStack.pop();
        }
    }
    
    /**
     * Enter a let scope
     */
    public void enterLet() {
        scopeStack.push(new HashMap<>());
    }
    
    /**
     * Exit a let scope
     */
    public void exitLet() {
        if (!scopeStack.isEmpty()) {
            scopeStack.pop();
        }
    }
    
    /**
     * Enter a case branch scope
     */
    public void enterCase() {
        scopeStack.push(new HashMap<>());
    }
    
    /**
     * Exit a case branch scope
     */
    public void exitCase() {
        if (!scopeStack.isEmpty()) {
            scopeStack.pop();
        }
    }
    
    /**
     * Add a variable to the current scope
     */
    public void addVariable(String name, String type) {
        if (!scopeStack.isEmpty()) {
            scopeStack.peek().put(name, type);
        }
    }
    
    /**
     * Get a variable's type from the current or enclosing scopes
     */
    public String getVariableType(String name) {
        // Search from innermost to outermost scope
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            String type = scopeStack.get(i).get(name);
            if (type != null) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * Get the current class name
     */
    public String getCurrentClass() {
        return currentClass;
    }
}
