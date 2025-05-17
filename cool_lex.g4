lexer grammar cool_lex;

// Keywords with case-insensitivity
CLASS : [Cc][Ll][Aa][Ss][Ss];
INHERITS : [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss];
IF : [Ii][Ff];
THEN : [Tt][Hh][Ee][Nn];
ELSE : [Ee][Ll][Ss][Ee];
FI : [Ff][Ii];
WHILE : [Ww][Hh][Ii][Ll][Ee];
LOOP : [Ll][Oo][Oo][Pp];
POOL : [Pp][Oo][Oo][Ll];
LET : [Ll][Ee][Tt];
IN : [Ii][Nn];
CASE : [Cc][Aa][Ss][Ee];
ESAC : [Ee][Ss][Aa][Cc];
OF : [Oo][Ff];
NEW : [Nn][Ee][Ww];
ISVOID : [Ii][Ss][Vv][Oo][Ii][Dd];
NOT : [Nn][Oo][Tt];

// Boolean literals with special casing rules
TRUE : [t][Rr][Uu][Ee];
FALSE : [f][Aa][Ll][Ss][Ee];

// Integer literals
INTEGERS: [0-9]+;
FLOAT: [0-9]+'.'[0-9]*;// Special identifiers with semantic actions
SELF: 'self' {setType(OBJECT_IDENTIFIER);};
SELF_TYPE: 'SELF_TYPE' {setType(TYPE_IDENTIFIER);};

// Identifiers
TYPE_IDENTIFIER: SELF_TYPE | [A-Z][a-zA-Z0-9_]*;
OBJECT_IDENTIFIER: SELF | [a-z][a-zA-Z0-9_]*;

// Whitespace handling (prioritized early)
WS : [ \t\r\n\f]+ -> skip;

// Delimiters and punctuation
LPAREN : '(';
RPAREN : ')';
SEMI : ';';
COLON : ':';
ATSYM : '@';
COMMA : ',';

// Arithmetic operators
PLUS_OP : '+';
MINUS_OP : '-';
MULT_OP : '*';
DIV_OP : '/';
TILDE_OP : '~';

// Relational operators - order is important for multi-character operators
LE_OP : '<=';
LT_OP : '<';
GT_OP : '>';
GE_OP : '>=';
EQUALS_OP : '=';

// Assignment operator
ASSIGN_OP : '<-';

// Case branch operator
RES : '=>';

// Block delimiters
LBRACE : '{';
RBRACE : '}';
DOT_OP : '.';

// String literals with escape sequence support
STRING: '"' (('\\' [btnrf"\\]) | ~('\\'|'\r'|'\n'|'"'))* '"';

// Comments
SINGLECOMMENT: '--' ~[\r\n]* -> skip;
COMMENT: '(*'-> pushMode(ININCOM), skip;

// Support for nested multiline comments
mode ININCOM;
OCOMMENT: '(*' -> pushMode(ININCOM), skip;
CCOMMENT: '*)' -> popMode, skip;
INCOMMENT_T: . -> skip;
