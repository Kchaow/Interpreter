package org.letunov.parser;

import org.letunov.exception.SyntaxParsingException;

import java.io.IOException;

public interface SyntaxParser {
    void parse(String text) throws IOException, SyntaxParsingException;
}
