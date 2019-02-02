package com.cjburkey.toylang.lang;

public class Parameter {
    
    public String name;
    public String type;
    
    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public String toString() {
        return name + " of " + type;
    }
    
}
