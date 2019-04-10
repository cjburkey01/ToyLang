package com.cjburkey.toylang.lang;

import com.cjburkey.toylang.lang.expression.FuncVal;
import com.cjburkey.toylang.lang.statement.VariableDec;
import java.util.Map;

/**
 * Created by CJ Burkey on 2019/02/02
 */
public interface IScope {

    ScopeContainer scope();

    default IScope visibleParentScope() {
        return scope().visibleParentScope;
    }

    default Map<String, VariableDec> variables() {
        return scope().variables;
    }

    default Map<String, FuncVal> functions() {
        return scope().functions;
    }

    default void createVar(VariableDec variable) {
        variables().put(variable.name, variable);
    }

}
