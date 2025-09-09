package edu.fafic.vocabulary;

public class Alphabet {

    public static boolean isLetter(int symbol) {
        return Character.isLetter(symbol);
    }

    public static boolean isDigit(int symbol) {
        return Character.isDigit(symbol);
    }

    public static boolean isLetterOrDigit(int symbol) {
        return Character.isLetterOrDigit(symbol);
    }

    public static boolean isWhitespace(int symbol) {
        return Character.isWhitespace(symbol) || Alphabet.isLineSeparator(symbol);
    }

    public static boolean isLineSeparator(int symbol) {
        return symbol == '\r' || symbol == '\n';
    }

    public static boolean isUnderline(int symbol) {
        return symbol == '_';
    }

    public static boolean isDot(int symbol) {
        return symbol == '.';
    }

    public static boolean isDoubleQuotes(int symbol) {
        return symbol == '"';
    }
}
