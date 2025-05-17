package semantic;

/**
 * Represents a semantic error found during analysis
 */
public class SemanticError {
    private int lineNumber;
    private String message;
    
    public SemanticError(int lineNumber, String message) {
        this.lineNumber = lineNumber;
        this.message = message;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return "Error at line " + lineNumber + ": " + message;
    }
}
