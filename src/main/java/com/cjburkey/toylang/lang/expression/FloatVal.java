package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;

public class FloatVal implements IExpression<Double> {

    public double value;

    public FloatVal(double value) {
        this.value = value;
    }

    @Override
    public Class<Double> getClassType() {
        return Double.class;
    }

    @Override
    public String getType() {
        return "Number";
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Number: %.2f", value);
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
