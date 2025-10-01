package edu.fafic.vocabulary;

import edu.fafic.token.Type;

import java.util.Map;

public final class Keywords {

    private static final Map<String, Type> ALL = Map.ofEntries(
            Map.entry("abstract", Type.KEYWORD_ABSTRACT),
            Map.entry("boolean", Type.KEYWORD_BOOLEAN),
            Map.entry("break", Type.KEYWORD_BREAK),
            Map.entry("case", Type.KEYWORD_CASE),
            Map.entry("catch", Type.KEYWORD_CATCH),
            Map.entry("char", Type.KEYWORD_CHAR),
            Map.entry("class", Type.KEYWORD_CLASS),
            Map.entry("continue", Type.KEYWORD_CONTINUE),
            Map.entry("default", Type.KEYWORD_DEFAULT),
            Map.entry("do", Type.KEYWORD_DO),
            Map.entry("double", Type.KEYWORD_DOUBLE),
            Map.entry("else", Type.KEYWORD_ELSE),
            Map.entry("enum", Type.KEYWORD_ENUM),
            Map.entry("extends", Type.KEYWORD_EXTENDS),
            Map.entry("final", Type.KEYWORD_FINAL),
            Map.entry("finally", Type.KEYWORD_FINALLY),
            Map.entry("float", Type.KEYWORD_FLOAT),
            Map.entry("for", Type.KEYWORD_FOR),
            Map.entry("if", Type.KEYWORD_IF),
            Map.entry("implements", Type.KEYWORD_IMPLEMENTS),
            Map.entry("import", Type.KEYWORD_IMPORT),
            Map.entry("int", Type.KEYWORD_INT),
            Map.entry("interface", Type.KEYWORD_INTERFACE),
            Map.entry("long", Type.KEYWORD_LONG),
            Map.entry("new", Type.KEYWORD_NEW),
            Map.entry("package", Type.KEYWORD_PACKAGE),
            Map.entry("private", Type.KEYWORD_PRIVATE),
            Map.entry("protected", Type.KEYWORD_PROTECTED),
            Map.entry("public", Type.KEYWORD_PUBLIC),
            Map.entry("return", Type.KEYWORD_RETURN),
            Map.entry("short", Type.KEYWORD_SHORT),
            Map.entry("static", Type.KEYWORD_STATIC),
            Map.entry("super", Type.KEYWORD_SUPER),
            Map.entry("switch", Type.KEYWORD_SWITCH),
            Map.entry("this", Type.KEYWORD_THIS),
            Map.entry("throw", Type.KEYWORD_THROW),
            Map.entry("throws", Type.KEYWORD_THROWS),
            Map.entry("try", Type.KEYWORD_TRY),
            Map.entry("void", Type.KEYWORD_VOID),
            Map.entry("while", Type.KEYWORD_WHILE)
    );

    private static final Map<String, Type> CONSTANT_LITERALS = Map.ofEntries(
            Map.entry("true", Type.LITERAL_TRUE),
            Map.entry("false", Type.LITERAL_FALSE),
            Map.entry("null", Type.LITERAL_NULL)
    );

    private Keywords() {
    }

    public static Type resolve(String lexeme) {
        if (CONSTANT_LITERALS.containsKey(lexeme)) {
            return CONSTANT_LITERALS.get(lexeme);
        }
        return ALL.getOrDefault(lexeme, Type.IDENTIFIER);
    }

    public static boolean contains(String lexeme) {
        return ALL.containsKey(lexeme) || CONSTANT_LITERALS.containsKey(lexeme);
    }
}
