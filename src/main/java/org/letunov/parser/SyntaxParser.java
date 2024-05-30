package org.letunov.parser;

import org.letunov.environment.Env;
import org.letunov.exception.SyntaxParsingException;
import org.letunov.exception.VariableDeclarationException;
import org.letunov.lexer.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SyntaxParser {
    private final Lexer lexer;
    private final Env env;
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
    public SyntaxParser(Lexer lexer, Env env) {
        this.lexer = lexer;
        this.env = env;
    }
    public Env getEnv() {
        return env;
    }
    public void parse(String text) throws IOException, SyntaxParsingException, VariableDeclarationException {
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        lexer.setInputStream(inputStream);
        language();
    }
    private void language() throws IOException, SyntaxParsingException, VariableDeclarationException {
        currentToken = lexer.scan();
        matchReservedWord(Word.BEGIN);
        link();
        while (Word.FIRST.equals(currentToken) || Word.SECOND.equals(currentToken))
            link();
        last();
        operator();
        while (NUMBER.equals(currentToken))
            operator();
        matchReservedWord(Word.END);
    }
    private void link() throws SyntaxParsingException, IOException {
        if (Word.FIRST.equals(currentToken)) {
            matchReservedWord(Word.FIRST);
            match(NUMBER);
            while (currentToken != null && currentToken.equals(COMMA)) {
                match(COMMA);
                match(NUMBER);
            }
        }
        else if (Word.SECOND.equals(currentToken)) {
            matchReservedWord(Word.SECOND);
            matchVariable();
            int variableCount = 1;
            Token lastVar = null;
            while (currentToken != null && currentToken instanceof Word word && !lexer.isReserved(word)) {
                lastVar = currentToken;
                matchVariable();
                variableCount++;
            }
            if (variableCount < 2 && currentToken == null)
                throw new SyntaxParsingException("Ожидалась переменная, но найден конец");
            else if (variableCount < 2)
                throw new SyntaxParsingException("Ожидалась переменная, но найдено %s"
                        .formatted(currentToken.toString()));
            if (currentToken == null || !currentToken.equals(Word.FIRST)) {
                currentToken = lastVar;
                lexer.unreadToken();
            }
        }
        else if (currentToken == null)
            throw new SyntaxParsingException("Ожидалось First или Second, но найден конец");
        else
            throw new SyntaxParsingException("Ожидалось First или Second, но найден %s"
                    .formatted(currentToken.toString()));
    }
    private void last() throws SyntaxParsingException, IOException {
        matchVariable();
        if (currentToken == null)
            throw new SyntaxParsingException("Ожидалось целое число или ;, но найден конец");
        while (currentToken != null && currentToken.equals(SEMICOLON)) {
            match(SEMICOLON);
            matchVariable();
        }
    }
    private void operator() throws SyntaxParsingException, IOException, VariableDeclarationException {
        match(NUMBER);
        match(COLON);
        Token var = currentToken;
        matchVariable();
        match(EQUAL);
        env.put((Word) var, rightPart());
    }
    private int rightPart() throws SyntaxParsingException, IOException, VariableDeclarationException {
        char op = ' ';
        int t = 0;
        if (currentToken == null)
            throw new SyntaxParsingException("Ожидалось выражение, но найден конец");
        if (currentToken.equals(PLUS)) {
            match(PLUS);
        } else if (currentToken.equals(MINUS)) {
            match(MINUS);
        }
        t = block1();
        while (currentToken.equals(PLUS) || currentToken.equals(MINUS)) {
            if (currentToken.equals(PLUS)) {
                match(PLUS);
                op = '+';
            } else if (currentToken.equals(MINUS)) {
                match(MINUS);
                op = '-';
            }
            switch (op) {
                case '+':
                    t += block1();
                    break;
                case '-':
                    t -= block1();
            }
        }
        return  t;
    }
    private int block1() throws SyntaxParsingException, IOException, VariableDeclarationException {
        char op = ' ';
        int t = 0;
        t = block2();
        while (currentToken.equals(STAR) || currentToken.equals(SLASH)) {
            if (currentToken.equals(STAR)) {
                match(STAR);
                op = '*';
            } else if (currentToken.equals(SLASH)) {
                match(SLASH);
                op = '/';
            }
            switch (op) {
                case '*':
                    t *= block2();
                    break;
                case '/':
                    t /= block2();
            }
        }
        return t;
    }
    private int block2() throws SyntaxParsingException, IOException, VariableDeclarationException {
        int t = 0;
        t = block3();
        if (currentToken == null)
            throw new SyntaxParsingException("Ожидалось End, но найден конец");
        while (currentToken.equals(POWER)) {
            match(POWER);
            t = (int) Math.pow(t, block3());
        }
        return t;
    }
    private int block3() throws SyntaxParsingException, IOException, VariableDeclarationException {
        int t = 0;
        char op = ' ';
        if (isFunction(currentToken)) {
            if (Word.SIN.equals(currentToken)) {
                matchReservedWord(Word.SIN);
                op = 's';
            }
            else if (Word.COS.equals(currentToken)) {
                matchReservedWord(Word.COS);
                op = 'c';
            }
            else if (Word.ABS.equals(currentToken)) {
                matchReservedWord(Word.ABS);
                op = 'a';
            }
            Token gap = currentToken;
            match(LEFT_BRACE);
            lexer.unreadToken();
            currentToken = gap;
            switch (op) {
                case 's':
                    t = (int) Math.sin(block4());
                    break;
                case 'c':
                    t = (int) Math.cos(block4());
                    break;
                case 'a':
                    t = Math.abs(block4());
            }
            return t;
        }
        return block4();
    }
    private int block4() throws SyntaxParsingException, IOException, VariableDeclarationException {
        int t = 0;
        if (currentToken == null)
            throw new SyntaxParsingException("Ожидался операнд, но найден конец");
        if (currentToken instanceof Word word && !lexer.isReserved(word)) {
            Token var = currentToken;
            matchVariable();
            Integer declaredVariable = env.get((Word) var);
            if (declaredVariable == null)
                throw new VariableDeclarationException();
            t = declaredVariable;
            return t;
        }
        else if (currentToken instanceof Num) {
            Token checked = currentToken;
            match(NUMBER);
            t = ((Num) checked).getValue();
            return t;
        }
        else if (currentToken.equals(LEFT_BRACE)) {
            match(LEFT_BRACE);
            t = rightPart();
            match(RIGHT_BRACE);
            return t;
        }
        else
            throw new SyntaxParsingException("Неожиданный токен: %s"
                    .formatted(currentToken.toString()));
    }
    private boolean isFunction(Token token) {
        return Word.SIN.equals(currentToken) ||
                Word.COS.equals(currentToken) ||
                Word.ABS.equals(currentToken);
    }
    private void matchReservedWord(Word word) throws IOException, SyntaxParsingException {
        if (currentToken == null)
            throw new SyntaxParsingException("Ожидалось ключевое слово %s, но найден конец"
                    .formatted(word.toString()));
        if (word.equals(currentToken) && lexer.isReserved(word))
            currentToken = lexer.scan();
        else
            throw new SyntaxParsingException("Ожидалось ключевое слово %s, но найдено %s"
                    .formatted(word.toString(), currentToken.toString()));
    }
    private void matchVariable() throws IOException, SyntaxParsingException {
        if (currentToken == null)
            throw new SyntaxParsingException("Ожидалась переменная, но найден конец");
        if (currentToken instanceof Word word && !lexer.isReserved(word))
            currentToken = lexer.scan();
        else
            throw new SyntaxParsingException("Ожидалась переменная, но найдено %s"
                    .formatted(currentToken.toString()));
    }
    private void match(Token token) throws IOException, SyntaxParsingException {
        if (currentToken == null)
            throw new SyntaxParsingException("Ожидалось %s, но найден конец"
                    .formatted(token.toString()));
        else if (token.equals(currentToken))
            currentToken = lexer.scan();
        else
            throw new SyntaxParsingException("Ожидалось %s, но найдено %s"
                    .formatted(token.toString(), currentToken.toString()));
    }
}
