package edu.fafic;

import edu.fafic.core.Lexer;
import edu.fafic.token.Token;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main {
    public static void main(String[] args) {

        try (Reader file = new FileReader(".code")) {
            Lexer lexer = new Lexer(file);
            Token token;

            do {
                token = lexer.nextToken();
                System.out.println(token);
            } while (!token.isEOF());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
