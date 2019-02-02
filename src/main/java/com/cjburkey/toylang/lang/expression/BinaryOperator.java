package com.cjburkey.toylang.lang.expression;

public class BinaryOperator extends Expression {
    
    public Operator operator;
    public Expression valueA;
    public Expression valueB;
    
    public BinaryOperator(Operator operator, Expression valueA, Expression valueB) {
        this.operator = operator;
        this.valueA = valueA;
        this.valueB = valueB;
    }
    
    @Override
    public String getType() {
        return valueA.getType();
    }
    
    @Override
    public String toString() {
        return "{" + valueA + "} " + operator.token + " {" + valueB + "}";
    }
    
}
