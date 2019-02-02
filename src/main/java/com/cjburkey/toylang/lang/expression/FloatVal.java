package com.cjburkey.toylang.lang.expression;

public class FloatVal extends Expression {
    
    public float value;
    
    public FloatVal(float value) {
        this.value = value;
    }
    
    public String getType() {
        return "Number";
    }
    
    public String toString() {
        return "Number: " + value;
    }
    
}
