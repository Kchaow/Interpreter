package org.letunov.lexer;

import java.util.Objects;

public class Word extends Token {
    public static final Word BEGIN = new Word("Begin"),
                                END = new Word("End"),
                                FIRST = new Word("First"),
                                SECOND = new Word("Second"),
                                COS = new Word("cos"),
                                SIN = new Word("sin"),
                                ABS = new Word("abs");
    private final String lexeme;
    public Word(String lexeme) {
        super(LexemeType.WORD);
        this.lexeme = lexeme;
    }
    public String getLexeme() {
        return lexeme;
    }
    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (this == object)
            return true;
        if (object instanceof Word otherWord)
            return Objects.equals(this.lexeme, otherWord.lexeme)
                    && super.equals(otherWord);
        else
            return false;
    }
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + lexeme.hashCode();
    }
}
