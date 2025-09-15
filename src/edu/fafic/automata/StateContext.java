package edu.fafic.automata;

import edu.fafic.lexer.Buffer;
import edu.fafic.lexer.Lookahead;
import edu.fafic.token.Token;
import edu.fafic.token.Type;

import java.util.function.Consumer;

public record StateContext(Buffer buffer, Lookahead lookahead, Consumer<Token> sink) {

    public void unread(int ch) {
        lookahead.store(ch);
    }

    public void append(int ch) {
        buffer.append(ch);
    }

    public String currentLexeme() {
        return buffer.read();
    }

    public void clearLexeme() {
        buffer.clear();
    }

    public void emit(Type type) {
        Token token = new Token(type, currentLexeme());
        sink.accept(token);
        buffer.clear();
    }
}
