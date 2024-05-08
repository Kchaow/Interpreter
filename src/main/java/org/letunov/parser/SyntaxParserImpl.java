package org.letunov.parser;

import org.letunov.exception.SyntaxParsingException;
import org.letunov.lexer.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SyntaxParserImpl implements SyntaxParser {
    private final Lexer lexer;
    private Token currentToken;
    private final Token NUMBER = new Token(LexemeType.NUM),
            COMMA = new Token(','),
            COLON = new Token(':'),
            SEMICOLON = new Token(';'),
            EQUAL = new Token('=');
    public SyntaxParserImpl(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public void parse(String text) throws IOException, SyntaxParsingException {
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        lexer.setInputStream(inputStream);
        language();
    }
    private void language() throws IOException, SyntaxParsingException {
        currentToken = lexer.scan();
        if (Word.BEGIN.equals(currentToken)) {
            matchReservedWord(Word.BEGIN);
            link();
            last();
            operator();
            matchReservedWord(Word.END);
        }
        else
            throw new SyntaxParsingException("Error");
    }
    private void link() throws SyntaxParsingException, IOException {
        if (Word.FIRST.equals(currentToken)) {
            matchReservedWord(Word.FIRST);
            match(NUMBER);
            while (currentToken.equals(COMMA)) {
                match(COMMA);
                match(NUMBER);
            }
        }
        else if (Word.SECOND.equals(currentToken)) {
            matchReservedWord(Word.SECOND);
            matchVariable();
            int variableCount = 1;
            Token lastVar = null;
            while (currentToken instanceof Word word && !lexer.isReserved(word)) {
                lastVar = currentToken;
                matchVariable();
                variableCount++;
            }
            if (variableCount < 2)
                throw new SyntaxParsingException("Error");
            currentToken = lastVar;
            lexer.unreadToken();
        }
        else
            throw new SyntaxParsingException("Error");
    }
    private void last() throws SyntaxParsingException, IOException {
        if (currentToken instanceof Word word && !lexer.isReserved(word)) {
            matchVariable();
            while (currentToken.equals(SEMICOLON)) {
                match(SEMICOLON);
                matchVariable();
            }
        }
        else
            throw new SyntaxParsingException("Error");
    }
    private void operator() throws SyntaxParsingException, IOException {
        if (currentToken instanceof Num) {
            match(NUMBER);
            match(COLON);
            matchVariable();
            match(EQUAL);
        }
        else
            throw new SyntaxParsingException("Error");
    }
    private void matchReservedWord(Word word) throws IOException, SyntaxParsingException {
        if (word.equals(currentToken) && lexer.isReserved(word))
            currentToken = lexer.scan();
        else
            throw new SyntaxParsingException("Error");
    }
    private void matchVariable() throws IOException, SyntaxParsingException {
        if (currentToken instanceof Word word && !lexer.isReserved(word))
            currentToken = lexer.scan();
        else
            throw new SyntaxParsingException("Error");
    }
    private void match(Token token) throws IOException, SyntaxParsingException {
        if (token.equals(currentToken))
            currentToken = lexer.scan();
        else
            throw new SyntaxParsingException("Error");
    }
}
