package edu.fafic.token;

public enum Type {

    IDENTIFIER,
    LITERAL_INTEGER,
    LITERAL_FLOAT,
    LITERAL_STRING,
    LITERAL_CHAR,
    LITERAL_TRUE,
    LITERAL_FALSE,
    LITERAL_NULL,

    KEYWORD_ABSTRACT,
    KEYWORD_BOOLEAN,
    KEYWORD_BREAK,
    KEYWORD_CASE,
    KEYWORD_CATCH,
    KEYWORD_CHAR,
    KEYWORD_CLASS,
    KEYWORD_CONTINUE,
    KEYWORD_DEFAULT,
    KEYWORD_DO,
    KEYWORD_DOUBLE,
    KEYWORD_ELSE,
    KEYWORD_ENUM,
    KEYWORD_EXTENDS,
    KEYWORD_FINAL,
    KEYWORD_FINALLY,
    KEYWORD_FLOAT,
    KEYWORD_FOR,
    KEYWORD_IF,
    KEYWORD_IMPLEMENTS,
    KEYWORD_IMPORT,
    KEYWORD_INT,
    KEYWORD_INTERFACE,
    KEYWORD_LONG,
    KEYWORD_NEW,
    KEYWORD_PACKAGE,
    KEYWORD_PRIVATE,
    KEYWORD_PROTECTED,
    KEYWORD_PUBLIC,
    KEYWORD_RETURN,
    KEYWORD_SHORT,
    KEYWORD_STATIC,
    KEYWORD_SUPER,
    KEYWORD_SWITCH,
    KEYWORD_THIS,
    KEYWORD_THROW,
    KEYWORD_THROWS,
    KEYWORD_TRY,
    KEYWORD_VOID,
    KEYWORD_WHILE,

    ASSIGN,                 // =

    RELATIONAL_EQUALS,      // ==
    RELATIONAL_NOT_EQUALS,  // !=
    RELATIONAL_LESS,        // <
    RELATIONAL_LESS_EQUAL,  // <=
    RELATIONAL_GREATER,     // >
    RELATIONAL_GREATER_EQUAL, // >=

    LOGICAL_AND,            // &&
    LOGICAL_OR,             // ||
    LOGICAL_NOT,            // !

    TERNARY,                // ?

    BITWISE_AND,            // &
    BITWISE_OR,             // |
    BITWISE_XOR,            // ^
    BITWISE_NOT,            // ~
    BITWISE_AND_EQUALS,     // &=
    BITWISE_OR_EQUALS,      // |=
    BITWISE_XOR_EQUALS,     // ^=
    BITWISE_NOT_EQUALS,     // ~=

    ADD,                    // +
    SUB,                    // -
    MUL,                    // *
    DIV,                    // /
    MOD,                    // %

    INCREMENT,              // ++
    DECREMENT,              // --

    ADD_EQUALS,             // +=
    SUB_EQUALS,             // -=
    MUL_EQUALS,             // *=
    DIV_EQUALS,             // /=
    MOD_EQUALS,             // %=

    ARROW,                  // ->

    DOT,                    // .
    COMMA,                  // ,
    COLON,                  // :
    SEMICOLON,              // ;
    AT,                     // @
    LPAREN,                 // (
    RPAREN,                 // )
    LBRACE,                 // {
    RBRACE,                 // }
    LBRACKET,               // [
    RBRACKET,               // ]

    EOF,                    // End of file
    INVALID                 // Default
}

