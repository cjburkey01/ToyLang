package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.IExpression;
import com.cjburkey.toylang.lang.IScope;
import com.cjburkey.toylang.lang.IStatement;
import com.cjburkey.toylang.lang.Parameter;
import com.cjburkey.toylang.lang.ScopeContainer;
import java.util.ArrayList;
import java.util.List;

public class FuncVal implements IScope, IExpression<Object> {
    
    public String type;
    public final ArrayList<Parameter> parameters = new ArrayList<>();
    private final ScopeContainer scope = new ScopeContainer();

    public FuncVal(String type, List<Parameter> parameters, List<IStatement> statements) {
        this.type = (type == null) ? "Void" : type;
        if (parameters != null) this.parameters.addAll(parameters);
        if (statements != null) scope.statements.addAll(statements);
    }

    @Override
    public Class<Object> getClassType() {
        return Object.class;
    }
    
    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getValue() {
        // TODO
        return null;
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
    public ScopeContainer scope() {
        return scope;
    }
    
}
