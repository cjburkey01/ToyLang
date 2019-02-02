package com.cjburkey.toylang.lang.statement;

import com.cjburkey.toylang.lang.Statement;
import com.cjburkey.toylang.lang.expression.Expression;

public class VariableDec extends Statement {
    
    public String name;
    public String type;
    public Expression value;
    
    public VariableDec(String name, String type, Expression value) {
        this.name = name;
        this.type = type;
        this.value = value;
        
        if (type == null && value != null) {
            this.type = value.getType();
        }
    }
    
    public VariableDec(String name, String type) {
        this(name, type, null);
    }
    
    @Override
    public String toString() {
        return String.format("Define variable \"%s\" as {%s}", name, value);
    }
    
}
