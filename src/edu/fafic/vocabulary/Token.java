package edu.fafic.vocabulary;

import java.util.Map;

public record Token(Category category, String lexeme) {

    // Reserved words (keywords)
    public static final Map<String, Category> keywords = Map.ofEntries(
            Map.entry("char", Category.CHAR),
            Map.entry("int", Category.INT),
            Map.entry("void", Category.VOID),
            Map.entry("true", Category.TRUE),
            Map.entry("false", Category.FALSE),
            Map.entry("if", Category.IF),
            Map.entry("else", Category.ELSE),
            Map.entry("while", Category.WHILE)
    );

    // Punctuation
    public static final Map<String, Category> punctuation = Map.ofEntries(
            Map.entry("(", Category.OP),
            Map.entry(")", Category.CP),
            Map.entry("[", Category.OB),
            Map.entry("]", Category.CB),
            Map.entry("{", Category.OK),
            Map.entry("}", Category.CK),
            Map.entry(":", Category.CL),
            Map.entry(";", Category.SC),
            Map.entry(",", Category.CO),
            Map.entry(".", Category.DOT)
    );

    // Arithmetic operators
    public static final Map<String, Category> arithmetic = Map.ofEntries(
            Map.entry("+", Category.ADD),
            Map.entry("-", Category.SUB),
            Map.entry("/", Category.DIV),
            Map.entry("*", Category.MUL)
    );

    // Logical operators
    public static final Map<String, Category> logical = Map.ofEntries(
            Map.entry("&&", Category.AND),
            Map.entry("||", Category.OR),
            Map.entry("!", Category.NOT),
            Map.entry("?", Category.TER)
    );

    // Relational operators
    public static final Map<String, Category> relational = Map.ofEntries(
            Map.entry("<", Category.LT),
            Map.entry("<=", Category.LE),
            Map.entry(">", Category.GT),
            Map.entry(">=", Category.GE),
            Map.entry("==", Category.EQ),
            Map.entry("!=", Category.NE)
    );

    // Assignment operators
    public static final Map<String, Category> assignment = Map.ofEntries(
            Map.entry("=", Category.AS)
    );


    public Token(Category category, int codePoint) {
        this(category, String.valueOf((char) codePoint));
    }

    public Token(Category category) {
        this(category, null);
    }

    public boolean isEOF() {
        return this.category == Category.EOF;
    }

    @Override
    public String toString() {
        return "<%s, %s>".formatted(category, lexeme);
    }
}
