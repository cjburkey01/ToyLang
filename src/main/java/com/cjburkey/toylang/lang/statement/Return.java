package com.cjburkey.toylang.lang.statement;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;
import com.cjburkey.toylang.lang.IReturn;
import com.cjburkey.toylang.lang.IStatement;

/**
 * Created by CJ Burkey on 2019/02/01
 */
public class Return implements IReturn, IStatement {

    public IExpression value;

    public Return(IExpression value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Return: {" + value + '}';
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

    @Override
    public IExpression getValue() {
        return value;
    }
    
}
