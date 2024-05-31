package org.letunov.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.letunov.dto.EnvDto;
import org.letunov.environment.Env;
import org.letunov.exception.SyntaxParsingException;
import org.letunov.exception.VariableDeclarationException;
import org.letunov.lexer.Lexer;
import org.letunov.lexer.Word;
import org.letunov.parser.SyntaxParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class LanguageController {
    private final ObjectMapper objectMapper;
    public LanguageController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @GetMapping
    public ResponseEntity<Object> parseAndRun(@RequestBody String json) throws VariableDeclarationException, SyntaxParsingException, IOException {
        JsonNode jsonNode = objectMapper.readTree(json);
        String listing = jsonNode.get("listing").asText();
        if (listing == null)
            throw new NullPointerException();
        Lexer lexer = new Lexer();
        initLexer(lexer);
        Env env = new Env();
        SyntaxParser syntaxParser = new SyntaxParser(lexer, env);
        syntaxParser.parse(listing);
        return new ResponseEntity<>(syntaxParser.getEnv(), HttpStatus.OK);
    }

    private void initLexer(Lexer lexer) {
        lexer.reserve(Word.BEGIN);
        lexer.reserve(Word.END);
        lexer.reserve(Word.FIRST);
        lexer.reserve(Word.SECOND);
        lexer.reserve(Word.ABS);
        lexer.reserve(Word.SIN);
        lexer.reserve(Word.COS);
    }
}
