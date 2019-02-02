grammar ToyLang;

@header {
    package com.cjburkey.toylang.antlr;
}

// -- TOKENS -- //

// Ignore comments and whitespace
COMMENT         : '//' (~'\n')*? '\n' -> skip ;
WS              : [ \n\r\t]+ -> skip ;

PLUS            : '+' ;
MINUS           : '-' ;
TIMES           : '*' ;
DIV             : '/' ;
LPAR            : '(' ;
RPAR            : ')' ;

LET             : 'let' ;
EQUAL           : '=' ;
OF              : 'of' ;
SEMI            : ';' ;
COMMA           : ',' ;
LBR             : '{' ;
RBR             : '}' ;

fragment DIGIT  : [0-9] ;
INTEGER         : DIGIT+ ;
FLOAT           : INTEGER? '.' INTEGER+ ;
IDENTIFIER      : [A-Za-z_] [A-Za-z0-9_]* ;

// -- RULES -- //

variableName    : IDENTIFIER ;

typeName        : IDENTIFIER ;

parameter       : variableName OF typeName ;

parameters      : parameter COMMA parameters
                | parameter
                ;

expression      : FLOAT                                                     # Float
                | INTEGER                                                   # Int
                | LPAR parameters? RPAR OF typeName LBR statement* RBR      # Func
                | LPAR expression RPAR                                      # Par
                | MINUS expression                                          # Neg
                | expression op=(TIMES | DIV) expression                    # MulDiv
                | expression op=(PLUS | MINUS) expression                   # AddSub
                ;

variableDec     : LET variableName OF typeName (EQUAL expression)? ;

statement       : variableDec SEMI      # VarDec
                | expression SEMI       # Expr
                ;

program         : statement* EOF ;
