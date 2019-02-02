package com.cjburkey.toylang.lang.expression;

/**
 * Created by CJ Burkey on 2019/02/01
 */
public class VarRef extends Expression {
    
    public String variableName;
    
    public VarRef(String variableName) {
        this.variableName = variableName;
    }
    
    public String getType() {
        return null;    // TODO: DETECT
    }
    
    public String toString() {
        return "Reference to \"" + variableName + "\"";
    }
    
}
