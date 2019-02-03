package com.cjburkey.toylang.lang;

import java.util.ArrayList;
import java.util.Objects;

public class ScopeContainer {

    public final ArrayList<IStatement> statements = new ArrayList<>();

    public String toString() {
        return statements.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScopeContainer that = (ScopeContainer) o;
        return statements.equals(that.statements);
    }

    public int hashCode() {
        return Objects.hash(statements);
    }
    
}
