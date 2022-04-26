package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.Action;
import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.Input;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.HelperCardCLIComponent;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

/**
 * This class represents a cli state in which the user is asked to select a helper card
 * @author Niccolò Nicolosi
 */
public class HelperSelection extends CLIState {

    private int currentSelectedIndex;
    private HelperCardCLIComponent currentSelected;

    public HelperSelection(EriantysCLI cli) {

        super(cli, new CLIComponent(1, new String[] { GREEN + BLINKING + "V" + RESET }));
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        cli.getHintTextArea().setText("Select a helper card:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↑ or w to select a character card");
        updateCLI();
    }

    @Override
    public void exit() {

        currentSelected.setColor(RESET);
        cli.getHintTextArea().setText("");
        cli.setPrompt(null);
    }

    @Override
    public void manageInput(Input input) {

        if(input.triggersAction(Action.UP)) {

            cli.setState(new CharacterSelection(cli, this, Action.DOWN));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = cli.getNumberOfHelpers() - 1;
        else if (currentSelectedIndex > cli.getNumberOfHelpers() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = cli.getHelper(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2, currentSelected.getFrameY() - 1);
    }
}
