package ir;

/**
 * Manages temporaries and labels for IR generation
 */
public class IRContext {
    private int tempCounter = 0;
    private int labelCounter = 0;
    
    /**
     * Generate a new temporary variable name
     */
    public String newTemp() {
        return "t" + (++tempCounter);
    }
    
    /**
     * Generate a new label name
     */
    public String newLabel() {
        return "L" + (++labelCounter);
    }
}
