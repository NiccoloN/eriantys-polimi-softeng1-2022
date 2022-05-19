package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.ColorCLIComponent;
import it.polimi.ingsw2022.eriantys.messages.Message;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.GREEN;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * This class represents a game scene state in which the user is asked to select a color
 * @author Niccolò Nicolosi
 */
public class ColorSelection extends GameSceneState {

    private int currentSelectedIndex;
    private ColorCLIComponent currentSelected;

    /**
     * Constructs a color selection state
     * @param cli   the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public ColorSelection(EriantysCLI cli, GameScene scene, Message requestMessage) {

        super(cli, scene, requestMessage);
        scene.setColors(scene.getPlayer().getEntranceColors());
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        getScene().getHintTextArea().setText("Select a color:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                             "Press ↑ or w to select a character card");
        for(int n = 0; n < getScene().getNumberOfHelpers(); n++) getScene().getHelper(n).setHidden(true);
        updateCLI();
    }

    @Override
    public void exit() {

        super.exit();
        currentSelected.setColor(RESET);
        getScene().getHintTextArea().setText("");
        for(int n = 0; n < getScene().getNumberOfHelpers(); n++) getScene().getHelper(n).setHidden(false);
        for(int n = 0; n < getScene().getNumberOfColors(); n++) getScene().getColor(n).setHidden(true);
    }

    @Override
    public void manageInput(Input input) {

        if(input.triggersAction(Action.UP)) {

            getScene().setState(new CharacterSelection(getCli(), getScene(), requestMessage, this, Action.DOWN));
            return;
        }

        if(input.triggersAction(Action.SELECT)){

            getScene().setState(new IslandSelection(getCli(), getScene(), requestMessage, currentSelected.pawnColor));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfColors() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfColors() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = getScene().getColor(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 2);
    }
}
