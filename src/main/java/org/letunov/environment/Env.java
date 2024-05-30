package org.letunov.environment;

import org.letunov.lexer.Word;

import java.util.HashMap;
import java.util.Map;

public class Env {
    private Map<Word, Integer> table;
    public Env() {
        table = new HashMap<>();
    }
    public void put(Word word, int i) {
        table.put(word, i);
    }
    public Integer get(Word word) {
        return table.get(word);
    }
    public void remove(Word word) {
        table.remove(word);
    }
}
