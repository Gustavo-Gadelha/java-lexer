package edu.fafic.lexer;

import edu.fafic.automata.States;
import edu.fafic.exceptions.LexicalException;
import edu.fafic.vocabulary.Alphabet;
import edu.fafic.vocabulary.Category;
import edu.fafic.vocabulary.Symbols;
import edu.fafic.vocabulary.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Lexer {

    private final Stack<Character> stored;
    private final List<Token> tokens;
    private final StringBuffer buffer;
    private final BufferedReader reader;

    public Lexer(InputStream in) {
        this.stored = new Stack<>();
        this.tokens = new ArrayList<>();
        this.buffer = new StringBuffer();
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public int read() {
        try {
            return stored.isEmpty() ? reader.read() : stored.pop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Token readToken() {
        States state = States.INITIAL;
        Token token = null;

        while (state != States.FINAL) {
            int current = this.read();

            switch (state) {
                case INITIAL -> {
                    if (current == -1 && buffer.isEmpty()) {
                        token = new Token(Category.EOF, "EOF");
                        state = States.FINAL;
                    } else if (Alphabet.isWhitespace(current) && buffer.isEmpty()) {
                        continue;
                    } else if (Alphabet.isLetter(current) || Alphabet.isUnderline(current)) {
                        buffer.appendCodePoint(current);
                        state = States.KEYWORD_OR_IDENTIFIER;
                    } else if (Alphabet.isDigit(current)) {
                        buffer.appendCodePoint(current);
                        state = States.INTEGER_LITERAL;
                    } else if (Symbols.arithmetic.contains((char) current)) {
                        stored.push((char) current);
                        state = States.ARITHMETIC;
                    } else if (Symbols.logical.contains((char) current)) {
                        buffer.appendCodePoint(current);
                        state = States.LOGICAL;
                    } else if (Symbols.relational.contains((char) current)) {
                        buffer.appendCodePoint(current);
                        state = States.RELATIONAL;
                    } else if (Symbols.punctuation.contains((char) current)) {
                        stored.push((char) current);
                        state = States.PUNCTUATION;
                    } else if (Symbols.isAssignment(current)) {
                        token = new Token(Category.AS, current);
                        state = States.FINAL;
                    } else {
                        state = States.INVALID;
                    }
                }

                case KEYWORD_OR_IDENTIFIER -> {
                    if (Alphabet.isLetter(current)) {
                        buffer.appendCodePoint(current);
                    } else if (Alphabet.isUnderline(current) || Alphabet.isDigit(current)) {
                        buffer.appendCodePoint(current);
                        state = States.IDENTIFIER;
                    } else if (Symbols.punctuation.contains((char) current)) {
                        stored.push((char) current);
                        String lexeme = buffer.toString();
                        Category category = Token.keywords.getOrDefault(lexeme, Category.ID);
                        token = new Token(category, lexeme);
                        state = States.FINAL;
                    } else if (Alphabet.isWhitespace(current) || Symbols.isEOF(current)) {
                        String lexeme = buffer.toString();
                        Category category = Token.keywords.getOrDefault(lexeme, Category.ID);
                        token = new Token(category, lexeme);
                        state = States.FINAL;
                    } else {
                        state = States.INVALID;
                    }
                }

                case IDENTIFIER -> {
                    if (Alphabet.isLetterOrDigit(current) || Alphabet.isUnderline(current)) {
                        buffer.appendCodePoint(current);
                    } else if (Symbols.punctuation.contains((char) current)) {
                        stored.push((char) current);
                        token = new Token(Category.ID, buffer.toString());
                        state = States.FINAL;
                    } else if (Alphabet.isWhitespace(current) || Symbols.isEOF(current)) {
                        token = new Token(Category.ID, buffer.toString());
                        state = States.FINAL;
                    } else {
                        state = States.INVALID;
                    }
                }

                case INTEGER_LITERAL -> {
                    if (Alphabet.isDigit(current)) {
                        buffer.appendCodePoint(current);
                    } else if (Alphabet.isDot(current)) {
                        buffer.appendCodePoint(current);
                        state = States.FLOAT_LITERAL;
                    } else if (Symbols.isEOL(current)) {
                        stored.push((char) current);
                        token = new Token(Category.INTEGER_LITERAL, buffer.toString());
                        state = States.FINAL;
                    } else if (Alphabet.isWhitespace(current) || Symbols.isEOF(current)) {
                        token = new Token(Category.INTEGER_LITERAL, buffer.toString());
                        state = States.FINAL;
                    } else {
                        state = States.INVALID;
                    }
                }

                case FLOAT_LITERAL -> {
                    if (Alphabet.isDigit(current)) {
                        buffer.appendCodePoint(current);
                    } else if (Alphabet.isWhitespace(current) || Symbols.isEOF(current) || Symbols.isEOL(current)) {
                        token = new Token(Category.FLOAT_LITERAL, buffer.toString());
                        state = States.FINAL;
                    } else {
                        state = States.INVALID;
                    }
                }

                case ARITHMETIC -> {
                    if (Symbols.arithmetic.contains((char) current)) {
                        String lexeme = String.valueOf((char) current);
                        Category category = Token.arithmetic.get(lexeme);
                        token = new Token(category, lexeme);
                        state = States.FINAL;
                    } else {
                        state = States.INVALID;
                    }
                }

                case LOGICAL -> {
                    throw new RuntimeException("LOGICAL state is WIP");
                }

                case RELATIONAL -> {
                    throw new RuntimeException("RELATIONAL state is WIP");
                }

                case PUNCTUATION -> {
                    if (Symbols.punctuation.contains((char) current)) {
                        String lexeme = String.valueOf((char) current);
                        Category category = Token.punctuation.get(lexeme);
                        token = new Token(category, lexeme);
                        state = States.FINAL;
                    } else {
                        state = States.INVALID;
                    }
                }

                case INVALID -> {
                    throw LexicalException.unexpected(current, this.buffer);
                }
            }

        }

        this.tokens.add(token);
        this.buffer.setLength(0);

        return token;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
