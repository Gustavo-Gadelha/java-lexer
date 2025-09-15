package edu.fafic.automata;

import edu.fafic.token.Type;
import edu.fafic.vocabulary.Alphabet;
import edu.fafic.vocabulary.Symbols;

public enum States implements State {
    INITIAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isEOF(ch)) {
                return EOF;
            }
            if (Alphabet.isWhitespace(ch)) {
                return INITIAL;
            }

            if (Symbols.isPunctuation(ch)) {
                ctx.append(ch);
                return PUNCTUATION;
            }
            if (Symbols.isAssignment(ch)) {
                ctx.append(ch);
                return ASSIGNMENT;
            }
            if (Alphabet.isLetter(ch) || Alphabet.isUnderline(ch)) {
                ctx.append(ch);
                return KEYWORD_OR_IDENTIFIER;
            }
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return INTEGER_LITERAL;
            }
            if (Alphabet.isDoubleQuotes(ch)) {
                ctx.append(ch);
                return STRING_LITERAL;
            }
            if (Symbols.isLogical(ch)) {
                ctx.append(ch);
                return LOGICAL;
            }
            if (Symbols.isRelational(ch)) {
                ctx.append(ch);
                return RELATIONAL;
            }
            if (Symbols.isArithmetic(ch)) {
                ctx.append(ch);
                return ARITHMETIC;
            }

            return INVALID;
        }
    },

    ASSIGNMENT {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (ch == '=') {
                ctx.append(ch);
                return RELATIONAL;
            }

            ctx.unread(ch);
            ctx.emit(Type.AS);
            return FINAL;
        }
    },

    KEYWORD_OR_IDENTIFIER {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isLetter(ch) || Alphabet.isDigit(ch) || Alphabet.isUnderline(ch)) {
                ctx.append(ch);
                return KEYWORD_OR_IDENTIFIER;
            }

            Type type = Symbols.isKeyword(ctx.currentLexeme())
                    ? Type.KEYWORD
                    : Type.ID;

            if (Symbols.isPunctuation(ch)) {
                ctx.unread(ch);
                ctx.emit(type);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Symbols.isEOF(ch)) {
                ctx.emit(type);
                return FINAL;
            }

            return INVALID;
        }
    },

    INTEGER_LITERAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return INTEGER_LITERAL;
            }

            if (Alphabet.isDot(ch)) {
                ctx.append(ch);
                return FLOAT_LITERAL;
            }

            if (Symbols.isEOL(ch)) {
                ctx.unread(ch);
                ctx.emit(Type.INTEGER_LITERAL);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Symbols.isEOF(ch)) {
                ctx.emit(Type.INTEGER_LITERAL);
                return FINAL;
            }

            return INVALID;
        }
    },

    FLOAT_LITERAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return FLOAT_LITERAL;
            }

            if (Symbols.isEOL(ch)) {
                ctx.unread(ch);
                ctx.emit(Type.FLOAT_LITERAL);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Symbols.isEOF(ch)) {
                ctx.emit(Type.FLOAT_LITERAL);
                return FINAL;
            }

            return INVALID;
        }
    },

    COMMENT {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isLineSeparator(ch)) {
                ctx.emit(Type.COMMENT);
                return FINAL;
            }

            ctx.append(ch);
            return COMMENT;
        }
    },

    BLOCK_COMMENT {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isEOF(ch)) {
                return INVALID;
            }

            int last = ctx.last();

            if (last == '*' && ch == '/') {
                ctx.append(ch);
                ctx.emit(Type.COMMENT);
                return FINAL;
            }

            ctx.append(ch);
            return BLOCK_COMMENT;
        }
    },

    STRING_LITERAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isDoubleQuotes(ch)) {
                ctx.append(ch);
                ctx.emit(Type.STRING_LITERAL);
                return FINAL;
            }
            if (Symbols.isEOF(ch)) {
                return INVALID;
            }

            ctx.append(ch);
            return STRING_LITERAL;
        }
    },

    LOGICAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            int last = ctx.last();

            if (last == '!') {
                if (ch == '=') {
                    ctx.append(ch);
                    return RELATIONAL;
                }
                ctx.unread(ch);
                ctx.emit(Type.LOGICAL);
                return FINAL;
            }

            if (ch == last) {
                ctx.append(ch);
                ctx.emit(Type.LOGICAL);
                return FINAL;
            }

            return INVALID;
        }
    },

    RELATIONAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            int last = ctx.last();

            if (last == '<' && ch == '=' || last == '>' && ch == '=') {
                ctx.append(ch);
                ctx.emit(Type.RELATIONAL);
                return FINAL;
            }

            ctx.unread(ch);
            ctx.emit(Type.RELATIONAL);
            return FINAL;
        }
    },

    ARITHMETIC {
        @Override
        public State accept(StateContext ctx, int ch) {
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
            ctx.emit(Type.ARITHMETIC);
            return FINAL;
        }
    },

    PUNCTUATION {
        @Override
        public State accept(StateContext ctx, int ch) {
            ctx.unread(ch);
            ctx.emit(Type.PUNCTUATION);
            return FINAL;
        }
    },

    EOF {
        @Override
        public State accept(StateContext ctx, int ch) {
            ctx.emit(Type.EOF);
            return FINAL;
        }
    },

    FINAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            return null;
        }
    },

    INVALID {
        @Override
        public State accept(StateContext ctx, int ch) {
            // TODO: Implementar um log de erros mais detalhado com LexicalException
            throw new RuntimeException("O analisador léxico chegou em um estado inválido");
        }
    };

    @Override
    public State accept(StateContext ctx, int ch) {
        return INVALID;
    }
}
