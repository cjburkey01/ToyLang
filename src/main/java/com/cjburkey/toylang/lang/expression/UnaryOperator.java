package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.lang.Operator;

public class UnaryOperator extends Expression {
    
    public Operator operator;
    public Expression value;
    
    public UnaryOperator(Operator operator, Expression value) {
        this.operator = operator;
        this.value = value;
    }
    
    @Override
    public String getType() {
        return value.getType();
    }
    
    @Override
    public String toString() {
        return operator.token + value;
    }
    
}
