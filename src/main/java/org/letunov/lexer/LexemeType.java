package org.letunov.lexer;

public class LexemeType {
    public static final LexemeType NUM = new LexemeType(-1),
                            WORD = new LexemeType(-2);
    private final int tag;
    LexemeType(int tag) {
        this.tag = tag;
    }
    public int getTag() {
        return tag;
    }
    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (this == object)
            return true;
        if (object instanceof LexemeType otherLexemeType) {
            return tag == otherLexemeType.tag;
        }
        else
            return false;
    }
    @Override
    public int hashCode() {
        return tag;
    }
    @Override
    public String toString() {
        return String.valueOf((char) tag);
    }
}
