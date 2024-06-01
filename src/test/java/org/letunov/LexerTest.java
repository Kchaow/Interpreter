package org.letunov;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.letunov.exception.AlphabetException;
import org.letunov.exception.NumeralSystemException;
import org.letunov.lexer.Lexer;
import org.letunov.lexer.Num;
import org.letunov.lexer.Token;
import org.letunov.lexer.Word;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {
    @Test
    public void numAndCharacterStringTest() throws IOException, NumeralSystemException, AlphabetException {
        Lexer lexer = new Lexer();
        lexer.reserve(new Word("for"));
        lexer.reserve(new Word("if"));
        String text = "for 234 big228 if 12";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        lexer.setInputStream(inputStream);
        List<Token> tokens = new ArrayList<>();
        Token token = lexer.scan();
        while (token != null){
            tokens.add(token);
            token = lexer.scan();
        }
        assertAll(
                () -> assertEquals(new Word("for"), tokens.getFirst()),
                () -> assertEquals(new Num(234), tokens.get(1)),
                () -> assertEquals(new Word("big228"), tokens.get(2)),
                () -> assertEquals(new Word("if"), tokens.get(3)),
                () -> assertEquals(new Num(12), tokens.get(4))
        );
    }

    @Test
    @Disabled
    public void severalTokenInOneWordTest() throws IOException, NumeralSystemException, AlphabetException {
        Lexer lexer = new Lexer();
        String text = "bab12+177";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        lexer.setInputStream(inputStream);
        List<Token> tokens = new ArrayList<>();
        Token token = lexer.scan();
        while (token != null){
            tokens.add(token);
            token = lexer.scan();
        }
        assertAll(
                () -> assertEquals(new Word("bab12"), tokens.getFirst()),
                () -> assertEquals(new Token('+'), tokens.get(1)),
                () -> assertEquals(new Num(199), tokens.get(2))
        );
    }


}
