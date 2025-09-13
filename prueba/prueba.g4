grammar prueba;

// ----------- TOKENS (LÉXICO) -----------

// keywords
IF      : 'if' ;
THEN    : 'then';
ELSE    : 'else';
WHILE   : 'while';
DO      : 'do';
LET     : 'let';
IN      : 'in';
BEGIN   : 'begin';
END     : 'end';
CONST   : 'const';
VAR     : 'var';

// symbols
PyCOMA      : ';';
ASSIGN      : ':=';
PIZQ        : '(';
PDER        : ')';
VIR         : '~';
DOSPUNT     : ':';
SUM         : '+';
SUB         : '-';
MUL         : '*';
DIV         : '/';
MOD         : '%';
EQEQ        : '==';
NOTEQ       : '!=';
MENOR       : '<';
MAYOR       : '>';
MENOREQ     : '<=';
MAYOREQ     : '>=';

// identifiers & literals
ID      : [a-zA-Z] [a-zA-Z0-9]* ;
LITERAL : [0-9]+ ;

// comments & whitespace
LINE_COMMENT : '//' .*? '\r'? '\n' -> skip ;
WS           : [ \t\n\r]+ -> skip ;

// ----------- PARSER (SINTAXIS) -----------

program
    : singleCommand EOF
    ;

command
    : singleCommand (SEMI singleCommand)*
    ;

singleCommand
    : ID ASSIGN expression
    | ID LPAREN expression RPAREN
    | IF expression THEN singleCommand (ELSE singleCommand)?
    | WHILE expression DO singleCommand
    | LET declaration IN singleCommand
    | BEGIN command END
    ;

declaration
    : singleDeclaration (SEMI singleDeclaration)*
    ;

singleDeclaration
    : CONST ID EQ expression
    | VAR ID COLON typeDenoter
    ;

typeDenoter
    : ID
    ;

expression
    : primaryExpression (operator primaryExpression)*
    ;

primaryExpression
    : LITERAL
    | ID
    | LPAREN expression RPAREN
    ;

operator
    : PLUS | MINUS | MUL | DIV | MOD
    | EQEQ | NOTEQ | LT | GT | LTEQ | GTEQ
    ;
