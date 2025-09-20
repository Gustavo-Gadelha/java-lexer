package edu.fafic.vocabulary;

import java.util.Set;

public final class Alphabet {

    public static final int EOF = -1;

    private Alphabet() {
    }

    public static boolean isEOF(int ch) {
        return ch == EOF;
    }

    public static boolean isLetter(int ch) {
        return Character.isLetter(ch);
    }

    public static boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }

    public static boolean isUnderline(int ch) {
        return ch == '_';
    }

    public static boolean isDollar(int ch) {
        return ch == '$';
    }

    public static boolean isIdentifierStart(int ch) {
        return isLetter(ch) || isUnderline(ch) || isDollar(ch);
    }

    public static boolean isIdentifierPart(int ch) {
        return isLetter(ch) || isDigit(ch) || isUnderline(ch) || isDollar(ch);
    }

    public static boolean isWhitespace(int ch) {
        return Character.isWhitespace(ch);
    }

    public static boolean isNewline(int ch) {
        return ch == '\n' || ch == '\r';
    }

    public static boolean isDecimalPoint(int ch) {
        return ch == '.';
    }

    public static boolean isSemicolon(int ch) {
        return ch == ';';
    }

    public static boolean isSingleQuote(int ch) {
        return ch == '\'';
    }

    public static boolean isDoubleQuote(int ch) {
        return ch == '\"';
    }

    private static final Set<Character> OPERATORS = Set.of(
            '+', '-', '*', '/', '%', '=', '&', '|', '^', '!', '<', '>', '~'
    );

    private static final Set<Character> PUNCTUATION = Set.of(
            '(', ')', '{', '}', '[', ']', ';', ',', ':', '?', '@'
    );

    public static boolean isOperator(int ch) {
        return OPERATORS.contains((char) ch);
    }

    public static boolean isPunctuation(int ch) {
        return PUNCTUATION.contains((char) ch);
    }
}
