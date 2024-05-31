package org.letunov.exception;

import lombok.Getter;

public class SyntaxParsingException extends LanguageException {
    public SyntaxParsingException(String message, int line) {
        super(message, line);
    }
    public SyntaxParsingException(int line) {
        super(line);
    }
}
