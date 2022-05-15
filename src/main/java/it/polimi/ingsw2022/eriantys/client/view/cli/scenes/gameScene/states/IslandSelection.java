package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveMotherNature;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene state in which the user is asked to select an island
 * @author Niccolò Nicolosi
 */
public class IslandSelection extends GameSceneState {

    private int currentSelectedIndex;
    private IslandCLIComponent currentSelected;

    private final Message requestMessage;

    /**
     * Constructs an island selection state
     * @param cli the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public IslandSelection(EriantysCLI cli, GameScene scene, Message requestMessage) {

        super(cli, scene);
        this.requestMessage = requestMessage;
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        getScene().getHintTextArea().setText("Select an island:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↓ or s to select a character card");
        updateCLI();
    }

    @Override
    public void exit() {

        currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        getScene().getHintTextArea().setText("");
        getScene().setPrompt(null);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(Action.DOWN)) {

            getScene().setState(new CharacterSelection(getCli(), getScene(), this, Action.UP));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if(input.triggersAction(Action.SELECT)){

            EriantysClient client = EriantysClient.getInstance();
            client.sendToServer(new PerformedMoveMessage(requestMessage, new MoveMotherNature
                    (MoveType.MOVE_MOTHER_NATURE, currentSelected.getIndex()), client.getUsername()));
            return;
        }

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfIslands() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfIslands() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        currentSelected = getScene().getIsland(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f - 1, currentSelected.getFrameY() - 1);
    }
}
