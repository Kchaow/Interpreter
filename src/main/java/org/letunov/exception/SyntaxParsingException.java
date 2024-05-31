package org.letunov.exception;

import lombok.Getter;

public class SyntaxParsingException extends LanguageException {
    public SyntaxParsingException(String message, int line, int symbolCount, int symbolLength) {
        super(message, line, symbolCount, symbolLength);
    }
    public SyntaxParsingException(int line, int symbolCount, int symbolLength) {
        super(line, symbolCount, symbolLength);
    }
}
