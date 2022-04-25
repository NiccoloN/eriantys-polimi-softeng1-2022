package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

public class ViewOnly extends CLIState {

    public ViewOnly(EriantysCLI cli) {

        super(cli, new CLIComponent(1, new String[] { " " }));
    }

    @Override
    public void exit() {}

    @Override
    public void manageInput(char[] input) {}
}
