package it.polimi.ingsw2022.eriantys.view.cli;

import java.util.Arrays;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.ESCAPE_CHAR;

public enum Action {

    UP(new Input[] { new Input('w'), new Input('W'), new Input(ESCAPE_CHAR, '[', 'A'), new Input(ESCAPE_CHAR, 'O', 'A') }),
    DOWN(new Input[] { new Input('s'), new Input('S'), new Input(ESCAPE_CHAR, '[', 'B'), new Input(ESCAPE_CHAR, 'O', 'B') }),
    RIGHT(new Input[] { new Input('d'), new Input('D'), new Input(ESCAPE_CHAR, '[', 'C'), new Input(ESCAPE_CHAR, 'O', 'C') }),
    LEFT(new Input[] { new Input('a'), new Input('A'), new Input(ESCAPE_CHAR, '[', 'D'), new Input(ESCAPE_CHAR, 'O', 'D') }),
    SELECT(new Input[] { new Input('\n')});

    private final Input[] inputs;

    Action(Input[] inputs) {

        this.inputs = inputs;
    }

    public Input[] getInputs() {

        return Arrays.copyOf(inputs, inputs.length);
    }
}
