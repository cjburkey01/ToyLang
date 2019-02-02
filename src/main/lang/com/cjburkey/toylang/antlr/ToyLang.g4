grammar ToyLang;

@header {
    package com.cjburkey.toylang.antlr;
}

// -- TOKENS -- //

// Ignore comments and whitespace
COMMENT         : '//' (~'\n')* -> skip ;
WS              : [ \n\r\t]+ -> skip ;
STRING          : '"' (~('\n' | '"'))*? '"' ;

// Simple symbols
PLUS            : '+' ;
MINUS           : '-' ;
TIMES           : '*' ;
DIV             : '/' ;
LPAR            : '(' ;
RPAR            : ')' ;
SEMI            : ';' ;
COMMA           : ',' ;
LBR             : '{' ;
RBR             : '}' ;
GTE             : '>=' ;
LTE             : '<=' ;
GT              : '>' ;
LT              : '<' ;
ISEQ            : '==' ;
NOTEQ           : '!=' ;
EQUAL           : '=' ;

// Key words
LET             : 'let' ;
OF              : 'of' ;
RETURN          : 'return' ;
SELF            : ':self' ;
IF              : 'if' ;
ELSE            : 'else' ;

// Literals
fragment DIGIT  : [0-9] ;
INTEGER         : DIGIT+ ;
FLOAT           : INTEGER? '.' INTEGER+ ;
IDENTIFIER      : [A-Za-z_] [A-Za-z0-9_]* ;

// -- RULES -- //

variableName    : IDENTIFIER ;

typeName        : IDENTIFIER ;

parameter       : variableName OF typeName ;

parameters      : parameters COMMA parameter
                | parameter
                ;

arguments       : arguments COMMA expression
                | expression
                ;

ifStatement     : IF expression LBR statement* RBR
                | ELSE expression? LBR statement* RBR
                ;

expression      : FLOAT                                                             # Float
                | INTEGER                                                           # Int
                | STRING                                                            # String
                
                | variableName                                                      # VarRef
                | (variableName | SELF) LPAR arguments? RPAR                        # FuncRef
                | LPAR parameters? RPAR (OF typeName)? LBR statement* RBR           # Func
                | ifStatement                                                       # If
                
                | LPAR expression RPAR                                              # Par
                | op=MINUS expression                                               # Neg
                | expression op=(TIMES | DIV) expression                            # MulDiv
                | expression op=(PLUS | MINUS) expression                           # AddSub
                | expression op=(GTE | LTE | GT | LT | ISEQ | NOTEQ) expression     # Compare
                ;

variableDec     : LET variableName (OF typeName)? EQUAL expression ;

statement       : variableDec SEMI          # VarDec
                | ifStatement               # IfState
                | expression SEMI           # Expr
                | RETURN expression SEMI    # Return
                | expression                # Return
                ;

program         : statement* EOF ;
