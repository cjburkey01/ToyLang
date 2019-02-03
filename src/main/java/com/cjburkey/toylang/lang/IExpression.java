package com.cjburkey.toylang.lang;

public interface IExpression<T> extends IStatement {

    Class<T> getClassType();

    String getType();

    T getValue();

}
