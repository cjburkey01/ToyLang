package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;
import com.cjburkey.toylang.lang.Operator;

public class BinaryOperator implements IExpression<Object> {

    public Operator operator;
    public IExpression<?> valueA;
    public IExpression<?> valueB;

    private Object evaluatedValue = null;

    public BinaryOperator(Operator operator, IExpression<?> valueA, IExpression<?> valueB) {
        this.operator = operator;
        this.valueA = valueA;
        this.valueB = valueB;
    }

    @Override
    public Class<Object> getClassType() {
        return Object.class;
    }

    @Override
    public String getType() {
        return valueA.getType();
    }

    @Override
    public Object getValue() {
        return evaluatedValue;
    }

    @Override
    public String toString() {
        return "{" + valueA + "} " + operator.token + " {" + valueB + "}";
    }

    @Override
    public ToyLangError errorCheck() {
        // Ensure the operator exists
        if (operator == null) return new ToyLangError("Invalid binary operator");

        // Ensure the operator has a binary function
        if (operator.binaryOperation == null)
            return new ToyLangError("The binary operator \"%s\" does not have a binary function", operator.token);

        // Check if the values are present
        if (valueA == null || valueB == null) {
            boolean missingA = valueA == null;
            boolean missingB = valueB == null;
            return new ToyLangError("Binary operation missing %s value%s",
                    ((missingA && missingB) ? "both" : ("the " + (missingA ? "first" : "second"))),
                    ((missingA && missingB) ? "s" : ""));
        }

        // Check if the values have errors
        ToyLangError error;
        if ((error = valueA.errorCheck()) != null) return error;
        if ((error = valueB.errorCheck()) != null) return error;

        // Check if the types of the values differ
        if (!valueA.getType().equals(valueB.getType())) {
            return new ToyLangError("Binary operation type mismatch between first value of type \"%s\" and second value of type \"%s\"",
                    valueA.getType(),
                    valueB.getType());
        }

        // TODO: CHECK IF TYPES CAN BE OPERATED UPON BY THIS OPERATOR
        return new ToyLangError("Unimplemented error detection path");

        // No errors
//        return null;
    }

    @Override
    public ToyLangError execute() {
        // Execute the two values to generate their values
        ToyLangError error;
        if ((error = valueA.execute()) != null) return error;
        if ((error = valueB.execute()) != null) return error;

        // Try to generate a value from the operator
        evaluatedValue = operator.binaryOperation.apply(valueA.getValue(), valueB.getValue());

        // Ensure the value is valid
        if (evaluatedValue == null) {
            return new ToyLangError("Error in binary operation of \"%s\" on {%s} and {%s}",
                    operator.token,
                    valueA,
                    valueB);
        }

        // No errors = muy bueno
        return null;
    }

}
