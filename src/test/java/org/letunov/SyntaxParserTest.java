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
        lexer.reserve(Word.ABS);
        lexer.reserve(Word.SIN);
        lexer.reserve(Word.COS);
    }

    @Test
    public void parsingTest() {
        String text = "Begin First 23 asd123; hj 56 : rty = 4 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralNumberAtLinkSuccessTest() {
        String text = "Begin First 23, 134, 89, 0 asd123;d 56 : rty = 2 End";
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
        String text = "Begin Second a1 a2 a3 a4 asd123;hj 56 : rty = 4 End";
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
        String text = "Begin Second a1 a2 a3 a4 asd123;fg23;df32 56 : rty = 4 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingSeveralVariableAtLastFailedTest() {
        String text = "Begin Second a1 a2 a3 a4 asd123; 56 : rty = End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class, () -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_1() {
        String text = "Begin Second wf23 gh4 32 : g = 1 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_2() {
        String text = "Begin Second wf23 gh4 32 : g = 1 + 2 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_3() {
        String text = "Begin Second wf23 gh4 32 : g = (1 + 2) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_4() {
        String text = "Begin Second wf23 gh4 32 : g = 1 + End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class, () -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_5() {
        String text = "Begin Second wf23 gh4 32 : g = 1 + 2 + 3 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_6() {
        String text = "Begin Second wf23 gh4 32 : g = - 1 + 2 + 3 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_7() {
        String text = "Begin Second wf23 gh4 32 : g = - (1 + 2 + 3) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_8() {
        String text = "Begin Second wf23 gh4 32 : g = 1 + 2 * 3 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_9() {
        String text = "Begin Second wf23 gh4 32 : g = 1 * 2 * 3 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_10() {
        String text = "Begin Second wf23 gh4 32 : g = (1 + 2) * 3 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_11() {
        String text = "Begin Second wf23 gh4 32 : g = 1 + (2 * 3) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_12() {
        String text = "Begin Second wf23 gh4 32 : g = (1 + 2) + (3 + 4) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_13() {
        String text = "Begin Second wf23 gh4 32 : g = (1 + 2) * (3 + 4) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_14() {
        String text = "Begin Second wf23 gh4 32 : g = 1 / 2 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_15() {
        String text = "Begin Second wf23 gh4 32 : g = 3 +  6 ^ 2 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_16() {
        String text = "Begin Second wf23 gh4 32 : g = (3 +  6) ^ 2 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_17() {
        String text = "Begin Second wf23 gh4 32 : g = cos(5) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_18() {
        String text = "Begin Second wf23 gh4 32 : g = cos(5) sin(6) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class ,() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_19() {
        String text = "Begin Second wf23 gh4 32 : g = cos 5 End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class ,() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_20() {
        String text = "Begin Second wf23 gh4 32 : g = cos( 5 + 6 * 2 ) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_21() {
        String text = "Begin Second wf23 gh4 32 : g = cos( (5 + 6) * 2 ) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_22() {
        String text = "Begin Second wf23 gh4 32 : g = abs( cos( (5 + 6) * 2 ) ) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_23() {
        String text = "Begin Second wf23 gh4 32 : g = abs( cos( (5 + 6) * 2 ) + 10 ) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }
    @Test
    public void parsingExpressions_24() {
        String text = "Begin Second wf23 gh4 32 : g = abs( cos( (5 + 6) * 2 ) + + ) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertThrows(SyntaxParsingException.class ,() -> syntaxParser.parse(text));
    }

    @Test
    public void parsingExpressions_25() {
        String text = "Begin Second wf23 gh4 32 : g = 26 * 4 / abs( cos( (5 + 6) * 2 ) + 10 ) End";
        SyntaxParser syntaxParser = new SyntaxParserImpl(lexer);
        assertDoesNotThrow(() -> syntaxParser.parse(text));
    }
}
