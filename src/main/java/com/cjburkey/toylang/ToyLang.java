package com.cjburkey.toylang;

import com.cjburkey.toylang.antlr.ToyLangLexer;
import com.cjburkey.toylang.antlr.ToyLangParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

@SuppressWarnings("unused")
public class ToyLang {

    public static final ToyLang INSTANCE = new ToyLang();

    public static void main(String[] args) {
        System.out.printf("ToyLang interpreter v%s on JVM %s\n", "0.0.1", System.getProperty("java.version"));

        // Load from the example file
        String input = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("test.tlp"))))
                .lines()
                .collect(Collectors.joining("\n"));

        // Start the parsing process
        Program program = INSTANCE.parse(true, input);
        program.execute();
    }

    private ToyLangParser createParser(ToyLangLexer lexer) {
        ToyLangParser toyLangParser = new ToyLangParser(new CommonTokenStream(lexer));
        toyLangParser.removeErrorListeners();
        toyLangParser.addErrorListener(ParserErrorHandler.INSTANCE);
        return toyLangParser;
    }

    private Program parse(boolean debug, ToyLangParser parser) {
        return new Program(debug).parse(parser.program());
    }

    public Program parse(boolean debug, InputStream inputStream) throws IOException {
        return parse(debug, createParser(createLexer(inputStream)));
    }

    public Program parse(boolean debug, String input) {
        return parse(debug, createParser(createLexer(input)));
    }

    private ToyLangLexer createLexer(CharStream charStream) {
        ToyLangLexer lexer = new ToyLangLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ParserErrorHandler.INSTANCE);
        return lexer;
    }

    private ToyLangLexer createLexer(InputStream inputStream) throws IOException {
        return createLexer(CharStreams.fromStream(inputStream, StandardCharsets.UTF_8));
    }

    private ToyLangLexer createLexer(String input) {
        return createLexer(CharStreams.fromString(input));
    }

}
