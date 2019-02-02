package com.cjburkey.toylang.lang.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by CJ Burkey on 2019/02/01
 */
public class FuncRef extends Expression {
    
    public String name;
    public final List<Expression> arguments = new ArrayList<>();
    
    public FuncRef(String name, Collection<Expression> arguments) {
        this.name = name;
        if (arguments != null) this.arguments.addAll(arguments);
    }
    
    public String getType() {
        return "Function";
    }
    
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
        for (Expression argument : arguments) {
            output.append('{');
            output.append(argument.toString());
            output.append("}, ");
        }
        if (arguments.size() > 0) output.setLength(output.length() - 2);
        output.append(')');
        return output.toString();
    }
    
}
