package ast;

import org.antlr.v4.runtime.Token;
import ast.expressions.*;

// Import the packaged ANTLR classes
import compiler.cool_synBaseVisitor;
import compiler.cool_synParser;

import java.util.ArrayList;

/**
 * Visitor implementation that converts the ANTLR parse tree to our AST.
 */
public class ASTBuilder extends cool_synBaseVisitor<ASTNode> {
    
    @Override
    public ASTNode visitStart(cool_synParser.StartContext ctx) {
        // Create a program node with the line number from the first token
        ProgramNode program = new ProgramNode(ctx.getStart().getLine());
        
        // Visit each class definition and add it to the program
        for (cool_synParser.ClassContext classCtx : ctx.class_()) {
            ClassNode classNode = (ClassNode) visit(classCtx);
            program.addClass(classNode);
        }
        
        return program;
    }
    
    @Override
    public ASTNode visitClassdef(cool_synParser.ClassdefContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String className = ctx.TYPE_IDENTIFIER(0).getText();
        
        // Get parent class name if it exists, otherwise use "Object" as default
        String parentClass = ctx.INHERITS() != null ? 
                ctx.TYPE_IDENTIFIER(1).getText() : "Object";
        
        ClassNode classNode = new ClassNode(lineNumber, className, parentClass);
        
        // Visit each feature (method or attribute) and add it to the class
        for (cool_synParser.FeatureContext featureCtx : ctx.feature()) {
            FeatureNode feature = (FeatureNode) visit(featureCtx);
            classNode.addFeature(feature);
        }
        
        return classNode;
    }
    
    @Override
    public ASTNode visitFunction(cool_synParser.FunctionContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String methodName = ctx.OBJECT_IDENTIFIER().getText();
        String returnType = ctx.TYPE_IDENTIFIER().getText();
        
        MethodNode methodNode = new MethodNode(lineNumber, methodName, returnType);
        
        // Add formal parameters if they exist
        if (ctx.formal() != null) {
            for (cool_synParser.FormalContext formalCtx : ctx.formal()) {
                FormalNode param = (FormalNode) visit(formalCtx);
                methodNode.addParameter(param);
            }
        }
        
        // Visit method body with better error handling
        try {
            if (ctx.expr() != null) {
                ExpressionNode body = (ExpressionNode) visit(ctx.expr());
                methodNode.setBody(body);
            } else {
                System.err.println("Warning: Method " + methodName + " has no expression body");
            }
        } catch (Exception e) {
            System.err.println("Error visiting method body for " + methodName + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return methodNode;
    }
    
    @Override
    public ASTNode visitVariable(cool_synParser.VariableContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String name = ctx.OBJECT_IDENTIFIER().getText();
        String type = ctx.TYPE_IDENTIFIER().getText();
        
        AttributeNode attrNode = new AttributeNode(lineNumber, name, type);
        
        // Handle initialization if present
        if (ctx.expr() != null) {
            ExpressionNode init = (ExpressionNode) visit(ctx.expr());
            attrNode.setInitialization(init);
        }
        
        return attrNode;
    }
    
    @Override
    public ASTNode visitParameter(cool_synParser.ParameterContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String name = ctx.OBJECT_IDENTIFIER().getText();
        String type = ctx.TYPE_IDENTIFIER().getText();
        
        return new FormalNode(lineNumber, name, type);
    }
    
    @Override
    public ASTNode visitAssign(cool_synParser.AssignContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String varName = ctx.OBJECT_IDENTIFIER().getText();
        ExpressionNode expr = (ExpressionNode) visit(ctx.expr());
        
        return new AssignNode(lineNumber, varName, expr);
    }
    
    @Override
    public ASTNode visitObjectcall(cool_synParser.ObjectcallContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        // Get object on which method is called
        ExpressionNode object = (ExpressionNode) visit(ctx.expr(0));
        
        // Get static type if present
        String staticType = null;
        if (ctx.ATSYM() != null) {
            staticType = ctx.TYPE_IDENTIFIER().getText();
        }
        
        String methodName = ctx.OBJECT_IDENTIFIER().getText();
        
        MethodCallNode methodCall = new MethodCallNode(
            lineNumber, object, methodName, staticType);
        
        // Add arguments if present
        for (int i = 1; i < ctx.expr().size(); i++) {
            methodCall.addArgument((ExpressionNode) visit(ctx.expr(i)));
        }
        
        return methodCall;
    }
    
    @Override
    public ASTNode visitStaticcall(cool_synParser.StaticcallContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String methodName = ctx.OBJECT_IDENTIFIER().getText();
        
        // For regular static calls (not on an explicit object), pass null as object
        MethodCallNode methodCall = new MethodCallNode(
            lineNumber, null, methodName, null);
        
        // Add arguments if present
        for (cool_synParser.ExprContext exprCtx : ctx.expr()) {
            methodCall.addArgument((ExpressionNode) visit(exprCtx));
        }
        
        return methodCall;
    }
    
    @Override
    public ASTNode visitId(cool_synParser.IdContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String name = ctx.OBJECT_IDENTIFIER().getText();
        
        return new IdentifierNode(lineNumber, name);
    }
    
    @Override
    public ASTNode visitNum(cool_synParser.NumContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        int value = Integer.parseInt(ctx.INTEGERS().getText());
        
        return new IntConstNode(lineNumber, value);
    }
    
    @Override
    public ASTNode visitText(cool_synParser.TextContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        // Remove the surrounding quotes
        String value = ctx.STRING().getText();
        value = value.substring(1, value.length() - 1);
        
        return new StringConstNode(lineNumber, value);
    }
    
    @Override
    public ASTNode visitTrue(cool_synParser.TrueContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        return new BoolConstNode(lineNumber, true);
    }
    
    @Override
    public ASTNode visitFalse(cool_synParser.FalseContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        return new BoolConstNode(lineNumber, false);
    }
    
    @Override
    public ASTNode visitBlock(cool_synParser.BlockContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        BlockNode block = new BlockNode(lineNumber);
        
        // Add all expressions in the block
        for (cool_synParser.ExprContext exprCtx : ctx.expr()) {
            block.addExpression((ExpressionNode) visit(exprCtx));
        }
        
        return block;
    }
    
    @Override
    public ASTNode visitIf(cool_synParser.IfContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode condition = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode thenExpr = (ExpressionNode) visit(ctx.expr(1));
        ExpressionNode elseExpr = (ExpressionNode) visit(ctx.expr(2));
        
        return new IfNode(lineNumber, condition, thenExpr, elseExpr);
    }
    
    @Override
    public ASTNode visitWhile(cool_synParser.WhileContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode condition = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode body = (ExpressionNode) visit(ctx.expr(1));
        
        return new WhileNode(lineNumber, condition, body);
    }
    
    @Override
    public ASTNode visitAdd(cool_synParser.AddContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode left = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        
        return new BinaryOpNode(lineNumber, BinaryOpNode.Operator.PLUS, left, right);
    }
    
    @Override
    public ASTNode visitSub(cool_synParser.SubContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode left = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        
        return new BinaryOpNode(lineNumber, BinaryOpNode.Operator.MINUS, left, right);
    }
    
    @Override
    public ASTNode visitMul(cool_synParser.MulContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode left = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        
        return new BinaryOpNode(lineNumber, BinaryOpNode.Operator.MULTIPLY, left, right);
    }
    
    @Override
    public ASTNode visitDiv(cool_synParser.DivContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode left = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        
        return new BinaryOpNode(lineNumber, BinaryOpNode.Operator.DIVIDE, left, right);
    }
    
    @Override
    public ASTNode visitLt(cool_synParser.LtContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode left = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        
        return new BinaryOpNode(lineNumber, BinaryOpNode.Operator.LESS_THAN, left, right);
    }
    
    @Override
    public ASTNode visitLq(cool_synParser.LqContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode left = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        
        return new BinaryOpNode(lineNumber, BinaryOpNode.Operator.LESS_EQUAL, left, right);
    }
    
    @Override
    public ASTNode visitEq(cool_synParser.EqContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        ExpressionNode left = (ExpressionNode) visit(ctx.expr(0));
        ExpressionNode right = (ExpressionNode) visit(ctx.expr(1));
        
        return new BinaryOpNode(lineNumber, BinaryOpNode.Operator.EQUAL, left, right);
    }
    
    @Override
    public ASTNode visitNewobject(cool_synParser.NewobjectContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        String typeName = ctx.TYPE_IDENTIFIER().getText();
        
        return new NewNode(lineNumber, typeName);
    }
    
    @Override
    public ASTNode visitParexpr(cool_synParser.ParexprContext ctx) {
        // Just return the inner expression
        return visit(ctx.expr());
    }
    
    @Override
    public ASTNode visitLet(cool_synParser.LetContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        LetNode letNode = new LetNode(lineNumber);
        
        // Process all variable declarations
        for (int i = 0; i < ctx.OBJECT_IDENTIFIER().size(); i++) {
            String name = ctx.OBJECT_IDENTIFIER(i).getText();
            String type = ctx.TYPE_IDENTIFIER(i).getText();
            
            // Check if there's an initialization for this variable
            ExpressionNode init = null;
            if (i < ctx.expr().size() - 1 && ctx.ASSIGN_OP(i) != null) {
                init = (ExpressionNode) visit(ctx.expr(i));
            }
            
            letNode.addVariable(name, type, init);
        }
        
        // Set the body expression (the last expression)
        letNode.setBody((ExpressionNode) visit(ctx.expr(ctx.expr().size() - 1)));
        
        return letNode;
    }
    
    @Override
    public ASTNode visitCase(cool_synParser.CaseContext ctx) {
        int lineNumber = ctx.getStart().getLine();
        
        // Get the expression to test
        ExpressionNode expr = (ExpressionNode) visit(ctx.expr(0));
        CaseNode caseNode = new CaseNode(lineNumber, expr);
        
        // Add each branch
        for (int i = 0; i < ctx.OBJECT_IDENTIFIER().size(); i++) {
            String varName = ctx.OBJECT_IDENTIFIER(i).getText();
            String typeName = ctx.TYPE_IDENTIFIER(i).getText();
            ExpressionNode branchBody = (ExpressionNode) visit(ctx.expr(i + 1));
            
            caseNode.addBranch(varName, typeName, branchBody);
        }
        
        return caseNode;
    }
}
