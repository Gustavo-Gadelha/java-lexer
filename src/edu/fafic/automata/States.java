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

            if (Symbols.isArithmetic(ch)) {
                ctx.append(ch);
                return ARITHMETIC;
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
                return STRING_LITERAL_START;
            }
            if (Symbols.isLogical(ch)) {
                ctx.append(ch);
                return LOGICAL_SINGLE;
            }
            if (Symbols.isRelational(ch)) {
                ctx.append(ch);
                return RELATIONAL_SINGLE;
            }

            return INVALID;
        }
    },

    ASSIGNMENT {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (ch == '=') {
                ctx.append(ch);
                return RELATIONAL_DOUBLE;
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
            if (ch == '*') {
                ctx.append(ch);
                return BLOCK_COMMENT_END;
            }

            ctx.append(ch);
            return BLOCK_COMMENT;
        }
    },

    BLOCK_COMMENT_END {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isEOF(ch)) {
                return INVALID;
            }
            if (ch == '/') {
                ctx.append(ch);
                ctx.emit(Type.COMMENT);
                return FINAL;
            }

            ctx.append(ch);
            return BLOCK_COMMENT;
        }
    },

    STRING_LITERAL_START { // TODO: Não permite escape de caracteres

        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isEOF(ch)) {
                return INVALID; // Todos os caracteres foram lidos e um " não foi encontrada
            }
            if (Alphabet.isDoubleQuotes(ch)) {
                ctx.unread(ch); // Será consumido no próximo estado
                return STRING_LITERAL_END;
            }

            ctx.append(ch);
            return STRING_LITERAL_START;
        }
    },

    STRING_LITERAL_END {
        @Override
        public State accept(StateContext ctx, int ch) {
            ctx.append(ch); // Concatena " ao buffer
            ctx.emit(Type.STRING_LITERAL);
            return FINAL;
        }
    },

    LOGICAL_SINGLE {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isLogical(ch)) {
                ctx.append(ch);
                return LOGICAL_DOUBLE;
            }

            String lexeme = ctx.currentLexeme();
            if (Symbols.isLogical(lexeme)) {
                ctx.unread(ch);
                ctx.emit(Type.LOGICAL);
                return FINAL;
            }

            return INVALID;
        }
    },

    LOGICAL_DOUBLE {
        @Override
        public State accept(StateContext ctx, int ch) {
            String lexeme = ctx.currentLexeme();
            if (Symbols.isLogical(lexeme)) {
                ctx.unread(ch);
                ctx.emit(Type.LOGICAL);
                return FINAL;
            }

            return INVALID;
        }
    },

    RELATIONAL_SINGLE {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isRelational(ch)) {
                ctx.append(ch);
                return RELATIONAL_DOUBLE;
            }

            ctx.unread(ch);
            ctx.emit(Type.RELATIONAL);
            return FINAL;
        }
    },

    RELATIONAL_DOUBLE {
        @Override
        public State accept(StateContext ctx, int ch) {
            String lexeme = ctx.currentLexeme();
            if (Symbols.isRelational(lexeme)) {
                ctx.unread(ch);
                ctx.emit(Type.RELATIONAL);
                return FINAL;
            }

            return INVALID;
        }
    },

    ARITHMETIC {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isAssignment(ch)) {
                ctx.append(ch);
                return ASSIGNMENT;
            }
            if (ch == '/') {
                ctx.append(ch);
                return COMMENT;
            }
            if (ch == '*') {
                ctx.append(ch);
                return BLOCK_COMMENT;
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
