package edu.fafic.core;

import edu.fafic.automata.State;
import edu.fafic.automata.States;
import edu.fafic.exception.LexicalException;
import edu.fafic.token.Token;
import edu.fafic.token.Type;
import edu.fafic.vocabulary.Alphabet;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class Lexer {

    private final PushbackReader reader;
    private final StringBuffer buffer;
    private final LexingContext ctx;

    private Token emitted;

    public Lexer(Reader in) {
        this.reader = new PushbackReader(in);
        this.buffer = new StringBuffer();
        this.ctx = new LexingContextImpl();
        this.emitted = null;
    }

    public Token nextToken() throws IOException {
        emitted = null;
        State current = States.INITIAL;

        do {
            int ch = ctx.read();
            current = current.accept(ctx, ch);
        } while (current != States.FINAL && emitted == null);

        ctx.clear();
        return emitted;
    }

    private class LexingContextImpl implements LexingContext {
        @Override
        public int read() {
            try {
                return reader.read();
            } catch (IOException e) {
                throw new RuntimeException("I/O error while reading", e);
            }
        }

        @Override
        public void unread(int ch) {
            if (ch == Alphabet.EOF) return;

            try {
                reader.unread(ch);
            } catch (IOException e) {
                throw new RuntimeException("I/O error while unreading", e);
            }
        }

        @Override
        public int peek() {
            int ch = read();
            if (ch != Alphabet.EOF) unread(ch);
            return ch;
        }

        @Override
        public void append(int ch) {
            buffer.appendCodePoint(ch);
        }

        @Override
        public int first() {
            if (buffer.isEmpty()) return -1;
            return buffer.codePointAt(0);
        }

        @Override
        public int last() {
            if (buffer.isEmpty()) return -1;
            int n = buffer.length();
            return buffer.codePointBefore(n);
        }

        @Override
        public void clear() {
            buffer.setLength(0);
        }

        @Override
        public String lexeme() {
            return buffer.toString();
        }

        @Override
        public void emit(Type type) {
            emitted = new Token(type, lexeme());
            clear();
        }

        @Override
        public void error(String message) {
            System.out.printf("Caracteres no buffer: %s%n", buffer);
            throw new LexicalException("Lexing error: " + message);
        }
    }
}
