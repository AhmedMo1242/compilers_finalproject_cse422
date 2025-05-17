grammar cool_syn;

options {
  tokenVocab = cool_lex;
}

// Program structure
program : class+  # start;
class   : CLASS TYPE_IDENTIFIER (INHERITS TYPE_IDENTIFIER)? LBRACE (feature SEMI)* RBRACE SEMI # classdef;

// Features: methods and attributes
feature : OBJECT_IDENTIFIER LPAREN (formal (COMMA formal)*)? RPAREN COLON TYPE_IDENTIFIER LBRACE expr RBRACE # function
        | OBJECT_IDENTIFIER COLON TYPE_IDENTIFIER (ASSIGN_OP expr)? # variable;
formal  : OBJECT_IDENTIFIER COLON TYPE_IDENTIFIER # parameter;

// Expressions with proper precedence ordering (highest to lowest)
expr    : LPAREN expr RPAREN # parexpr
        | OBJECT_IDENTIFIER # id
        | INTEGERS # num
        | STRING # text
        | TRUE # true
        | FALSE # false
        | NEW TYPE_IDENTIFIER # newobject
        | ISVOID expr # void
        | TILDE_OP expr # invert
        | NOT expr # not
        | expr MULT_OP expr # mul
        | expr DIV_OP expr # div
        | expr PLUS_OP expr # add
        | expr MINUS_OP expr # sub
        | expr LT_OP expr # lt
        | expr LE_OP expr # lq
        | expr GT_OP expr # gt
        | expr GE_OP expr # gq
        | expr EQUALS_OP expr # eq
        | IF expr THEN expr ELSE expr FI # if
        | WHILE expr LOOP expr POOL # while
        | LBRACE (expr SEMI)+ RBRACE # block
        | LET OBJECT_IDENTIFIER COLON TYPE_IDENTIFIER (ASSIGN_OP expr)? 
          (COMMA OBJECT_IDENTIFIER COLON TYPE_IDENTIFIER (ASSIGN_OP expr)?)* IN expr # let
        | CASE expr OF (OBJECT_IDENTIFIER COLON TYPE_IDENTIFIER RES expr SEMI)+ ESAC # case
        | expr (ATSYM TYPE_IDENTIFIER)? DOT_OP OBJECT_IDENTIFIER LPAREN (expr (COMMA expr)*)? RPAREN # objectcall
        | OBJECT_IDENTIFIER LPAREN (expr (COMMA expr)*)? RPAREN # staticcall
        | OBJECT_IDENTIFIER ASSIGN_OP expr # assign
        ; 