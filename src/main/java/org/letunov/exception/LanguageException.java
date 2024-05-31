package org.letunov.exception;

import lombok.Getter;

@Getter
public class LanguageException extends Exception {
    private final int line;
    private final int symbolCount;
    private final int symbolLength;
    public LanguageException(String message, int line, int symbolCount, int symbolLength) {
        super(message);
        this.line = line;
        this.symbolCount = symbolCount;
        this.symbolLength = symbolLength;
    }
    public LanguageException(int line, int symbolCount, int symbolLength) {
        this.line = line;
        this.symbolCount = symbolCount;
        this.symbolLength = symbolLength;
    }
}
