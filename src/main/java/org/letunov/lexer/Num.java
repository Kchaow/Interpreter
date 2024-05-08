package org.letunov.lexer;

import java.util.Objects;

public class Num extends Token {
    private final int value;
    public Num(int value) {
        super(LexemeType.NUM);
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (this == object)
            return true;
        if (object instanceof Num otherWord)
            return this.value == otherWord.value
                    && lexemeType == otherWord.lexemeType;
        else
            return false;
    }
    @Override
    public int hashCode() {
        return 31 * lexemeType.hashCode() + value;
    }
}
