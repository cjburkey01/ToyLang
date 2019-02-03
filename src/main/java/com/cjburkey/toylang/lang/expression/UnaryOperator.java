package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;
import com.cjburkey.toylang.lang.Operator;

public class UnaryOperator implements IExpression<Object> {
    
    public Operator operator;
    public IExpression value;

    private Object evaluatedValue = null;

    public UnaryOperator(Operator operator, IExpression value) {
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Class<Object> getClassType() {
        return Object.class;
    }
    
    @Override
    public String getType() {
        return value.getType();
    }

    @Override
    public Object getValue() {
        return evaluatedValue;
    }
    
    @Override
    public String toString() {
        return operator.token + value;
    }

    @Override
    public ToyLangError errorCheck() {
        // Ensure the operator exists
        if (operator == null) return new ToyLangError("Invalid unary operator");

        // Ensure the operator has a binary function
        if (operator.unaryOperation == null)
            return new ToyLangError("The unary operator \"%s\" does not have a unary function", operator.token);

        // Check if the values are present
        if (value == null) {
            return new ToyLangError("Unary operation missing value");
        }

        // Check if the values have errors
        ToyLangError error;
        if ((error = value.errorCheck()) != null) return error;

        // TODO: CHECK IF TYPE CAN BE OPERATED UPON BY THIS OPERATOR

        // No errors
        return null;
    }

    @Override
    public ToyLangError execute() {
        // Execute the two values to generate their values
        ToyLangError error;
        if ((error = value.execute()) != null) return error;

        // Try to generate a value from the operator
        evaluatedValue = operator.unaryOperation.apply(value.getValue());

        // Ensure the value is valid
        if (evaluatedValue == null) {
            return new ToyLangError("Error in unary operation of \"%s\" on {%s}",
                    operator.token,
                    value);
        }

        // No errors = muy bueno
        return null;
    }
    
}
