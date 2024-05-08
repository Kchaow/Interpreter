package org.letunov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.letunov.exception.SyntaxParsingException;
import org.letunov.lexer.Lexer;
import org.letunov.lexer.Word;
import org.letunov.parser.SyntaxParserImpl;
import org.letunov.parser.SyntaxParser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SyntaxParserTest {
    private Lexer lexer;
    @BeforeEach
    public void setupLexer() {
        lexer = new Lexer();
        lexer.reserve(Word.BEGIN);
        lexer.reserve(Word.END);
        lexer.reserve(Word.FIRST);
        lexer.reserve(Word.SECOND);
    }

    @Test
    public void parsingTest() {
        String text = "Begin First 23 asd123; hj 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralNumberAtLinkSuccessTest() {
        String text = "Begin First 23, 134, 89, 0 asd123;d 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralNumberAtLinkFailedTest() {
        String text = "Begin First 23, 134, 89, asd123; 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class, () -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralVariableAtLinkSuccessTest() {
        String text = "Begin Second a1 a2 a3 a4 asd123;hj 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralVariableAtLinkFailedTest() {
        String text = "Begin Second a1 ; 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class, () -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralVariableAtLastSuccessTest() {
        String text = "Begin Second a1 a2 a3 a4 asd123;fg23;df32 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralVariableAtLastFailedTest() {
        String text = "Begin Second a1 a2 a3 a4 asd123; 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class, () -> syntaxParser.parse(text));
    }

}
