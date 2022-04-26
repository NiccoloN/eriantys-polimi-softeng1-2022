package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.Action;
import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.Input;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.IslandCLIComponent;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

/**
 * This class represents a cli state in which the user is asked to select an island
 * @author Niccolò Nicolosi
 */
public class IslandSelection extends CLIState {

    private int currentSelectedIndex;
    private IslandCLIComponent currentSelected;

    public IslandSelection(EriantysCLI cli) {

        super(cli, new CLIComponent(2, new String[] { GREEN + BLINKING + "VV" + RESET }));
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        cli.getHintTextArea().setText("Select an island:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↓ or s to select a character card");
        updateCLI();
    }

    @Override
    public void exit() {

        currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        cli.getHintTextArea().setText("");
        cli.setPrompt(null);
    }

    @Override
    public void manageInput(Input input) {

        if(input.triggersAction(Action.DOWN)) {

            cli.setState(new CharacterSelection(cli, this, Action.UP));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = cli.getNumberOfIslands() - 1;
        else if (currentSelectedIndex > cli.getNumberOfIslands() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        currentSelected = cli.getIsland(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2 - 1, currentSelected.getFrameY() - 1);
    }
}
