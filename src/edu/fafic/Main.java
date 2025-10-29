package edu.fafic;

import edu.fafic.core.Lexer;
import edu.fafic.token.Token;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        File file = Path.of(".code").toFile();

        if (!file.exists()) {
            System.err.println("Arquivo .code não encontrado na raiz do projeto");
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Lexer lexer = new Lexer(reader);
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
