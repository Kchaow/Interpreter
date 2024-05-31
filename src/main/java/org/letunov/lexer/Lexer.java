package org.letunov.lexer;

import lombok.Getter;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    @Getter
    private int line = 1;
    @Getter
    private int symbolCount = 1;
    @Getter
    private int symbolLength = 1;
    private final Map<String, Word> words = new HashMap<>();
    private final Map<String, Word> reservedWords = new HashMap<>();
    private PushbackReader pushbackReader;
    private boolean isUnread;
    private Token lastToken;
    public void reserve(Word word) {
        reservedWords.put(word.getLexeme(), word);
    }
    public void setInputStream(InputStream inputStream) {
        this.pushbackReader = new PushbackReader(new BufferedReader(new InputStreamReader(inputStream)));
    }

    public Token scan() throws IOException {
        if (isUnread) {
            isUnread = false;
            return lastToken;
        }

        char peek = ' ';
        symbolLength = 1;
        do {
            int unit = pushbackReader.read();
            symbolCount++;
            if (unit == -1 || unit == 65535) {
                lastToken = null;
                return lastToken;
            }
            peek = (char) unit;
            if (peek == '\n') {
                line++;
                symbolCount = 1;
            }
        } while (peek == ' ' || peek == '\t' || peek == '\n');

        if (Character.isDigit(peek)) {
            int value = 0;
            do {
                value = 10*value + Character.digit(peek, 10);
                peek = (char) pushbackReader.read();
                symbolLength++;
                symbolCount++;
            } while (Character.isDigit(peek));
            pushbackReader.unread(peek);
            symbolLength--;
            symbolCount--;
            lastToken = new Num(value);
            return lastToken;
        }

        if (Character.isLetter(peek)) {
            StringBuilder stringBuilder = new StringBuilder();
            do {
                stringBuilder.append(peek);
                peek = (char) pushbackReader.read();
                symbolLength++;
                symbolCount++;
            } while (Character.isLetterOrDigit(peek));
            pushbackReader.unread(peek);
            symbolLength--;
            symbolCount--;
            String lexeme = stringBuilder.toString();
            Word word = reservedWords.get(lexeme);
            if (word != null) {
                lastToken = word;
                return lastToken;
            }
            word = words.get(lexeme);
            if (word != null) {
                lastToken = word;
                return lastToken;
            }
            word = new Word(lexeme);
            words.put(lexeme, word);
            lastToken = word;
            return lastToken;
        }

        lastToken = new Token(new LexemeType(peek));
        return lastToken;
    }
    public void unreadToken() {
        isUnread = true;
    }
    public boolean isReserved(Word word) {
        return reservedWords.containsKey(word.getLexeme());
    }
}
