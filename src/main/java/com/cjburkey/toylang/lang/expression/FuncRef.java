package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2019/02/01
 */
public class FuncRef implements IExpression<Object> {

    public final List<IExpression<?>> arguments = new ArrayList<>();
    public String name;

    public FuncRef(String name, List<IExpression<?>> arguments) {
        this.name = name;
        if (arguments != null) this.arguments.addAll(arguments);
    }

    @Override
    public Class<Object> getClassType() {
        return Object.class;
    }

    @Override
    public String getType() {
        return "Function";
    }

    @Override
    public Object getValue() {
        // TODO
        return null;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        if (name == null) {
            output.append("Recursive function reference");
        } else {
            output.append("Function reference to \"");
            output.append(name);
            output.append('\"');
        }

        output.append(" with arguments: (");
        for (IExpression<?> argument : arguments) {
            output.append('{');
            output.append(argument.toString());
            output.append("}, ");
        }
        if (arguments.size() > 0) output.setLength(output.length() - 2);
        output.append(')');
        return output.toString();
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
