package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

public abstract class CLIState {

    protected EriantysCLI cli;
    protected final CLIComponent prompt;

    public CLIState(EriantysCLI cli, CLIComponent prompt) {

        this.cli = cli;
        this.prompt = prompt;
    }

    public void enter() {

        cli.setPrompt(prompt);
    }

    public abstract void exit();

    public abstract void manageInput(char input);
}
