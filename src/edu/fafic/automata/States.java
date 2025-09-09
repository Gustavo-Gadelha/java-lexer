package edu.fafic.automata;

import edu.fafic.lexer.Lexer;

public enum States implements State {
    INITIAL,
    FINAL,
    SYMBOL,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    STRING_LITERAL,
    KEYWORD_OR_IDENTIFIER,
    IDENTIFIER,
    LOGICAL,
    ARITHMETIC,
    RELATIONAL,
    PUNCTUATION,
    INVALID,
    EOF {
        @Override
        public State accept(Lexer lexer, int input) {
            return FINAL;
        }
    };

    @Override
    public State accept(Lexer lexer, int input) {
        return INVALID;
    }
}
