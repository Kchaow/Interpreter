package org.letunov.exception;

import lombok.Getter;

public class VariableDeclarationException extends LanguageException {
    public VariableDeclarationException(String message, int line) {
        super(message, line);
    }
    public VariableDeclarationException(int line) {
        super(line);
    }
}
