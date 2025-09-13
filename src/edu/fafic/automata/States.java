package edu.fafic.automata;

import edu.fafic.token.Token;
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
            if (Alphabet.isLetter(ch)) {
                ctx.append(ch);
                return KEYWORD_OR_IDENTIFIER;
            }
            if (Alphabet.isUnderline(ch)) {
                ctx.append(ch);
                return IDENTIFIER;
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
            ctx.emit(new Token(Type.AS, ctx.currentLexeme()));
            return FINAL;
        }
    },

    KEYWORD_OR_IDENTIFIER {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isLetter(ch)) {
                ctx.append(ch);
                return KEYWORD_OR_IDENTIFIER;
            }
            if (Alphabet.isUnderline(ch) || Alphabet.isDigit(ch)) {
                ctx.append(ch);
                return IDENTIFIER;
            }

            String lexeme = ctx.currentLexeme();
            Type type = Symbols.isKeyword(lexeme) ? Type.KEYWORD : Type.ID;
            Token token = new Token(type, lexeme);

            if (Symbols.isPunctuation(ch)) {
                ctx.unread(ch);
                ctx.emit(token);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Symbols.isEOF(ch)) {
                ctx.emit(token);
                return FINAL;
            }

            return INVALID;
        }
    },

    IDENTIFIER {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isLetterOrDigit(ch) || Alphabet.isUnderline(ch)) {
                ctx.append(ch);
                return IDENTIFIER;
            }

            String lexeme = ctx.currentLexeme();
            Token token = new Token(Type.ID, lexeme);

            if (Symbols.isPunctuation(ch)) {
                ctx.unread(ch);
                ctx.emit(token);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Symbols.isEOF(ch)) {
                ctx.emit(token);
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

            String lexeme = ctx.currentLexeme();
            Token token = new Token(Type.INTEGER_LITERAL, lexeme);

            if (Symbols.isEOL(ch)) {
                ctx.unread(ch);
                ctx.emit(token);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Symbols.isEOF(ch)) {
                ctx.emit(token);
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

            String lexeme = ctx.currentLexeme();
            Token token = new Token(Type.FLOAT_LITERAL, lexeme);

            if (Symbols.isEOL(ch)) {
                ctx.unread(ch);
                ctx.emit(token);
                return FINAL;
            }
            if (Alphabet.isWhitespace(ch) || Symbols.isEOF(ch)) {
                ctx.emit(token);
                return FINAL;
            }

            return INVALID;
        }
    },

    COMMENT {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isLineSeparator(ch) || Symbols.isEOL(ch)) {
                if (Symbols.isEOL(ch)) ctx.unread(ch);

                Token token = new Token(Type.COMMENT, ctx.currentLexeme());
                ctx.emit(token);
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
                Token token = new Token(Type.COMMENT, ctx.currentLexeme());
                ctx.emit(token);
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
            Token token = new Token(Type.STRING_LITERAL, ctx.currentLexeme());
            ctx.emit(token);
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
                Token token = new Token(Type.LOGICAL, lexeme);
                ctx.emit(token);
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
                Token token = new Token(Type.LOGICAL, lexeme);
                ctx.emit(token);
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
            Token token = new Token(Type.RELATIONAL, ctx.currentLexeme());
            ctx.emit(token);
            return FINAL;
        }
    },

    RELATIONAL_DOUBLE {
        @Override
        public State accept(StateContext ctx, int ch) {
            String lexeme = ctx.currentLexeme();
            if (Symbols.isRelational(lexeme)) {
                ctx.unread(ch);
                Token token = new Token(Type.RELATIONAL, lexeme);
                ctx.emit(token);
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
            Token token = new Token(Type.ARITHMETIC, ctx.currentLexeme());
            ctx.emit(token);
            return FINAL;
        }
    }, // TODO: Extremamente simples, não lê mais de 1 símbolo

    PUNCTUATION {
        @Override
        public State accept(StateContext ctx, int ch) {
            ctx.unread(ch);
            Token token = new Token(Type.PUNCTUATION, ctx.currentLexeme());
            ctx.emit(token);
            return FINAL;
        }
    }, // TODO: Extremamente simples, talvez não funcione para todos os casos

    EOF {
        @Override
        public State accept(StateContext ctx, int ch) {
            Token token = new Token(Type.EOF, "EOF");
            ctx.emit(token);
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
