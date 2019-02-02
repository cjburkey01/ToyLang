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

public class ToyLang {
    
    public static final ToyLang INSTANCE = new ToyLang();
    
    public static void main(String[] args) {
        // Info or debug idk
        System.out.println("ToyLang interpreter v0.0.1");
        
        // Load from the example file
        String input = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("test.tlp"))))
                .lines()
                .collect(Collectors.joining("\n"));
        
        // Start the compilation process
        Compiler compiler = INSTANCE.parse(input);
    }
    
    private ToyLangParser createParser(ToyLangLexer lexer) {
        ToyLangParser toyLangParser = new ToyLangParser(new CommonTokenStream(lexer));
        toyLangParser.removeErrorListeners();
        toyLangParser.addErrorListener(CompilerErrorHandler.INSTANCE);
        return toyLangParser;
    }
    
    private Compiler parse(ToyLangParser parser) {
        return new Compiler().start(parser.program());
    }
    
    public Compiler parse(InputStream inputStream) throws IOException {
        return parse(createParser(createLexer(inputStream)));
    }
    
    public Compiler parse(String input) {
        return parse(createParser(createLexer(input)));
    }
    
    private ToyLangLexer createLexer(CharStream charStream) {
        ToyLangLexer lexer = new ToyLangLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(CompilerErrorHandler.INSTANCE);
        return lexer;
    }
    
    private ToyLangLexer createLexer(InputStream inputStream) throws IOException {
        return createLexer(CharStreams.fromStream(inputStream, StandardCharsets.UTF_8));
    }
    
    private ToyLangLexer createLexer(String input) {
        return createLexer(CharStreams.fromString(input));
    }
    
}
