package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.HelperCardCLIComponent;

import java.util.regex.Pattern;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiCodes.*;

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
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2, currentSelected.getFrameY() - 1);
    }

    @Override
    public void exit() {

        currentSelected.setColor(RESET);
    }

    @Override
    public void manageInput(char[] input) {

        currentSelected.setColor(RESET);

        if (input[0] == 'a' || (input[0] == ESCAPE_CHAR && input[1] == '[' && input[2] == 'D')) currentSelectedIndex--;
        else if (input[0] == 'd' || (input[0] == ESCAPE_CHAR && input[1] == '[' && input[2] == 'C')) currentSelectedIndex++;

        if (currentSelectedIndex < 0) currentSelectedIndex = cli.getNumberOfHelpers() - 1;
        if (currentSelectedIndex > cli.getNumberOfHelpers() - 1) currentSelectedIndex = 0;

        enter();
    }
}
