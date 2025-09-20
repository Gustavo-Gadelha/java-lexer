package edu.fafic.automata;

import edu.fafic.core.LexingContext;
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

            if (Punctuation.isKnown(ch)) {
                ctx.append(ch);
                return PUNCTUATION;
            }

            if (Alphabet.isIdentifierStart(ch)) {
                ctx.append(ch);
                return KEYWORD_OR_IDENTIFIER;
            }
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return INTEGER_LITERAL;
            }
            if (Alphabet.isSingleQuote(ch)) {
                ctx.append(ch);
                return CHARACTER_LITERAL;
            }
            if (Alphabet.isDoubleQuote(ch)) {
                ctx.append(ch);
                return STRING_LITERAL;
            }

            if (Operators.isEquals(ch)) {
                ctx.append(ch);
                return ASSIGNMENT;
            }
            if (Operators.isTernary(ch)) {
                ctx.append(ch);
                return TERNARY;
            }
            if (Operators.isArithmeticStart(ch)) {
                ctx.append(ch);
                return ARITHMETIC;
            }
            if (Operators.isLogicalStart(ch)) {
                ctx.append(ch);
                return LOGICAL;
            }
            if (Operators.isRelationalStart(ch)) {
                ctx.append(ch);
                return RELATIONAL;
            }
            if (Operators.isBitwise(ch)) {
                ctx.append(ch);
                return BITWISE;
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
            if (Alphabet.isWhitespace(ch) || Alphabet.isEOF(ch) || Punctuation.isKnown(ch)) {
                Type type = Keywords.resolve(ctx.lexeme());
                ctx.unread(ch);
                ctx.emit(type);
                return FINAL;
            }

            return INVALID;
        }
    },

    INTEGER_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return INTEGER_LITERAL;
            }
            if (Alphabet.isDecimalPoint(ch)) {
                ctx.append(ch);
                return FLOAT_LITERAL;
            }

            if (Alphabet.isWhitespace(ch) || Alphabet.isEOF(ch) || Punctuation.isKnown(ch)) {
                ctx.unread(ch);
                ctx.emit(Type.LITERAL_INTEGER);
                return FINAL;
            }

            return INVALID;
        }
    },

    FLOAT_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return FLOAT_LITERAL;
            }

            if (Alphabet.isWhitespace(ch) || Alphabet.isEOF(ch) || Punctuation.isKnown(ch)) {
                ctx.unread(ch);
                ctx.emit(Type.LITERAL_FLOAT);
                return FINAL;
            }

            return INVALID;
        }
    },

    COMMENT {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isEOF(ch)) {
                return EOF;
            }
            if (Alphabet.isNewline(ch)) {
                return INITIAL;
            }

            return COMMENT;
        }
    },

    BLOCK_COMMENT {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isEOF(ch)) {
                return INVALID;
            }
            if (ctx.last() == '*' && ch == '/') {
                return INITIAL;
            }

            return BLOCK_COMMENT;
        }
    },

    CHARACTER_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isNewline(ch) || Alphabet.isEOF(ch)) {
                return INVALID;
            }
            if (ctx.last() != '\\' && ch == '\\') {
                ctx.append(ch);
                return CHARACTER_LITERAL;
            }

            ctx.append(ch);
            return CHARACTER_LITERAL_END;
        }
    },

    STRING_LITERAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isEOF(ch)) {
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

    CHARACTER_LITERAL_END {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (Alphabet.isSingleQuote(ch)) {
                ctx.append(ch);
                ctx.emit(Type.LITERAL_CHAR);
                return FINAL;
            }

            return INVALID;
        }
    },

    ASSIGNMENT {
        @Override
        public State accept(LexingContext ctx, int ch) {
            if (ch == '=') {
                ctx.append(ch);
                return RELATIONAL;
            }

            ctx.unread(ch);
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

            if (last == '/') {
                if (ch == '/') {
                    ctx.append(ch);
                    return COMMENT;
                }
                if (ch == '*') {
                    ctx.append(ch);
                    return BLOCK_COMMENT;
                }
            }

            ctx.unread(ch);
            ctx.emit(Operators.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    LOGICAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            int last = ctx.last();

            if (last == '!') {
                if (ch == '=') {
                    ctx.append(ch);
                    return RELATIONAL;
                }
                ctx.unread(ch);
                ctx.emit(Operators.resolve(ctx.lexeme()));
                return FINAL;
            }

            if (ch == last) {
                ctx.append(ch);
                ctx.emit(Operators.resolve(ctx.lexeme()));
                return FINAL;
            }

            return INVALID;
        }
    },

    RELATIONAL {
        @Override
        public State accept(LexingContext ctx, int ch) {
            int last = ctx.last();

            if (last == '<' && ch == '=' || last == '>' && ch == '=') {
                ctx.append(ch);
                ctx.emit(Operators.resolve(ctx.lexeme()));
                return FINAL;
            }

            ctx.unread(ch);
            ctx.emit(Operators.resolve(ctx.lexeme()));
            return FINAL;
        }
    },

    BITWISE {
        @Override
        public State accept(LexingContext ctx, int ch) {
            ctx.unread(ch);
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

    FINAL,

    INVALID {
        @Override
        public State accept(LexingContext ctx, int ch) {
            // TODO: Implementar um log de erros mais detalhado com LexicalException
            throw new RuntimeException("O analisador léxico chegou em um estado inválido");
        }
    };

    @Override
    public State accept(LexingContext ctx, int ch) {
        return INVALID;
    }
}
