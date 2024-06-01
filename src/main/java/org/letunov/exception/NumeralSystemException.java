package org.letunov.exception;

public class NumeralSystemException extends LanguageException {
    public NumeralSystemException(String message, int line, int symbolCount, int symbolLength) {
        super(message, line, symbolCount, symbolLength);
    }
    public NumeralSystemException(int line, int symbolCount, int symbolLength) {
        super(line, symbolCount, symbolLength);
    }
}
