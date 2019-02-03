package com.cjburkey.toylang;

/**
 * Created by CJ Burkey on 2019/02/02
 */
public class ToyLangError {

    public final String msg;

    public ToyLangError(String msgFmt, Object... arguments) {
        msg = String.format(msgFmt, arguments);
    }

}
