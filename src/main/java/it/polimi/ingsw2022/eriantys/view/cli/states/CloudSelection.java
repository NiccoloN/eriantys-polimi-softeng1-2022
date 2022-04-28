package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.Action;
import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.Input;
import it.polimi.ingsw2022.eriantys.view.cli.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.CloudCLIComponent;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

/**
 * This class represents a cli state in which the user is asked to select a cloud
 * @author Niccolò Nicolosi
 */
public class CloudSelection extends CLIState {

    private int currentSelectedIndex;
    private CloudCLIComponent currentSelected;

    public CloudSelection(EriantysCLI cli) {

        super(cli, new BasicCLIComponent(1, new String[] {GREEN + BLINKING + "V" + RESET }));
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        cli.getHintTextArea().setText("Select a cloud:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↓ or s to select a character card");
        updateCLI();
    }

    @Override
    public void exit() {

        currentSelected.setColor(CloudCLIComponent.DEFAULT_COLOR);
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

        if (currentSelectedIndex < 0) currentSelectedIndex = cli.getNumberOfClouds() - 1;
        else if (currentSelectedIndex > cli.getNumberOfClouds() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(CloudCLIComponent.DEFAULT_COLOR);
        currentSelected = cli.getCloud(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 1);
    }
}
