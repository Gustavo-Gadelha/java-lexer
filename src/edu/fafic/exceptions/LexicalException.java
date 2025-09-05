package edu.fafic.exceptions;

public final class LexicalException extends RuntimeException {

    private LexicalException(String message) {
        super(message);
    }

    public static LexicalException unexpected(int codePoint, CharSequence buffer) {
        String message = String.format(
                "Lexical error: unexpected %s%n" +
                "current lexeme: [%s]",
                asString(codePoint), buffer.toString()
        );

        return new LexicalException(message);
    }

    private static String asString(int cp) {
        if (cp < 0) {
            return "EOF";
        }

        String s = String.valueOf(Character.toChars(cp));
        s = s.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        return s;
    }
}
