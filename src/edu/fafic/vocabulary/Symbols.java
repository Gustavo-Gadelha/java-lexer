package edu.fafic.vocabulary;

import java.util.Set;

public class Symbols {

    public static final Set<String> KEYWORDS = Set.of(
            "char", "int", "true", "false", "void", "if", "else", "do", "while",
            "import", "package", "class", "public", "private", "static", "final"
    ); // TODO: "true" e "false" são Boolean Literals

    public static final Set<Character> PUNCTUATION = Set.of(
            '(', ')', '[', ']', '{', '}', '<', '>', ':', ';', ',', '.'
    );

    public static final Set<Character> ARITHMETIC = Set.of(
            '+', '-', '*', '/'
    );

    public static final Character TERNARY = '?';

    public static final Character ASSIGNMENT = '=';

    public static final Character EOL = ';';

    public static final int EOF = -1;

    public static boolean isKeyword(String lexeme) {
        return KEYWORDS.contains(lexeme);
    }

    public static boolean isPunctuation(int ch) {
        return PUNCTUATION.contains((char) ch);
    }

    public static boolean isArithmetic(int ch) {
        return ARITHMETIC.contains((char) ch);
    }

    public static boolean isLogical(int ch) {
        return ch == '&' || ch == '|' || ch == '!';
    }

    public static boolean isRelational(int ch) {
        return ch == '>' || ch == '<';
    }

    public static boolean isTernary(int ch) {
        return ch == TERNARY;
    }

    public static boolean isAssignment(int ch) {
        return ch == ASSIGNMENT;
    }

    public static boolean isEOL(int ch) {
        return ch == EOL;
    }

    public static boolean isEOF(int ch) {
        return ch == EOF;
    }

    public static boolean isWhitespace(int ch) {
        return Alphabet.isWhitespace(ch) || Alphabet.isLineSeparator(ch);
    }

    public static boolean isTerminator(int ch) {
        return Symbols.isWhitespace(ch) || Symbols.isEOL(ch) || Symbols.isEOF(ch);
    }
}
