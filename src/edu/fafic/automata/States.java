package edu.fafic.automata;

import edu.fafic.vocabulary.Alphabet;
import edu.fafic.vocabulary.Category;
import edu.fafic.vocabulary.Symbols;
import edu.fafic.vocabulary.Token;

public enum States implements State {
    INITIAL {
        @Override
        public State accept(StateContext ctx, int ch) {
            if (Alphabet.isWhitespace(ch)) {
                return INITIAL;
            }

            if (ch == -1) {
                return EOF;
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
            if (Symbols.arithmetic.contains((char) ch)) {
                ctx.append(ch);
                return ARITHMETIC;
            }
            if (Symbols.logical.contains((char) ch)) {
                ctx.append(ch);
                return LOGICAL;
            }
            if (Symbols.relational.contains((char) ch)) {
                ctx.append(ch);
                return RELATIONAL;
            }

            if (Symbols.punctuation.contains((char) ch)) {
                ctx.unread(ch);
                return PUNCTUATION;
            }
            if (Symbols.isAssignment(ch)) {
                ctx.unread(ch);
                return ASSIGNMENT;
            }

            return INVALID;
        }
    },

    ASSIGNMENT {
        @Override
        public State accept(StateContext ctx, int ch) {
            ctx.emit(new Token(Category.AS, ch));
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
            Category category = Token.keywords.getOrDefault(lexeme, Category.ID);
            Token token = new Token(category, lexeme);

            if (Symbols.punctuation.contains((char) ch)) {
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
            Token token = new Token(Category.ID, lexeme);

            if (Symbols.punctuation.contains((char) ch)) {
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
            Token token = new Token(Category.INTEGER_LITERAL, lexeme);

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
            Token token = new Token(Category.FLOAT_LITERAL, lexeme);

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

    COMMENT, // TODO: Implementar reconhecimento de comentários

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
            Token token = new Token(Category.STRING_LITERAL, ctx.currentLexeme());
            ctx.emit(token);
            return FINAL;
        }
    },

    LOGICAL, // TODO: Implementar reconhecimento de operadores lógicos

    RELATIONAL, // TODO: Implementar reconhecimento de operadores relacionais

    ARITHMETIC { // TODO: Extremamente simples, não lê mais de 1 simbolo

        @Override
        public State accept(StateContext ctx, int ch) {
            String lexeme = ctx.currentLexeme();
            Category category = Token.arithmetic.get(lexeme);
            Token token = new Token(category, lexeme);

            if (Symbols.isArithmetic(ch)) {
                ctx.emit(token);
                return FINAL;
            }

            return INVALID;
        }
    },

    PUNCTUATION { // TODO: Extremamente simples, talvez não funcione para todos os casos

        @Override
        public State accept(StateContext ctx, int ch) {
            if (Symbols.isPunctuation(ch)) {
                ctx.append(ch);
                String lexeme = ctx.currentLexeme();
                Category category = Token.punctuation.get(lexeme);
                Token token = new Token(category, lexeme);
                ctx.emit(token);
                return FINAL;
            }

            return INVALID;
        }
    },

    EOF {
        @Override
        public State accept(StateContext ctx, int ch) {
            Token token = new Token(Category.EOF, "EOF");
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
