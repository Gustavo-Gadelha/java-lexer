package edu.fafic.token;

public record Token(Type type, String lexeme) {

    public boolean isEOF() {
        return this.type == Type.EOF;
    }

    @Override
    public String toString() {
        return "<%s, %s>".formatted(type, lexeme);
    }
}
