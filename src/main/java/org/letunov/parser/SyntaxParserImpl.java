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
            EQUAL = new Token('='),
            LEFT_BRACE = new Token('('),
            RIGHT_BRACE = new Token(')'),
            MINUS = new Token('-'),
            PLUS = new Token('+'),
            STAR = new Token('*'),
            SLASH = new Token('/'),
            POWER  = new Token('^');
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
            rightPart();
        }
        else
            throw new SyntaxParsingException("Error");
    }
    private void rightPart() throws SyntaxParsingException, IOException {
//        boolean isBrace = false;
////        if (currentToken.equals(LEFT_BRACE)) {
////            match(LEFT_BRACE);
////            isBrace = true;
////        }
        if (currentToken.equals(PLUS)) {
            match(PLUS);
        } else if (currentToken.equals(MINUS)) {
            match(MINUS);
        }
        block1();
        while (currentToken.equals(PLUS) || currentToken.equals(MINUS)) {
            if (currentToken.equals(PLUS)) {
                match(PLUS);
            } else if (currentToken.equals(MINUS)) {
                match(MINUS);
            }
            block1();
        }
//        if (isBrace)
//            match(RIGHT_BRACE);
    }
    private void block1() throws SyntaxParsingException, IOException {
        block2();
        while (currentToken.equals(STAR) || currentToken.equals(SLASH)) {
            if (currentToken.equals(STAR)) {
                match(STAR);
            } else if (currentToken.equals(SLASH)) {
                match(SLASH);
            }
            block2();
        }
    }
    private void block2() throws SyntaxParsingException, IOException {
        block3();
        if (currentToken == null)
            throw new SyntaxParsingException("Error");
        while (currentToken.equals(POWER)) {
            match(POWER);
            block3();
        }
    }
    private void block3() throws SyntaxParsingException, IOException {
        if (isFunction(currentToken)) {
            if (Word.SIN.equals(currentToken)) {
                matchReservedWord(Word.SIN);
            }
            else if (Word.COS.equals(currentToken)) {
                matchReservedWord(Word.COS);
            }
            else if (Word.ABS.equals(currentToken)) {
                matchReservedWord(Word.ABS);
            }
//            match(LEFT_BRACE);
//            block1();
//            match(RIGHT_BRACE);
            Token gap = currentToken;
            match(LEFT_BRACE);
            lexer.unreadToken();
            currentToken = gap;
        }
        block4();
    }
    private void block4() throws SyntaxParsingException, IOException {
        if (currentToken instanceof Word word && !lexer.isReserved(word)) {
            matchVariable();
        }
        else if (currentToken instanceof Num) {
            match(NUMBER);
        }
        else if (currentToken.equals(LEFT_BRACE)) {
            match(LEFT_BRACE);
            rightPart();
            match(RIGHT_BRACE);
        }
        else
            throw new SyntaxParsingException("Error");
    }
    private boolean isFunction(Token token) {
        return Word.SIN.equals(currentToken) ||
                Word.COS.equals(currentToken) ||
                Word.ABS.equals(currentToken);
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
