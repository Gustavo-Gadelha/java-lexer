package edu.fafic.vocabulary;

import java.util.Set;

public class Symbols {

    public static final Set<String> KEYWORDS = Set.of(
            "char", "int", "true", "false", "void", "if", "else", "do", "while",
            "import", "package", "class", "public", "private", "static", "final"
    ); // TODO: "true" e "false" são Boolean Literals

    public static final Set<Character> PUNCTUATION = Set.of(
            '(', ')', '[', ']', '{', '}', ':', ';', ',', '.'
    );

    public static final Set<Character> ARITHMETIC = Set.of(
            '+', '-', '*', '/'
    );

    public static final Set<String> LOGICAL = Set.of(
            "&&", "||", "!", "?"
    );

    public static final Set<String> RELATIONAL = Set.of(
            "<", ">", "<=", ">=", "==", "!="
    );

    public static final Character ASSIGNMENT = '=';

    public static final Character EOL = ';';

    public static boolean isKeyword(String lexeme) {
        return KEYWORDS.contains(lexeme);
    }

    public static boolean isArithmetic(int ch) {
        return ARITHMETIC.contains((char) ch);
    }

    public static boolean isLogical(String lexeme) {
        return LOGICAL.contains(lexeme);
    }

    public static boolean isLogical(int ch) {
        return ch == '&' || ch == '|' || ch == '!' || ch == '?';
    }

    public static boolean isRelational(String lexeme) {
        return RELATIONAL.contains(lexeme);
    }

    public static boolean isRelational(int ch) {
        return ch == '>' || ch == '<';
    }

    public static boolean isPunctuation(int ch) {
        return PUNCTUATION.contains((char) ch);
    }

    public static boolean isAssignment(int ch) {
        return ch == ASSIGNMENT;
    }

    public static boolean isBooleanLiteral(String lexeme) {
        return lexeme.equals("true") || lexeme.equals("false");
    }

    public static boolean isEOL(int ch) {
        return ch == EOL;
    }

    public static boolean isEOF(int ch) {
        return ch == -1;
    }
}
