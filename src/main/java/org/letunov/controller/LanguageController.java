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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
public class LanguageController {
    private final ObjectMapper objectMapper;
    public LanguageController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @GetMapping
    public String page() {
        return "index";
    }
    @PostMapping("/run")
    @ResponseBody
    public ResponseEntity<Object> parseAndRun(@RequestBody String json) throws VariableDeclarationException, SyntaxParsingException, IOException {
        log.info(json);
        JsonNode jsonNode = objectMapper.readTree(json);
        String listing = jsonNode.get("listing").asText();
        log.info(listing);
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
