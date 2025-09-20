package edu.fafic.vocabulary;

import edu.fafic.token.Type;

import java.util.Map;
import java.util.Set;

public final class Operators {

    public static final Character EQUALS = '=';

    public static final Character TERNARY = '?';

    public static final Set<Character> ARITHMETIC_START = Set.of(
            '+', '-', '*', '/', '%'
    );

    public static final Set<Character> LOGICAL_START = Set.of(
            '&', '|', '!'
    );

    public static final Set<Character> RELATIONAL_START = Set.of(
            '>', '<'
    );

    public static final Set<Character> BITWISE_ONLY = Set.of(
            '^', '~'
    );

    private static final Map<String, Type> ALL = Map.ofEntries(
            // Ternary operator
            Map.entry("?", Type.TERNARY),
            // Assignment operator
            Map.entry("=", Type.ASSIGN),
            // Relational operators
            Map.entry("==", Type.RELATIONAL_EQUALS),
            Map.entry("!=", Type.RELATIONAL_NOT_EQUALS),
            Map.entry("<", Type.RELATIONAL_LESS),
            Map.entry("<=", Type.RELATIONAL_LESS_EQUAL),
            Map.entry(">", Type.RELATIONAL_GREATER),
            Map.entry(">=", Type.RELATIONAL_GREATER_EQUAL),
            // Logical operators
            Map.entry("&&", Type.LOGICAL_AND),
            Map.entry("||", Type.LOGICAL_OR),
            Map.entry("!", Type.LOGICAL_NOT),
            // Bitwise operators
            Map.entry("&", Type.BITWISE_AND),
            Map.entry("|", Type.BITWISE_OR),
            Map.entry("^", Type.BITWISE_XOR),
            Map.entry("~", Type.BITWISE_NOT),
            // Arithmetic operators
            Map.entry("+", Type.ADD),
            Map.entry("-", Type.SUB),
            Map.entry("*", Type.MUL),
            Map.entry("/", Type.DIV),
            Map.entry("%", Type.MOD),
            // Increment and Decrement operators
            Map.entry("++", Type.INCREMENT),
            Map.entry("--", Type.DECREMENT),
            // Compound assignment operators
            Map.entry("+=", Type.ADD_EQUALS),
            Map.entry("-=", Type.SUB_EQUALS),
            Map.entry("*=", Type.MUL_EQUALS),
            Map.entry("/=", Type.DIV_EQUALS),
            Map.entry("%=", Type.MOD_EQUALS),
            // Arrow function
            Map.entry("->", Type.ARROW)
    );

    private Operators() {
    }

    public static Type resolve(String lexeme) {
        return ALL.getOrDefault(lexeme, Type.INVALID);
    }

    public static boolean isEquals(int ch) {
        return ch == EQUALS;
    }

    public static boolean isTernary(int ch) {
        return ch == TERNARY;
    }

    public static boolean isArithmeticStart(int ch) {
        return ARITHMETIC_START.contains((char) ch);
    }

    public static boolean isLogicalStart(int ch) {
        return LOGICAL_START.contains((char) ch);
    }

    public static boolean isRelationalStart(int ch) {
        return RELATIONAL_START.contains((char) ch);
    }

    public static boolean isBitwise(int ch) {
        return BITWISE_ONLY.contains((char) ch);
    }

    public static boolean isKnown(String lexeme) {
        return ALL.containsKey(lexeme);
    }
}
