package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.lang.Parameter;
import com.cjburkey.toylang.lang.ScopeContainer;
import com.cjburkey.toylang.lang.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class FuncVal extends Expression {
    
    public String type;
    public final ArrayList<Parameter> parameters = new ArrayList<>();
    public ScopeContainer scope;
    
    public FuncVal(String type, Collection<Parameter> parameters, Collection<Statement> statements) {
        this.type = type;
        this.parameters.addAll(parameters);
        scope.statements.addAll(statements);
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "Function: ";
    }
    
}
