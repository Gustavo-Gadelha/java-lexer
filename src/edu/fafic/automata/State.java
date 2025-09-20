package edu.fafic.automata;

public interface State {

    State accept(StateContext ctx, int ch);

}
