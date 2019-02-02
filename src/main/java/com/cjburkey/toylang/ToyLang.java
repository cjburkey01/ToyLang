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
import java.util.stream.Stream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class ToyLang {
    
    public static final ToyLang INSTANCE = new ToyLang();
    
    public static void main(String[] args) {
        System.out.println("ToyLang interpreter v0.0.1");
    
        Stream<String> stream = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("test.tlp")))).lines();
        Compiler compiler = INSTANCE.parse(stream.collect(Collectors.joining("\n")));
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
    
    private ToyLangParser createParser(ToyLangLexer lexer) {
        ToyLangParser toyLangParser = new ToyLangParser(new CommonTokenStream(lexer));
        return toyLangParser;
    }
    
    private ToyLangLexer createLexer(InputStream inputStream) throws IOException {
        return new ToyLangLexer(CharStreams.fromStream(inputStream, StandardCharsets.UTF_8));
    }
    
    private ToyLangLexer createLexer(String input) {
        return new ToyLangLexer(CharStreams.fromString(input));
    }
    
}
