package edu.fafic.lexer;

import edu.fafic.automata.State;
import edu.fafic.automata.StateContext;
import edu.fafic.automata.States;
import edu.fafic.token.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final List<Token> tokens;
    private final BufferedReader reader;
    private final Buffer buffer;
    private final Lookahead lookahead;

    public Lexer(InputStream in) {
        this.tokens = new ArrayList<>();
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.buffer = new Buffer();
        this.lookahead = new Lookahead();
    }

    public int read() {
        try {
            return lookahead.isEmpty() ? reader.read() : lookahead.consume();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Token readToken() {
        State current = States.INITIAL;
        StateContext ctx = new StateContext(this.buffer, this.lookahead, this::accept);

        do {
            int ch = this.read();
            current = current.accept(ctx, ch);
        } while (current != States.FINAL);

        this.buffer.clear();
        return this.tokens.getLast();
    }

    public void accept(Token token) {
        this.tokens.add(token);
    }

    public boolean hasEOF() {
        if (this.tokens.isEmpty()) {
            return false;
        }
        return this.tokens.getLast().isEOF();
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
