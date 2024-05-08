package org.letunov.exception;

public class SyntaxParsingException extends Exception {
    public SyntaxParsingException(String message) {
        super(message);
    }
    public SyntaxParsingException() {
        super();
    }
}
