package org.letunov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.letunov.environment.Env;
import org.letunov.exception.SyntaxParsingException;
import org.letunov.exception.VariableDeclarationException;
import org.letunov.lexer.Lexer;
import org.letunov.lexer.Word;
import org.letunov.parser.SyntaxParser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionsTest {
    private SyntaxParser syntaxParser;
    @BeforeEach
    public void setupLexer() {
        Lexer lexer = new Lexer();
        lexer.reserve(Word.BEGIN);
        lexer.reserve(Word.END);
        lexer.reserve(Word.FIRST);
        lexer.reserve(Word.SECOND);
        lexer.reserve(Word.ABS);
        lexer.reserve(Word.SIN);
        lexer.reserve(Word.COS);
        Env env = new Env();
        syntaxParser = new SyntaxParser(lexer, env);
    }

    @Test
    public void test_1() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 1 + 1 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 2,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_2() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 1 + 1 + 1 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 3,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_3() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 1 + 1 + 1 * 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 4,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_4() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 1 + 1 + 1 * 2 * 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 6,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_5() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 5 + 6 * 3 + 8 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 31,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_6() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = (5 + 6) * (3 + 8) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 121,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_7() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 10 + 5 * 3 + (6 + 3) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 34,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_8() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = (5 + 6 * (4 * 5 + (12 + 34) + 90) * 5) + 6 * 4 + (34 + 5) + (5 * 4) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 4768,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_9() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 1 - 1 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 0,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_10() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 1 - 1 + 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 2,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_11() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 12 - 3 / 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 11,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_12() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = 67 / (11 + 43 / (23 * 3) / 2) * 2 + 3 * (8 - 34) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( -66,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_13() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = cos(0) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 1,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_14() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "69: gg = cos(0) + abs(-1) * abs(7 + 8 * ((56 - sin(3 + abs(-45)) + (34 / 4) - 3) - 43)) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 152,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_15() throws SyntaxParsingException, IOException, VariableDeclarationException {
        String lang = "Begin First 45 fg " +
                "45: gg = (5 + 4) * (cos(0) / 67) " +
                "23: wp = (3 + 56) * (3 + 12) " +
                "25: nt = 1 + 1 End";
        Word var_1 = new Word("gg");
        Word var_2 = new Word("wp");
        Word var_3 = new Word("nt");
        syntaxParser.parse(lang);
        assertAll(
                () -> assertEquals(0, syntaxParser.getEnv().get(var_1)),
                () -> assertEquals(885, syntaxParser.getEnv().get(var_2)),
                () -> assertEquals(2, syntaxParser.getEnv().get(var_3))
        );
    }

}
