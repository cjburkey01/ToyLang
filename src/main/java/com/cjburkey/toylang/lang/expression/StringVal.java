package com.cjburkey.toylang.lang.expression;

public class StringVal extends Expression {
    
    public String value;
    
    public StringVal(String value) {
        this.value = value;
    }
    
    public String getType() {
        return "String";
    }
    
    public String toString() {
        return "\"" + value + "\"";
    }
    
}
