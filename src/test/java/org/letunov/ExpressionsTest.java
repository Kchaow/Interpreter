package org.letunov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.letunov.environment.Env;
import org.letunov.exception.AlphabetException;
import org.letunov.exception.NumeralSystemException;
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
    public void test_1() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 1 + 1 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 2,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_2() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 1 + 1 + 1 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 3,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_3() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 1 + 1 + 1 * 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 4,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_4() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 1 + 1 + 1 * 2 * 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 6,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_5() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 5 + 6 * 3 + 7 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 30,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_6() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = (5 + 6) * (3 + 7) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 110,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_7() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 7 + 5 * 3 + (6 + 3) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 31,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_8() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = (5 + 6 * (4 * 5 + (12 + 34) + 70) * 5) + 6 * 4 + (34 + 5) + (5 * 4) End"; //(5 + 6 * (4 * 5 + (10 + 28) + 56) * 5) + 6 * 4 + (28 + 5) + (5 * 4)
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 3502,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_9() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 1 - 1 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 0,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_10() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 1 - 1 + 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 2,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_11() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 12 - 3 / 2 End"; //10 - 3 / 2
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 9,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_12() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 67 / (11 + 43 / (23 * 3) / 2) * 2 + 3 * (7 - 34) End";//55 / (9 + 35 / (19 * 3) / 2) * 2 + 3 * (7 - 28)
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( -51,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_13() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = cos(0) End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 1,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_14() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = cos(0) + abs(-1) * abs(7 + 7 * ((56 - sin(3 + abs(-45)) + (34 / 4) - 3) - 43)) End";//cos(0) + abs(-1) * abs(7 + 7 * ((46 - sin(3 + abs(-37)) + (28 / 4) - 3) - 35))
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 113,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_15() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "45: gg = (5 + 4) * (cos(0) / 5) " +
                "23: wp = (3 + 5) * (3 + 5) " +
                "25: nt = 1 + 1 End";
        Word var_1 = new Word("gg");
        Word var_2 = new Word("wp");
        Word var_3 = new Word("nt");
        syntaxParser.parse(lang);
        assertAll(
                () -> assertEquals(0, syntaxParser.getEnv().get(var_1)),
                () -> assertEquals(64, syntaxParser.getEnv().get(var_2)),
                () -> assertEquals(2, syntaxParser.getEnv().get(var_3))
        );
    }

    @Test
    public void test_16() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg 67: gg = -1 + 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 1,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_17() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg 67: gg = - (5 + 6) + 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( -9,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_18() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg 67: gg = - cos(0) + 2 End";
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 1,syntaxParser.getEnv().get(var));
    }

    @Test
    public void test_19() throws SyntaxParsingException, IOException, VariableDeclarationException, NumeralSystemException, AlphabetException {
        String lang = "Begin First 45 fg " +
                "67: gg = 7 + 5 * 32 + (63 + 3) End"; //7 + 5 * 26 + (51 + 3)
        Word var = new Word("gg");
        syntaxParser.parse(lang);
        assertEquals( 191,syntaxParser.getEnv().get(var));
    }

}
