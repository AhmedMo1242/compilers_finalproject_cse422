# COOL Language Compiler

## Project Overview
This project implements a compiler for the COOL (Classroom Object-Oriented Language) programming language. The compiler follows the standard compilation pipeline including lexical analysis, syntax analysis, abstract syntax tree construction, semantic analysis, and intermediate code generation.

## Compiler Pipeline

| Stage | Description | Tools Used |
|-------|-------------|-----------|
| 1. Lexical Analysis | Tokenize input source code | ANTLR4 (`cool_lex.g4`) |
| 2. Syntax Analysis | Build parse tree from tokens | ANTLR4 Parser (`cool_syn.g4`) |
| 3. AST Construction | Convert parse tree into Abstract Syntax Tree | Java + ANTLR Visitor Pattern |
| 4. Semantic Analysis | Type checking, scope resolution, symbol table management, inheritance verification | Java (`SemanticAnalyzer.java`) |
| 5. IR Generation | Generate Three-Address Code (TAC) representation | Java (`IRGenerator.java`) |

## Features

- **Lexical Analyzer**: Handles COOL language tokens including keywords, identifiers, literals, and operators with proper case-sensitivity rules
- **Parser**: Implements COOL grammar with appropriate precedence rules and language constructs
- **Abstract Syntax Tree**: Custom AST implementation with visitor pattern for semantic analysis
- **Symbol Table**: Tracks classes, methods, variables, and scopes to support semantic analysis
- **Type Checker**: Full type checking system with inheritance verification
- **Intermediate Code**: Three-Address Code (TAC) representation with support for assignments, operations, jumps, and method calls

## Running the Compiler

The `Main.java` class provides a comprehensive testing framework with support for:
- Testing individual compilation phases (lexer, parser, AST, semantic, IR)
- Running the full compilation pipeline on COOL source files
- Displaying detailed output from each compilation phase

## Project Structure

- `src/semantic/`: Semantic analyzer implementation
- `src/ast/`: Abstract Syntax Tree definitions
- `src/ir/`: Intermediate Representation generation
- `cool_lex.g4`: ANTLR4 lexer grammar
- `cool_syn.g4`: ANTLR4 parser grammar
- `testcases/`: Test cases for different compiler phases

## Submission Information

This project is submitted to:
- **Prof. Ahmed Fares** (Course Instructor)
- **Eng. Esraa Abdelrazek** (Teaching Assistant) 
- **Eng. Sara Helal** (Teaching Assistant)

As part of the requirements for CSE 422 Programming Languages and Compilers (Spring 2025).