package it.polimi.ingsw2022.eriantys.view.cli;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.HelperCardCLIComponent;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

class HelperSelection extends CLIState {

    private int currentSelectedIndex;
    private HelperCardCLIComponent currentSelected;

    HelperSelection(EriantysCLI cli) {

        super(cli, new CLIComponent(1, new String[] { GREEN + "V" + RESET }));
        currentSelectedIndex = 0;
    }

    @Override
    void apply() {

        super.apply();
        currentSelected = cli.getHelper(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getX() + currentSelected.getWidth() / 2, currentSelected.getY() - 1);
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
        apply();
    }
}
