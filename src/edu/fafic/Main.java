package edu.fafic;

import edu.fafic.lexer.Lexer;
import edu.fafic.vocabulary.Token;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        try (InputStream file = new FileInputStream(".code")) {
            Lexer lexer = new Lexer(file);
            Token token;

            do {
                token = lexer.readToken();
            } while (!token.isEOF());

            lexer.getTokens().forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
