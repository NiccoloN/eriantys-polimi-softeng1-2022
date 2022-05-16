package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.CloudCLIComponent;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene state in which the user is asked to select a cloud
 * @author Niccolò Nicolosi
 */
public class CloudSelection extends GameSceneState {

    private int currentSelectedIndex;
    private CloudCLIComponent currentSelected;

    /**
     * Constructs a  cloud selection state
     * @param cli the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public CloudSelection(EriantysCLI cli, GameScene scene) {

        super(cli, scene);
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        getScene().getHintTextArea().setText("Select a cloud:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↓ or s to select a character card");
        updateCLI();
    }

    @Override
    public void exit() {

        super.exit();
        currentSelected.setColor(CloudCLIComponent.DEFAULT_COLOR);
        getScene().getHintTextArea().setText("");
        getScene().setPrompt(null);
    }

    @Override
    public void manageInput(Input input) {

        if(input.triggersAction(Action.DOWN)) {

            getScene().setState(new CharacterSelection(getCli(), getScene(), this, Action.UP));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfClouds() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfClouds() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(CloudCLIComponent.DEFAULT_COLOR);
        currentSelected = getScene().getCloud(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 1);
    }
}
