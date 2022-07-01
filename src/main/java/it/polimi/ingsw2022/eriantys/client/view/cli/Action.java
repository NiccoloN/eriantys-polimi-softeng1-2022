package it.polimi.ingsw2022.eriantys.client.view.cli;

import java.util.Arrays;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.ESCAPE_CHAR;

/**
 * This enumeration lists all the possible actions the user can perform to interact with the cli.
 * Every action is triggered by specific inputs
 * @author Niccolò Nicolosi
 * @see Input
 */
public enum Action {
    
    UP(new Input[]{new Input('w'), new Input('W'), new Input(ESCAPE_CHAR, '[', 'A'), new Input(ESCAPE_CHAR, 'O', 'A')}),
    DOWN(new Input[]{new Input('s'), new Input('S'), new Input(ESCAPE_CHAR, '[', 'B'), new Input(ESCAPE_CHAR, 'O', 'B')}),
    RIGHT(new Input[]{new Input('d'), new Input('D'), new Input(ESCAPE_CHAR, '[', 'C'), new Input(ESCAPE_CHAR, 'O', 'C')}),
    LEFT(new Input[]{new Input('a'), new Input('A'), new Input(ESCAPE_CHAR, '[', 'D'), new Input(ESCAPE_CHAR, 'O', 'D')}),
    SELECT(new Input[]{new Input((char) 13)}),
    ESC(new Input[]{new Input((char) 27)});
    
    private final Input[] inputs;
    
    Action(Input[] inputs) {
        
        this.inputs = inputs;
    }
    
    /**
     * @return the inputs that trigger this action
     */
    public Input[] getTriggerInputs() {
        
        return Arrays.copyOf(inputs, inputs.length);
    }
}
