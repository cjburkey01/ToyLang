package com.cjburkey.toylang.lang;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by CJ Burkey on 2019/02/02
 */
public enum Operator {
    
    // Math
    ADD("+", ((a, b) -> ((Double) a + (Double) b))),
    SUB("-", ((a) -> -((Double) a)), ((a, b) -> ((Double) a - (Double) b))),
    MUL("*", ((a, b) -> ((Double) a * (Double) b))),
    DIV("/", ((a, b) -> ((Double) a / (Double) b))),
    
    // Comparison
    GT(">", ((a, b) -> ((Double) a > (Double) b))),
    LT("<", ((a, b) -> ((Double) a < (Double) b))),
    GTE(">=", ((a, b) -> ((Double) a >= (Double) b))),
    LTE("<=", ((a, b) -> ((Double) a <= (Double) b))),
    ISEQ("==", Object::equals),
    NOTEQ("!=", ((a, b) -> !a.equals(b))),
    
    ;
    
    public final String token;
    public final Function<Object, Object> unaryOperation;
    public final BiFunction<Object, Object, Object> binaryOperation;

    Operator(String token, Function<Object, Object> unaryOperation, BiFunction<Object, Object, Object> binaryOperation) {
        this.token = token;
        this.unaryOperation = unaryOperation;
        this.binaryOperation = binaryOperation;
    }

    Operator(String token, Function<Object, Object> unaryOperation) {
        this(token, unaryOperation, null);
    }

    Operator(String token, BiFunction<Object, Object, Object> binaryOperation) {
        this(token, null, binaryOperation);
    }
    
    public static Operator get(String token) {
        for (Operator operator : values()) {
            if (operator.token.equals(token)) return operator;
        }
        return null;
    }
    
}
