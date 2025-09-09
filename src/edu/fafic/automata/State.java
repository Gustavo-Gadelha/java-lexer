package edu.fafic.automata;

import edu.fafic.lexer.Lexer;

public interface State {

    State accept(Lexer lexer, int input);

}
