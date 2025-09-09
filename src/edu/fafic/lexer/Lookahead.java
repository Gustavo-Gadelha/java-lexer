package edu.fafic.lexer;

public class Lookahead {

    public static final int NO_CHAR = -1;

    private int lookahead;

    public Lookahead() {
        lookahead = NO_CHAR;
    }

    public void store(int reading) {
        if (lookahead != NO_CHAR) {
            throw new IllegalStateException("Lookahead already occupied");
        }

        this.lookahead = reading;
    }

    public int peek() {
        return lookahead;
    }

    public boolean isEmpty() {
        return this.lookahead == NO_CHAR;
    }

    public int consume() {
        int result = this.lookahead;
        this.clear();
        return result;
    }

    public void clear() {
        this.lookahead = NO_CHAR;
    }
}
