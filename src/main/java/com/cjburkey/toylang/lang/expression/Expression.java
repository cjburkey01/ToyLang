package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.lang.Statement;

public abstract class Expression extends Statement {
    
    public abstract String getType();
    
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
