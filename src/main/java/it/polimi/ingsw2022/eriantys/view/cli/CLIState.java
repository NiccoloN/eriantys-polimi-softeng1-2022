package it.polimi.ingsw2022.eriantys.view.cli;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

abstract class CLIState {

    protected EriantysCLI cli;
    protected final CLIComponent prompt;

    CLIState(EriantysCLI cli, CLIComponent prompt) {

        this.cli = cli;
        this.prompt = prompt;
    }

    void apply() {

        cli.setPrompt(prompt);
    }

    abstract void manageInput(char input);
}
