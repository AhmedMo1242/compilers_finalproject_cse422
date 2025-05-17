import ast.ASTBuilder;
import ast.ASTNode;
import ast.ProgramNode;
import compiler.cool_lex;
import compiler.cool_synParser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        
        // Otherwise, run tests on all test files organized by category
        try {
            runAllLexerTests();
            runAllParserTests();
            runAllASTTests();
            
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
    
    private static void runFullCompiler(String input) {
        // First run lexer
        System.out.println("\nSTAGE 1: LEXICAL ANALYSIS");
        System.out.println("=========================");
        testLexer(input);
        System.out.println();
        
        // Then parse
        System.out.println("STAGE 2: SYNTAX ANALYSIS");
        System.out.println("=======================");
        testParser(input);
        System.out.println();
        
        // Finally build AST
        System.out.println("STAGE 3: AST CONSTRUCTION");
        System.out.println("========================");
        testAst(input);
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

