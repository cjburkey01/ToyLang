package com.cjburkey.toylang.lang.expression;

public class UnaryOperator extends Expression {
    
    public Operator operator;
    public Expression value;
    
    public UnaryOperator(Operator operator, Expression value) {
        this.operator = operator;
        this.value = value;
    }
    
    public String getType() {
        return value.getType();
    }
    
    public String toString() {
        return operator.token + value;
    }
    
}
