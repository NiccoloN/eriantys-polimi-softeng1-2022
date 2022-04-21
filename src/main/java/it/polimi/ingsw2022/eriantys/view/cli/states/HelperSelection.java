package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.HelperCardCLIComponent;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

public class HelperSelection extends CLIState {

    private int currentSelectedIndex;
    private HelperCardCLIComponent currentSelected;

    public HelperSelection(EriantysCLI cli) {

        super(cli, new CLIComponent(1, new String[] { GREEN + BLINKING + "V" + RESET }));
        currentSelectedIndex = 0;
    }

    @Override
    public void enter() {

        super.enter();
        currentSelected = cli.getHelper(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getX() + currentSelected.getWidth() / 2, currentSelected.getY() - 1);
    }

    @Override
    public void exit() {

        currentSelected.setColor(RESET);
    }

    @Override
    public void manageInput(char input) {

        currentSelected.setColor(RESET);

        switch(input) {

            case 'a':
                currentSelectedIndex--;
                if (currentSelectedIndex < 0) currentSelectedIndex = cli.getNumberOfHelpers() - 1;
                break;
            case 'd':
                currentSelectedIndex++;
                break;
            default:
                break;
        }

        currentSelectedIndex %= cli.getNumberOfHelpers();
        enter();
    }
}
