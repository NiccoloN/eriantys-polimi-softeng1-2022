package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.HelperCardCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.Move.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene state in which the user is asked to select a helper card
 * @author Niccolò Nicolosi
 */
public class HelperSelection extends GameSceneState {

    private int currentSelectedIndex;
    private HelperCardCLIComponent currentSelected;

    private final Message requestMessage;

    /**
     * Constructs a helper selection state
     * @param cli the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public HelperSelection(EriantysCLI cli, GameScene scene, Message requestMessage) {

        super(cli, scene);
        this.requestMessage = requestMessage;
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        getScene().getHintTextArea().setText("Select a helper card:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↑ or w to select a character card");
        updateCLI();
    }

    @Override
    public void exit() {

        super.exit();
        currentSelected.setColor(RESET);
        getScene().getHintTextArea().setText("");
        getScene().setPrompt(null);
    }


    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(Action.UP)) {

            getScene().setState(new CharacterSelection(getCli(), getScene(), this, Action.DOWN));
            return;
        }

        if(input.triggersAction(Action.SELECT)) {

            EriantysClient client = EriantysClient.getInstance();
            client.sendToServer(new PerformedMoveMessage(requestMessage, new ChooseHelperCard
                    (MoveType.CHOOSE_HELPER_CARD, currentSelected.getIndex()), client.getUsername()));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfHelpers() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfHelpers() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = getScene().getHelper(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 1);
    }
}
