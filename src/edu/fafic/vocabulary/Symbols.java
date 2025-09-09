package edu.fafic.vocabulary;

import java.util.Set;

public class Symbols {

    public static final Set<Character> arithmetic = Set.of(
            '+', '-', '/', '*'
    );

    public static final Set<Character> logical = Set.of(
            '&', '|', '!', '?'
    );

    public static final Set<Character> relational = Set.of(
            '<', '>'
    );

    public static final Set<Character> punctuation = Set.of(
            '(', ')', '[', ']', '{', '}', ',', '.', ':', ';'
    );

    public static final Character ASSIGNMENT = '=';

    public static final Character EOL = ';';

    public static boolean isAssignment(int symbol) {
        return symbol == ASSIGNMENT;
    }

    public static boolean isArithmetic(int symbol) {
        return Symbols.arithmetic.contains((char) symbol);
    }

    public static boolean isLogical(int symbol) {
        return Symbols.logical.contains((char) symbol);
    }

    public static boolean isRelational(int symbol) {
        return Symbols.relational.contains((char) symbol);
    }

    public static boolean isPunctuation(int symbol) {
        return Symbols.punctuation.contains((char) symbol);
    }

    public static boolean isEOL(int symbol) {
        return symbol == EOL;
    }

    public static boolean isEOF(int symbol) {
        return symbol == -1;
    }
}
