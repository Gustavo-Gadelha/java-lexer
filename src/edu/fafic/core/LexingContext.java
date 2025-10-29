package edu.fafic.core;

import edu.fafic.token.Type;

public interface LexingContext {

    int read();

    void unread(int ch);

    int peek();

    void append(int ch);

    int first();

    int last();

    void clear();

    String lexeme();

    void emit(Type type);

    void error(String message);
}
