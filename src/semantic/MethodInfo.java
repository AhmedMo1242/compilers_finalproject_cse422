package semantic;

import java.util.List;

/**
 * Stores information about a method definition
 */
public class MethodInfo {
    private String className;
    private String methodName;
    private String returnType;
    private List<String> parameterTypes;
    
    public MethodInfo(String className, String methodName, String returnType, List<String> parameterTypes) {
        this.className = className;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }
    
    public String getClassName() {
        return className;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public List<String> getParameterTypes() {
        return parameterTypes;
    }
}
