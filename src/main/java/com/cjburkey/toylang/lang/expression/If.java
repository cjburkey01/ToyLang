package com.cjburkey.toylang.lang.expression;

import com.cjburkey.toylang.ToyLangError;
import com.cjburkey.toylang.lang.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ Burkey on 2019/02/02
 */
public class If implements IScope, IExpression<Object> {

    public final ScopeContainer scope = new ScopeContainer();
    public final List<If> elseBranches = new ArrayList<>();
    public boolean isExpression = false;
    public boolean isElse;
    public IExpression<?> condition;
    private String type = null;

    public If(boolean isElse, IExpression<?> condition, List<IStatement> scope, List<If> elseBranches) {
        this.isElse = isElse;
        this.condition = condition;
        if (scope != null) this.scope.statements.addAll(scope);
        if (elseBranches != null) this.elseBranches.addAll(elseBranches);

        determineType();
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

    public void determineType() {
        if (!isExpression) {
            type = "Void";
            return;
        }
        for (IStatement statement : scope.statements) {
            if (IReturn.class.isAssignableFrom(statement.getClass())) {
                IExpression<?> value = ((IReturn) statement).getValue();
                type = ((value == null) ? null : value.getType());
                return;
            }
        }
        type = "Void";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (isElse) {
            builder.append("Else ");
        }
        if (!isElse || condition != null) {
            builder.append("If ");
        }
        if (condition != null) {
            builder.append('{');
            builder.append(condition);
            builder.append("} ");
        }
        builder.append("Then ");
        builder.append(scope);
        if (isExpression && !type.equals("Void")) {
            builder.append(" and return (");
            builder.append(type);
            builder.append(')');
        }
        elseBranches.forEach(elseBranch -> {
            builder.append(' ');
            builder.append(elseBranch);
        });
        return builder.toString();
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
