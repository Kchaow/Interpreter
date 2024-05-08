package org.letunov.lexer;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private int line = 1;
    private char peek = ' ';
    private final Map<String, Word> words = new HashMap<>();
    private PushbackReader pushbackReader;
    public void reserve(Word word) {
        words.put(word.getLexeme(), word);
    }
    public void setInputStream(InputStream inputStream) {
        this.pushbackReader = new PushbackReader(new BufferedReader(new InputStreamReader(inputStream)));
    }
    public PushbackReader getPushbackReader() {
        return pushbackReader;
    }
    public Token scan() throws IOException {
         do {
            int unit = pushbackReader.read();
            if (unit == -1 || unit == 65535)
                return null;
            peek = (char) unit;
            if (peek == '\n')
                line++;
        } while (peek == ' ' || peek == '\t' || peek == '\n');

        if (Character.isDigit(peek)) {
            int value = 0;
            do {
                value = 10*value + Character.digit(peek, 10);
                peek = (char) pushbackReader.read();
            } while (Character.isDigit(peek));
            pushbackReader.unread(peek);
            return new Num(value);
        }

        if (Character.isLetter(peek)) {
            StringBuilder stringBuilder = new StringBuilder();
            do {
                stringBuilder.append(peek);
                peek = (char) pushbackReader.read();
            } while (Character.isLetterOrDigit(peek));
            pushbackReader.unread(peek);
            String lexeme = stringBuilder.toString();
            Word word = words.get(lexeme);
            if (word != null)
                return word;
            Word newWord = new Word(lexeme);
            words.put(lexeme, newWord);
            return newWord;
        }

        return new Token(new LexemeType(peek));
    }
}
