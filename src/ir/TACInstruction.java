package ir;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Three Address Code (TAC) instruction
 */
public class TACInstruction {
    public enum OpType {
        ASSIGN,      // x = y
        BINARY_OP,   // x = y op z
        JUMP,        // goto L
        COND_JUMP,   // if x goto L
        LABEL,       // L:
        CALL,        // x = call f(args)
        COMMENT      // Comment for readability
    }
    
    private OpType type;
    private String result;     // Target variable/temp
    private String op1;        // First operand
    private String op2;        // Second operand
    private String operation;  // Operation type ("+", "-", etc.) or label for jumps
    
    // Constructor for ASSIGN: result = op1
    public TACInstruction(String result, String op1) {
        this.type = OpType.ASSIGN;
        this.result = result;
        this.op1 = op1;
        this.op2 = null;
        this.operation = "=";
    }
    
    // Constructor for BINARY_OP: result = op1 operation op2
    public TACInstruction(String result, String op1, String operation, String op2) {
        this.type = OpType.BINARY_OP;
        this.result = result;
        this.op1 = op1;
        this.op2 = op2;
        this.operation = operation;
    }
    
    // Constructor for JUMP: goto label
    public TACInstruction(String label) {
        this.type = OpType.JUMP;
        this.result = null;
        this.op1 = null;
        this.op2 = null;
        this.operation = label;
    }
    
    // Constructor for COND_JUMP: if op1 goto label, jumpIfTrue determines if/ifnot
    public TACInstruction(String op1, String label, boolean jumpIfTrue) {
        this.type = OpType.COND_JUMP;
        this.result = null;
        this.op1 = op1;
        this.op2 = null;
        this.operation = label;
        // Store jumpIfTrue as op2 to save space
        this.op2 = jumpIfTrue ? "true" : "false";
    }
    
    // Constructor for LABEL: label:
    public static TACInstruction createLabel(String label) {
        TACInstruction instr = new TACInstruction(null, null);
        instr.type = OpType.LABEL;
        instr.operation = label;
        return instr;
    }
    
    // Constructor for CALL: result = call method(args)
    public static TACInstruction createCall(String result, String method, List<String> args) {
        TACInstruction instr = new TACInstruction(null, null);
        instr.type = OpType.CALL;
        instr.result = result;
        instr.operation = method;
        
        // Store first argument in op1, rest in argList
        instr.op1 = args.isEmpty() ? null : args.get(0);
        if (args.size() > 1) {
            instr.argList = new ArrayList<>(args.subList(1, args.size()));
        } else {
            instr.argList = new ArrayList<>();
        }
        return instr;
    }
    
    // Constructor for COMMENT
    public static TACInstruction createComment(String comment) {
        TACInstruction instr = new TACInstruction(null, null);
        instr.type = OpType.COMMENT;
        instr.operation = comment;
        return instr;
    }
    
    // For method calls, we need a list of additional arguments
    private List<String> argList;
    
    public OpType getType() {
        return type;
    }
    
    public String getResult() {
        return result;
    }
    
    public String getOp1() {
        return op1;
    }
    
    public String getOp2() {
        return op2;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public List<String> getArgList() {
        return argList;
    }
    
    @Override
    public String toString() {
        switch (type) {
            case ASSIGN:
                return result + " = " + op1;
                
            case BINARY_OP:
                return result + " = " + op1 + " " + operation + " " + op2;
                
            case JUMP:
                return "goto " + operation;
                
            case COND_JUMP:
                return (op2.equals("true") ? "if " : "ifnot ") + op1 + " goto " + operation;
                
            case LABEL:
                return operation + ":";
                
            case CALL:
                StringBuilder sb = new StringBuilder();
                if (result != null) {
                    sb.append(result).append(" = ");
                }
                sb.append("call ").append(operation).append("(");
                
                if (op1 != null) {
                    sb.append(op1);
                    for (String arg : argList) {
                        sb.append(", ").append(arg);
                    }
                }
                sb.append(")");
                return sb.toString();
                
            case COMMENT:
                return "// " + operation;
                
            default:
                return "unknown instruction";
        }
    }
}
