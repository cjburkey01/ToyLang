package com.cjburkey.toylang.lang;

/**
 * Created by CJ Burkey on 2019/02/02
 */
public enum Operator {
    
    // Math
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    
    // Comparison
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    ISEQ("=="),
    NOTEQ("!="),
    
    ;
    
    public final String token;
    Operator(String token) {
        this.token = token;
    }
    
    public static Operator get(String token) {
        for (Operator operator : values()) {
            if (operator.token.equals(token)) return operator;
        }
        return null;
    }
    
}
