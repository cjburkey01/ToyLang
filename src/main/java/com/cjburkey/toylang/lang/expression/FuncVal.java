package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.lang.Parameter;
import com.cjburkey.toylang.lang.ScopeContainer;
import com.cjburkey.toylang.lang.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class FuncVal extends Expression {
    
    public String type;
    public final ArrayList<Parameter> parameters = new ArrayList<>();
    public final ScopeContainer scope = new ScopeContainer();
    
    public FuncVal(String type, Collection<Parameter> parameters, Collection<Statement> statements) {
        this.type = type;
        if (parameters != null) this.parameters.addAll(parameters);
        if (statements != null) scope.statements.addAll(statements);
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Function of (");
        for (Parameter parameter : parameters) {
            output.append(parameter.type);
            output.append(", ");
        }
        if (parameters.size() > 0) output.setLength(output.length() - 2);
        output.append(") returns ");
        output.append(type);
        return output.toString();
    }
    
}
