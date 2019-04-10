package com.cjburkey.toylang.lang;

import com.cjburkey.toylang.lang.expression.FuncVal;
import com.cjburkey.toylang.lang.statement.VariableDec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Map;

public class ScopeContainer {

    public final ObjectArrayList<IStatement> statements = new ObjectArrayList<>();
    public final Object2ObjectOpenHashMap<String, VariableDec> variables = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, FuncVal> functions = new Object2ObjectOpenHashMap<>();
    public IScope visibleParentScope;

    public String toString() {
        return statements.toString();
    }

    public Map<String, VariableDec> variables() {
        return variables;
    }

    public Map<String, FuncVal> functions() {
        return functions;
    }

    public void createVar(VariableDec variableDec) {
        variables.put(variableDec.name, variableDec);
    }

}
