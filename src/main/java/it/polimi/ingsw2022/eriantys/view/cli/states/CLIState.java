package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.Input;
import it.polimi.ingsw2022.eriantys.view.cli.components.BasicCLIComponent;

/**
 * This class represents a state of the cli
 * @see EriantysCLI
 * @author Niccolò Nicolosi
 */
public abstract class CLIState {

    protected EriantysCLI cli;
    protected final BasicCLIComponent prompt;

    /**
     * Constructs a cli state, setting its prompt component
     * @param cli the cli this state is linked to
     * @param prompt the prompt component of this state
     */
    protected CLIState(EriantysCLI cli, BasicCLIComponent prompt) {

        this.cli = cli;
        this.prompt = prompt;
    }

    /**
     * Makes the cli enter this state
     */
    public void enter() {

        cli.setPrompt(prompt);
    }

    /**
     * Makes the cli exit this state
     */
    public abstract void exit();

    /**
     * Makes the cli react to the given input (if the given input is client-side) and notifies the controller of the resulting events if needed
     * @param input the received input
     */
    public abstract void manageInput(Input input);
}
