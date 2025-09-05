package edu.fafic.vocabulary;

public enum Category {
    // Palavras-chave
    CHAR,
    INT,
    VOID,
    TRUE,
    FALSE,
    IF,
    ELSE,
    WHILE,

    // Identificadores e literais
    ID,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    STRING_LITERAL,
    CHARACTER_LITERAL,

    // Operador de atribuição
    AS, // = (atribuição)

    // Fim de arquivo
    EOF,

    // Operadores relacionais
    LT,
    LE,
    GT,
    GE,
    EQ,
    NE,

    // Operadores aritméticos
    ADD, // +
    SUB, // -
    DIV, // /
    MUL, // *

    // Operadores lógicos
    AND, // &&
    OR,  // ||
    NOT, // !
    TER, // ?

    // Pontuação
    OP, // (
    CP, // )
    OB, // [
    CB, // ]
    OK, // {
    CK, // }
    CL, // :
    SC, // ;
    CO, // ,
    DOT, // .
}
