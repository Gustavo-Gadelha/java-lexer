package edu.fafic.token;

public enum Type {
    // Palavras reservadas
    KEYWORD,

    // Identificadores e literais
    ID,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    STRING_LITERAL,
    CHARACTER_LITERAL,
    BOOLEAN_LITERAL,   // true, false

    // Operadores
    AS,                // assignment
    RELATIONAL,        // <, >, <=, >=, ==, !=
    ARITHMETIC,        // +, -, *, /
    LOGICAL,           // &&, ||, !

    // Punctuation
    PUNCTUATION,       // (), {}, [], ;, etc.

    // Tokens especiais
    EOF,               // end of file/input
    COMMENT,           // // or /* ... */

    // Error handling
    INVALID
}

