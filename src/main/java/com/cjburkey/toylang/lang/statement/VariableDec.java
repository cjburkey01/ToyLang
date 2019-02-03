package com.cjburkey.toylang.lang.statement;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;
import com.cjburkey.toylang.lang.IStatement;

public class VariableDec implements IStatement {
    
    public String name;
    public String type;
    public IExpression value;

    public VariableDec(String name, String type, IExpression value) {
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

    @Override
    public ToyLangError errorCheck() {
        // TODO
        return null;
    }

    @Override
    public ToyLangError execute() {
        // TODO
        return null;
    }
    
}
