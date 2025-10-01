package edu.fafic.automata;

import edu.fafic.core.LexingContext;
import edu.fafic.exception.LexicalException;
import edu.fafic.token.Type;
import edu.fafic.vocabulary.Alphabet;
import edu.fafic.vocabulary.Keywords;
import edu.fafic.vocabulary.Operators;
import edu.fafic.vocabulary.Punctuation;

public enum States implements State {
    INITIAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isEOF(ch)) {
                return EOF;
            }
            if (Alphabet.isWhitespace(ch)) {
                return INITIAL;
            }

            if (Punctuation.contains(ch)) {
                ctx.append(ch);
                return PUNCTUATION;
            }

            if (Alphabet.isIdentifierStart(ch)) {
                ctx.append(ch);
                return KEYWORD_OR_IDENTIFIER;
            }
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return NUMBER_LITERAL;
            }
            if (Alphabet.isSingleQuote(ch)) {
                ctx.append(ch);
                return CHARACTER_LITERAL;
            }
            if (Alphabet.isDoubleQuote(ch)) {
                ctx.append(ch);
                return STRING_LITERAL;
            }

            if (Operators.isAssignment(ch)) {
                ctx.append(ch);
                return ASSIGNMENT;
            }
            if (Operators.isTernary(ch)) {
                ctx.append(ch);
                return TERNARY;
            }
            if (Operators.isArithmetic(ch)) {
                ctx.append(ch);
                return ARITHMETIC;
            }
            if (Operators.isRelational(ch)) { // Order would be important here
                ctx.append(ch);
                return RELATIONAL;
            }
            if (Operators.isBitwise(ch)) { // Order is important here
                ctx.append(ch);
                return BITWISE;
            }
            if (Operators.isLogical(ch)) { // Order is important here
                ctx.append(ch);
                return LOGICAL;
            }

            return INVALID;
        }
    },

    PUNCTUATION {
        @Override
        public State accept(LexingContext ctx, int ch) {
            ctx.unread(ch);
            ctx.emit(Punctuation.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    KEYWORD_OR_IDENTIFIER {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isIdentifierPart(ch)) {
                ctx.append(ch);
                return KEYWORD_OR_IDENTIFIER;
            }

            ctx.unread(ch);
            ctx.emit(Keywords.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    NUMBER_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isUnderline(ch)) {
                return NUMBER_LITERAL;
            }
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return NUMBER_LITERAL;
            }
            if (Alphabet.isDecimalPoint(ch)) {
                ctx.append(ch);
                return DOUBLE_LITERAL;
            }

            if (ch == 'l' || ch == 'L') {
                ctx.append(ch);
                ctx.emit(Type.LITERAL_LONG);
                return FINAL;
            }
            if (ch == 'f' || ch == 'F') {
                ctx.append(ch);
                ctx.emit(Type.LITERAL_FLOAT);
                return FINAL;
            }
            if (ch == 'd' || ch == 'D') {
                ctx.append(ch);
                ctx.emit(Type.LITERAL_DOUBLE);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Alphabet.isEOF(ch) || Punctuation.contains(ch)) {
                ctx.unread(ch);
                ctx.emit(Type.LITERAL_INTEGER);
                return FINAL;
            }

            return INVALID;
        }
    },

    DOUBLE_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isUnderline(ch)) {
                return NUMBER_LITERAL;
            }
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return DOUBLE_LITERAL;
            }

            if (Alphabet.isWhitespace(ch) || Alphabet.isEOF(ch) || Punctuation.contains(ch)) {
                ctx.unread(ch);
                ctx.emit(Type.LITERAL_DOUBLE);
                return FINAL;
            }

            return INVALID;
        }
    },

    COMMENT {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isNewline(ch)) {
                ctx.clear();
                return INITIAL;
            }
            if (Alphabet.isEOF(ch)) {
                return EOF;
            }

            return COMMENT;
        }
    },

    BLOCK_COMMENT {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (ch == '*' && ctx.peek() == '/') {
                ctx.read();
                ctx.clear();
                return INITIAL;
            }
            if (Alphabet.isEOF(ch)) {
                return INVALID;
            }

            return BLOCK_COMMENT;
        }
    },

    CHARACTER_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            ctx.append(ch);

            if (Alphabet.isSingleQuote(ch)) {
                ctx.emit(Type.LITERAL_CHAR);
                return FINAL;
            }

            int next = ctx.read();

            if (Alphabet.isSingleQuote(next)) {
                ctx.append(next);
                ctx.emit(Type.LITERAL_CHAR);
                return FINAL;
            }

            return INVALID;
        }
    },

    STRING_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isNewline(ch) || Alphabet.isEOF(ch)) {
                return INVALID;
            }

            if (Alphabet.isDoubleQuote(ch)) {
                ctx.append(ch);
                ctx.emit(Type.LITERAL_STRING);
                return FINAL;
            }

            ctx.append(ch);
            return STRING_LITERAL;
        }
    },

    ASSIGNMENT {
        @Override
        public State accept(LexingContext ctx, int ch) {
            ctx.unread(ch);

            if (ch == '=') {
                return RELATIONAL;
            }

            ctx.emit(Operators.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    TERNARY {
        @Override
        public State accept(LexingContext ctx, int ch) {
            ctx.unread(ch);
            ctx.emit(Type.TERNARY);
            return FINAL;
        }
    },

    ARITHMETIC {
        @Override
        public State accept(LexingContext ctx, int ch) {
            int last = ctx.last();

            if (last == '/' && ch == '/') {
                return COMMENT;
            }
            if (last == '/' && ch == '*') {
                return BLOCK_COMMENT;
            }

            if (ch == '=' || (last == '+' && ch == '+') || (last == '-' && ch == '-') || (last == '-' && ch == '>')) {
                ctx.append(ch);
            } else {
                ctx.unread(ch);
            }

            ctx.emit(Operators.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    LOGICAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            int last = ctx.last();

            if ((last == '!' && ch == '=')) {
                ctx.append(ch);
                return RELATIONAL;
            }

            if ((last == '&' && ch == '&') || (last == '|' && ch == '|')) {
                ctx.append(ch);
            } else {
                ctx.unread(ch);
            }

            ctx.emit(Operators.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    RELATIONAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (ch == '=') {
                ctx.append(ch);
            } else {
                ctx.unread(ch);
            }

            ctx.emit(Operators.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    BITWISE {
        @Override
        public State accept(LexingContext ctx, int ch) {
            int last = ctx.last();

            if ((last == '&' && ch == '&') || (last == '|' && ch == '|')) {
                ctx.unread(ch);
                return LOGICAL;
            }

            if (ch == '=') {
                ctx.append(ch);
            } else {
                ctx.unread(ch);
            }

            ctx.emit(Operators.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    EOF {
        @Override
        public State accept(LexingContext ctx, int ch) {
            ctx.emit(Type.EOF);
            return FINAL;
        }
    },

    FINAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            throw new LexicalException("O Lexer tentou executar o estado final");
        }
    },

    INVALID {
        @Override
        public State accept(LexingContext ctx, int ch) {
            ctx.error("O analisador léxico chegou em um estado inválido lendo: %s".formatted((char) ctx.last()));
            return FINAL;
        }
    };

    @Override
    public State accept(LexingContext ctx, int ch) {
        return INVALID;
    }
}
