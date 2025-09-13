package edu.fafic.lexer;

public class Buffer {

    public final StringBuffer buffer;

    public Buffer() {
        buffer = new StringBuffer();
    }

    public void append(int codePoint) {
        this.buffer.appendCodePoint(codePoint);
    }

    public void append(Character character) {
        this.buffer.append(character);
    }

    public int first() {
        return this.buffer.codePointAt(0);
    }

    public int last() {
        return this.buffer.codePointBefore(buffer.length());
    }

    public String read() {
        return this.buffer.toString();
    }

    public String consume() {
        String result = this.buffer.toString();
        this.clear();
        return result;
    }

    public void clear() {
        this.buffer.setLength(0);
    }

    @Override
    public String toString() {
        return "Buffer(%s)".formatted(this.buffer.toString());
    }
}
