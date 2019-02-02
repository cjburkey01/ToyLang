package com.cjburkey.toylang.lang.expression;

public abstract class Expression {
    
    public abstract String getType();
    public abstract String toString();
    
    public enum Operator {
        ADD("+"),
        SUB("-"),
        MUL("*"),
        DIV("/"),
        NEG("-"),
        
        ;
        
        public final String token;
        Operator(String token) {
            this.token = token;
        }
    }
    
}
