package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;

public class StringVal implements IExpression<String> {

    public String value;

    public StringVal(String value) {
        this.value = value;
    }

    @Override
    public Class<String> getClassType() {
        return String.class;
    }

    @Override
    public String getType() {
        return "String";
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "String: \"" + value + "\"";
    }

    @Override
    public ToyLangError errorCheck() {
        return null;
    }

    @Override
    public ToyLangError execute() {
        return null;
    }

}
