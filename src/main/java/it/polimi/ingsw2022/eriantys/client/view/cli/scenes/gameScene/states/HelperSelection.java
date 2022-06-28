package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.HelperCardCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene state in which the user is asked to select a helper card
 * @author Niccolò Nicolosi
 */
public class HelperSelection extends GameSceneState {

    private int currentSelectedIndex;
    private HelperCardCLIComponent currentSelected;
    private final List<Integer> unplayableIndices;

    /**
     * Constructs a helper selection state
     * @param cli the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public HelperSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage, List<Integer> unplayableIndices) {

        super(cli, scene, requestMessage);
        this.unplayableIndices = unplayableIndices;
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        for(int n = 0; n < getScene().getNumberOfHelpers(); n++) {

            HelperCardCLIComponent helper = getScene().getHelper(n);
            if(unplayableIndices.contains(helper.getIndex())) {

                helper.setPlayable(false);
                if(n == currentSelectedIndex) currentSelectedIndex++;
            }
        }

        updateCLI();
    }

    @Override
    public void exit() {

        super.exit();
        for(int n = 0; n < getScene().getNumberOfHelpers(); n++) getScene().getHelper(n).setPlayable(true);
    }


    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(Action.UP) && getScene().gameMode == GameMode.EXPERT) {

            getScene().setState(new CharacterSelection(getCli(), getScene(), requestMessage, this, Action.DOWN));
            return;
        }

        if(input.triggersAction(Action.SELECT)) {

            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage,
                    new ChooseHelperCard(currentSelected.getIndex())));

            getScene().setState(new ViewOnly(getCli(), getScene()));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) {

            do {

                currentSelectedIndex++;
                if (currentSelectedIndex > getScene().getNumberOfHelpers() - 1) currentSelectedIndex = 0;
            }
            while(!getScene().getHelper(currentSelectedIndex).isPlayable());
        }
        else if (input.triggersAction(Action.LEFT)) {

            do {

                currentSelectedIndex--;
                if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfHelpers() - 1;
            }
            while(!getScene().getHelper(currentSelectedIndex).isPlayable());
        }

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = getScene().getHelper(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 1);
    }
}
