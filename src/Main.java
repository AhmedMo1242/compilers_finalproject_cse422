import ast.ASTBuilder;
import ast.ASTNode;
import ast.ClassNode;
import ast.FeatureNode;
import ast.FormalNode;
import ast.MethodNode;
import ast.AttributeNode;
import ast.ProgramNode;
import ast.expressions.*;
import compiler.cool_lex;
import compiler.cool_synParser;
import ir.IRGenerator;
import ir.TACInstruction;
import semantic.SemanticAnalyzer;
import semantic.SemanticError;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Base directory for test files
    private static final String TEST_DIR = "D:/compilers/compilers_finalproject_cse422/testcases";
    
    public static void main(String[] args) {
        System.out.println("=== Cool Compiler Tests ===");
        
        // Test specific file if provided
        if (args.length >= 1) {
            String filePath = args[0];
            System.out.println("\nTesting file: " + filePath);
            try {
                String input = new String(Files.readAllBytes(Paths.get(filePath)));
                runFullCompiler(input);
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
            return;
        }
        
        // Run the simple IR test directly
        System.out.println("\n=== RUNNING SIMPLE IR TEST ===");
        testSimpleIRGeneration();
        
        // Otherwise, run tests on all test files organized by category
        try {
            runAllLexerTests();
            runAllParserTests();
            runAllASTTests();
            runAllSemanticTests();
            runAllIRTests();  // Added IR generation tests
            
            System.out.println("\nAll tests completed.");
        } catch (Exception e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runAllLexerTests() throws IOException {
        System.out.println("\n=== LEXICAL ANALYSIS TESTS ===");
        processTestFolder(Paths.get(TEST_DIR, "lexer").toString(), "lexer");
    }
    
    private static void runAllParserTests() throws IOException {
        System.out.println("\n=== SYNTAX ANALYSIS TESTS ===");
        processTestFolder(Paths.get(TEST_DIR, "parser").toString(), "parser");
    }
    
    private static void runAllASTTests() throws IOException {
        System.out.println("\n=== AST CONSTRUCTION TESTS ===");
        processTestFolder(Paths.get(TEST_DIR, "ast").toString(), "ast");
    }
    
    private static void runAllSemanticTests() throws IOException {
        System.out.println("\n=== SEMANTIC ANALYSIS TESTS ===");
        processTestFolder(Paths.get(TEST_DIR, "semantic").toString(), "semantic");
    }
    
    // New method for IR generation tests
    private static void runAllIRTests() throws IOException {
        System.out.println("\n=== IR GENERATION TESTS ===");
        processTestFolder(Paths.get(TEST_DIR, "ir").toString(), "ir");
    }
    
    private static void processTestFolder(String folderPath, String mode) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Test folder not found: " + folderPath);
            return;
        }
        
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".cl"));
        if (files == null || files.length == 0) {
            System.out.println("No test files found in " + folderPath);
            return;
        }
        
        for (File file : files) {
            System.out.println("\n--- Testing: " + file.getName() + " ---");
            String input = new String(Files.readAllBytes(file.toPath()));
            
            switch (mode) {
                case "lexer":
                    testLexer(input);
                    break;
                case "parser":
                    testParser(input);
                    break;
                case "ast":
                    testAst(input);
                    break;
                case "semantic":
                    testSemantic(input);
                    break;
                case "ir":
                    testIR(input);  // Added IR test
                    break;
            }
        }
    }
    
    private static void testLexer(String input) {
        CharStream charStream = CharStreams.fromString(input);
        cool_lex lexer = new cool_lex(charStream);
        
        System.out.println("LEXER TEST OUTPUT:");
        System.out.println("------------------");
        
        // Collect all tokens and print them
        Token token;
        while ((token = lexer.nextToken()).getType() != Token.EOF) {
            String tokenName = lexer.getVocabulary().getSymbolicName(token.getType());
            String tokenText = token.getText();
            int line = token.getLine();
            int charPos = token.getCharPositionInLine();
            
            System.out.printf("Line %-3d Col %-3d: %-20s '%s'\n", 
                    line, charPos, tokenName, tokenText);
        }
        
        System.out.println("------------------");
        System.out.println("Lexical analysis completed successfully.");
    }
    
    private static void testParser(String input) {
        CharStream charStream = CharStreams.fromString(input);
        cool_lex lexer = new cool_lex(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        cool_synParser parser = new cool_synParser(tokens);
        
        // Add error listener to capture syntax errors
        parser.removeErrorListeners();
        SyntaxErrorCollector errorCollector = new SyntaxErrorCollector();
        parser.addErrorListener(errorCollector);
        
        System.out.println("PARSER TEST OUTPUT:");
        System.out.println("------------------");
        
        ParseTree parseTree = parser.program();
        
        if (errorCollector.hasErrors()) {
            System.out.println("Parsing failed with syntax errors:");
            List<String> errors = errorCollector.getErrors();
            for (String error : errors) {
                System.out.println("  - " + error);
            }
        } else {
            System.out.println("Parsing completed successfully.");
            System.out.println("Parse tree structure:");
            System.out.println(parseTree.toStringTree(parser));
        }
        
        System.out.println("------------------");
    }
    
    private static void testAst(String input) {
        CharStream charStream = CharStreams.fromString(input);
        cool_lex lexer = new cool_lex(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cool_synParser parser = new cool_synParser(tokens);
        ParseTree parseTree = parser.program();
        
        ASTBuilder astBuilder = new ASTBuilder();
        ASTNode ast = astBuilder.visit(parseTree);
        
        System.out.println("AST TEST OUTPUT:");
        System.out.println("------------------");
        
        if (ast != null) {
            System.out.println("AST Construction Successful:");
            System.out.println(ast.toString());
        } else {
            System.err.println("Failed to build AST.");
        }
        
        System.out.println("------------------");
    }
    
    private static void testSemantic(String input) {
        CharStream charStream = CharStreams.fromString(input);
        cool_lex lexer = new cool_lex(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cool_synParser parser = new cool_synParser(tokens);
        
        // Add error listener to capture syntax errors
        parser.removeErrorListeners();
        SyntaxErrorCollector errorCollector = new SyntaxErrorCollector();
        parser.addErrorListener(errorCollector);
        
        ParseTree parseTree = parser.program();
        
        if (errorCollector.hasErrors()) {
            System.out.println("SEMANTIC TEST OUTPUT:");
            System.out.println("------------------");
            System.out.println("Cannot perform semantic analysis - syntax errors exist:");
            for (String error : errorCollector.getErrors()) {
                System.out.println("  - " + error);
            }
            System.out.println("------------------");
            return;
        }
        
        ASTBuilder astBuilder = new ASTBuilder();
        ASTNode ast = astBuilder.visit(parseTree);
        
        System.out.println("SEMANTIC TEST OUTPUT:");
        System.out.println("------------------");
        
        if (ast == null) {
            System.err.println("Failed to build AST. Cannot perform semantic analysis.");
            System.out.println("------------------");
            return;
        }
        
        if (!(ast instanceof ProgramNode)) {
            System.err.println("Expected ProgramNode at the root of AST. Cannot perform semantic analysis.");
            System.out.println("------------------");
            return;
        }
        
        ProgramNode programNode = (ProgramNode) ast;
        
        // Perform semantic analysis
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        List<SemanticError> errors = analyzer.analyze(programNode);
        
        if (errors.isEmpty()) {
            System.out.println("No semantic errors found.");
        } else {
            System.out.println("Semantic errors found:");
            for (SemanticError error : errors) {
                System.out.println("  - " + error);
            }
        }
        
        System.out.println("------------------");
        System.out.println("Semantic analysis completed.");
    }
    
    // New method for IR generation testing
    private static void testIR(String input) {
        CharStream charStream = CharStreams.fromString(input);
        cool_lex lexer = new cool_lex(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cool_synParser parser = new cool_synParser(tokens);
        
        // Add error listener to capture syntax errors
        parser.removeErrorListeners();
        SyntaxErrorCollector errorCollector = new SyntaxErrorCollector();
        parser.addErrorListener(errorCollector);
        
        ParseTree parseTree = parser.program();
        
        if (errorCollector.hasErrors()) {
            System.out.println("IR TEST OUTPUT:");
            System.out.println("------------------");
            System.out.println("Cannot generate IR - syntax errors exist");
            System.out.println("------------------");
            return;
        }
        
        ASTBuilder astBuilder = new ASTBuilder();
        ASTNode ast = astBuilder.visit(parseTree);
        
        if (!(ast instanceof ProgramNode)) {
            System.out.println("IR TEST OUTPUT:");
            System.out.println("------------------");
            System.out.println("Cannot generate IR - invalid AST");
            System.out.println("------------------");
            return;
        }
        
        ProgramNode programNode = (ProgramNode) ast;
        
        // Perform semantic analysis first
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        List<SemanticError> errors = analyzer.analyze(programNode);
        
        System.out.println("IR TEST OUTPUT:");
        System.out.println("------------------");
        
        if (!errors.isEmpty()) {
            System.out.println("Cannot generate IR - semantic errors exist:");
            for (SemanticError error : errors) {
                System.out.println("  - " + error);
            }
            System.out.println("------------------");
            return;
        }
        
        // Generate IR
        IRGenerator irGenerator = new IRGenerator();
        List<TACInstruction> instructions = irGenerator.generate(programNode);
        
        System.out.println("Generated IR:");
        for (TACInstruction instr : instructions) {
            System.out.println(instr);
        }
        
        System.out.println("------------------");
        System.out.println("IR generation completed.");
    }
    
    /**
     * Test IR generation with a simple program constructed directly
     */
    private static void testSimpleIRGeneration() {
        System.out.println("Building a simple AST manually for IR generation testing...");
        
        // Create a simple program:
        // class Main {
        //   x: Int <- 5;
        //   main(): Object {
        //     let y: Int <- x + 10 in
        //       y
        //   };
        // };
        
        // Create the program node
        ProgramNode program = new ProgramNode(1);
        
        // Create the Main class
        ClassNode mainClass = new ClassNode(1, "Main", "Object");
        
        // Create attribute x: Int <- 5
        AttributeNode attrX = new AttributeNode(2, "x", "Int");
        attrX.setInitialization(new IntConstNode(2, 5));
        
        // Create main method
        MethodNode mainMethod = new MethodNode(3, "main", "Object");
        
        // Create the method body: let y: Int <- x + 10 in y
        LetNode letExpr = new LetNode(4);
        letExpr.addVariable("y", "Int", 
            new BinaryOpNode(4, BinaryOpNode.Operator.PLUS, 
                new IdentifierNode(4, "x"), 
                new IntConstNode(4, 10))
        );
        letExpr.setBody(new IdentifierNode(5, "y"));
        
        mainMethod.setBody(letExpr);
        
        // Add features to class
        mainClass.addFeature(attrX);
        mainClass.addFeature(mainMethod);
        
        // Add class to program
        program.addClass(mainClass);
        
        // Generate IR code
        IRGenerator irGenerator = new IRGenerator();
        List<TACInstruction> instructions = irGenerator.generate(program);
        
        // Print the generated IR
        System.out.println("\nGENERATED IR CODE:");
        System.out.println("==================");
        for (TACInstruction instr : instructions) {
            System.out.println(instr);
        }
        System.out.println("==================");
        System.out.println("IR generation test completed.");
    }
    
    private static void runFullCompiler(String input) {
        // First run lexer
        System.out.println("\nSTAGE 1: LEXICAL ANALYSIS");
        System.out.println("=========================");
        testLexer(input);
        System.out.println();
        
        // Then parse
        System.out.println("STAGE 2: SYNTAX ANALYSIS");
        System.out.println("=======================");
        
        CharStream charStream = CharStreams.fromString(input);
        cool_lex lexer = new cool_lex(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cool_synParser parser = new cool_synParser(tokens);
        
        // Add error listener to capture syntax errors
        parser.removeErrorListeners();
        SyntaxErrorCollector errorCollector = new SyntaxErrorCollector();
        parser.addErrorListener(errorCollector);
        
        ParseTree parseTree = parser.program();
        
        if (errorCollector.hasErrors()) {
            System.out.println("Parsing failed with syntax errors:");
            List<String> errors = errorCollector.getErrors();
            for (String error : errors) {
                System.out.println("  - " + error);
            }
            System.out.println("\nCompilation stopped due to syntax errors.");
            return;
        }
        
        System.out.println("Parsing completed successfully.");
        System.out.println();
        
        // Build AST
        System.out.println("STAGE 3: AST CONSTRUCTION");
        System.out.println("========================");
        ASTBuilder astBuilder = new ASTBuilder();
        ASTNode ast = astBuilder.visit(parseTree);
        
        if (ast == null) {
            System.err.println("Failed to build AST.");
            System.out.println("\nCompilation stopped due to AST construction failure.");
            return;
        }
        
        if (!(ast instanceof ProgramNode)) {
            System.err.println("Expected ProgramNode at the root of AST.");
            System.out.println("\nCompilation stopped due to invalid AST structure.");
            return;
        }
        
        System.out.println("AST construction successful.");
        System.out.println();
        
        // Perform semantic analysis
        System.out.println("STAGE 4: SEMANTIC ANALYSIS");
        System.out.println("=========================");
        
        ProgramNode programNode = (ProgramNode) ast;
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        List<SemanticError> semanticErrors = analyzer.analyze(programNode);
        
        if (semanticErrors.isEmpty()) {
            System.out.println("No semantic errors found.");
            System.out.println("Semantic analysis completed successfully.");
        } else {
            System.out.println("Semantic errors found:");
            for (SemanticError error : semanticErrors) {
                System.out.println("  - " + error);
            }
            System.out.println("\nCompilation stopped due to semantic errors.");
            return;
        }
        
        // Add IR generation stage
        System.out.println("\nSTAGE 5: IR GENERATION");
        System.out.println("=====================");
        
        if (semanticErrors.isEmpty()) {
            // Generate IR
            IRGenerator irGenerator = new IRGenerator();
            List<TACInstruction> instructions = irGenerator.generate(programNode);
            
            System.out.println("Generated IR (first 20 instructions):");
            int count = 0;
            for (TACInstruction instr : instructions) {
                System.out.println(instr);
                count++;
                if (count >= 20) {
                    System.out.println("... (additional instructions not shown)");
                    break;
                }
            }
            System.out.println("\nIR generation completed successfully.");
        } else {
            System.out.println("IR Generation skipped due to semantic errors.");
        }
    }
    
    /**
     * Helper class to collect syntax errors during parsing.
     */
    private static class SyntaxErrorCollector extends BaseErrorListener {
        private final List<String> errors = new java.util.ArrayList<>();
        
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
                               int line, int charPositionInLine, 
                               String msg, RecognitionException e) {
            String error = String.format("Line %d:%d - %s", line, charPositionInLine, msg);
            errors.add(error);
        }
        
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
        
        public List<String> getErrors() {
            return errors;
        }
    }
}
