package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;

/**
 * Created by CJ Burkey on 2019/02/01
 */
public class VarRef implements IExpression<Object> {
    
    public String variableName;
    
    public VarRef(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public Class<Object> getClassType() {
        return Object.class;
    }

    @Override
    public String getType() {
        // TODO
        return null;
    }

    @Override
    public Object getValue() {
        // TODO
        return null;
    }

    @Override
    public String toString() {
        return "Reference to \"" + variableName + "\"";
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
