package it.polimi.ingsw2022.eriantys.view.cli;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

class ViewOnly extends CLIState {

    ViewOnly(EriantysCLI cli) {

        super(cli, new CLIComponent(1, new String[] { " " }));
    }

    @Override
    void manageInput(char input) {

    }
}
