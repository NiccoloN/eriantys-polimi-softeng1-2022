package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.Input;

/**
 * This class represents a "view only" state of the cli. In this state inputs are ignored
 * @author Niccolò Nicolosi
 */
public class ViewOnly extends CLIState {

    public ViewOnly(EriantysCLI cli) {

        super(cli, null);
    }

    @Override
    public void exit() {}

    @Override
    public void manageInput(Input input) {}
}
