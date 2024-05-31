package org.letunov.exception;

import lombok.Getter;

@Getter
public class LanguageException extends Exception {
    private final int line;
    public LanguageException(String message, int line) {
        super(message);
        this.line = line;
    }
    public LanguageException(int line) {
        this.line = line;
    }
}
