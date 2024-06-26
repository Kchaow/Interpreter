package org.letunov.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.letunov.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    private final ObjectMapper objectMapper;
    public ExceptionController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @ExceptionHandler({SyntaxParsingException.class, VariableDeclarationException.class, NumeralSystemException.class, AlphabetException.class})
    public ResponseEntity<String> languageExceptionHandle(LanguageException languageException) {
        ObjectNode errorMessage = objectMapper.createObjectNode();
        errorMessage.put("error", languageException.getMessage());
        errorMessage.put("line", languageException.getLine());
        errorMessage.put("symbolCount", languageException.getSymbolCount());
        errorMessage.put("symbolLength", languageException.getSymbolLength());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorMessage.toString(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<String> emptyRequestHandle(RuntimeException runtimeException) {
        ObjectNode errorMessage = objectMapper.createObjectNode();
        errorMessage.put("error", "Не найдено поле listing в запросе");
        errorMessage.put("line", "1");
        errorMessage.put("symbolCount", 1);
        errorMessage.put("symbolLength", 1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorMessage.toString(), headers, HttpStatus.BAD_REQUEST);
    }
}
