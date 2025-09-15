package edu.fafic.vocabulary;

public class Alphabet {

    public static boolean isLetter(int symbol) {
        return Character.isLetter(symbol);
    }

    public static boolean isDigit(int symbol) {
        return Character.isDigit(symbol);
    }

    public static boolean isWhitespace(int symbol) {
        return Character.isWhitespace(symbol);
    }

    public static boolean isLineSeparator(int symbol) {
        return symbol == '\r' || symbol == '\n';
    }

    public static boolean isUnderline(int symbol) {
        return symbol == '_';
    }

    public static boolean isDecimalSeparator(int symbol) {
        return symbol == '.';
    }

    public static boolean isSingleQuote(int symbol) {
        return symbol == '\'';
    }

    public static boolean isDoubleQuote(int symbol) {
        return symbol == '\"';
    }
}
