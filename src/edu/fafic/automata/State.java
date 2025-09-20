package edu.fafic.automata;

import edu.fafic.core.LexingContext;

public interface State {

    State accept(LexingContext ctx, int ch);

}
