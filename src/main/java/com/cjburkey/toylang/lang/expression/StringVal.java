package com.cjburkey.toylang.lang.expression;

public class StringVal extends Expression {
    
    public String value;
    
    public StringVal(String value) {
        this.value = value;
    }
    
    @Override
    public String getType() {
        return "String";
    }
    
    @Override
    public String toString() {
        return "String: \"" + value + "\"";
    }
    
}
