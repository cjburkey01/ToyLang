package com.cjburkey.toylang.lang;

import com.cjburkey.toylang.ToyLangError;

public interface IStatement {

    String toString();

    ToyLangError errorCheck();

    ToyLangError execute();

}
