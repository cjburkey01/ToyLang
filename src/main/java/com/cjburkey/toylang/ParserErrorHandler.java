package com.cjburkey.toylang;

import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Created by CJ Burkey on 2019/02/01
 */
public class ParserErrorHandler extends ConsoleErrorListener {

    public static final ParserErrorHandler INSTANCE = new ParserErrorHandler();

    private static boolean errored;

    public static boolean hasErrored() {
        return errored;
    }

    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errored = true;
        System.err.printf("Syntax error on line %s at %s: %s\n", line, charPositionInLine, msg);
    }

}
