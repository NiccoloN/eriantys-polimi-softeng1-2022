package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiCodes.ESCAPE_CHAR;

/**
 * This class represents a state of the cli
 * @see EriantysCLI
 * @author Niccolò Nicolosi
 */
public abstract class CLIState {

    protected EriantysCLI cli;
    protected final CLIComponent prompt;

    /**
     * Constructs this state, setting its prompt component
     * @param cli the cli this state is linked to
     * @param prompt the prompt component of this state
     */
    public CLIState(EriantysCLI cli, CLIComponent prompt) {

        this.cli = cli;
        this.prompt = prompt;
    }

    /**
     * Makes the linked cli enter this state
     */
    public void enter() {

        cli.setPrompt(prompt);
    }

    /**
     * Makes the linked cli exit this state
     */
    public abstract void exit();

    /**
     * Makes the cli react to the given input (if the given input is client-side) and notifies the controller of the resulting events if needed
     * @param input the received input
     */
    public abstract void manageInput(char[] input);

    /**
     * @param input the input to check
     * @return whether the given input corresponds to the pressing of an arrow key
     */
    private boolean isArrowChar(char[] input) {

        if (input.length < 2) return false;
        return input[0] == ESCAPE_CHAR && (input[1] == '[' || input[1] == 'O');
    }

    /**
     * @param input the input to check
     * @return whether the given input corresponds to the pressing of an arrow up key
     */
    protected boolean isArrowUpChar(char[] input) {

        return isArrowChar(input) && input[2] == 'A';
    }


    /**
     * @param input the input to check
     * @return whether the given input corresponds to the pressing of an arrow down key
     */
    protected boolean isArrowDownChar(char[] input) {

        return isArrowChar(input) && input[2] == 'B';
    }


    /**
     * @param input the input to check
     * @return whether the given input corresponds to the pressing of an arrow right key
     */
    protected boolean isArrowRightChar(char[] input) {

        return isArrowChar(input) && input[2] == 'C';
    }


    /**
     * @param input the input to check
     * @return whether the given input corresponds to the pressing of an arrow left key
     */
    protected boolean isArrowLeftChar(char[] input) {

        return isArrowChar(input) && input[2] == 'D';
    }
}
