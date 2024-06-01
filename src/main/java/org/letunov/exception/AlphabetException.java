package org.letunov.exception;

public class AlphabetException  extends LanguageException {
    public AlphabetException(String message, int line, int symbolCount, int symbolLength) {
        super(message, line, symbolCount, symbolLength);
    }
    public AlphabetException(int line, int symbolCount, int symbolLength) {
        super(line, symbolCount, symbolLength);
    }
}
