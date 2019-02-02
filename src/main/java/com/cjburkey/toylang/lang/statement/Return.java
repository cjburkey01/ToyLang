package com.cjburkey.toylang.lang.statement;

import com.cjburkey.toylang.lang.Statement;
import com.cjburkey.toylang.lang.expression.Expression;

/**
 * Created by CJ Burkey on 2019/02/01
 */
public class Return extends Statement {
    
    public Expression value;
    
    public Return(Expression value) {
        this.value = value;
    }
    
    public String toString() {
        return "Return: {" + value + '}';
    }
    
}
