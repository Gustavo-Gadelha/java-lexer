package edu.fafic.vocabulary;

import edu.fafic.token.Type;

import java.util.Map;

public final class Punctuation {

    private static final Map<Character, Type> ALL = Map.ofEntries(
            Map.entry('.', Type.DOT),
            Map.entry(',', Type.COMMA),
            Map.entry(':', Type.COLON),
            Map.entry(';', Type.SEMICOLON),
            Map.entry('@', Type.AT),
            Map.entry('(', Type.LPAREN),
            Map.entry(')', Type.RPAREN),
            Map.entry('{', Type.LBRACE),
            Map.entry('}', Type.RBRACE),
            Map.entry('[', Type.LBRACKET),
            Map.entry(']', Type.RBRACKET)
    );

    private Punctuation() {
    }

    public static Type resolve(String lexeme) {
        return ALL.getOrDefault(lexeme.charAt(0), Type.IDENTIFIER);
    }

    public static boolean contains(int ch) {
        return ALL.containsKey((char) ch);
    }
}
