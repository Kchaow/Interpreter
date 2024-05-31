package org.letunov.exception;

import lombok.Getter;

public class VariableDeclarationException extends LanguageException {
    public VariableDeclarationException(String message, int line, int symbolCount, int symbolLength) {
        super(message, line, symbolCount, symbolLength);
    }
    public VariableDeclarationException(int line, int symbolCount, int symbolLength) {
        super(line, symbolCount, symbolLength);
    }
}
