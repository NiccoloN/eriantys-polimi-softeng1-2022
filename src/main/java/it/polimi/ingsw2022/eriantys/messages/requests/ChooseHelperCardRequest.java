package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.HelperSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

import java.util.List;

public class ChooseHelperCardRequest extends MoveRequest {

    private final List<Integer> unplayableIndices;

    public ChooseHelperCardRequest(List<Integer> unplayableIndices) {

        super("Choose an helper card");
        this.unplayableIndices = unplayableIndices;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setState(new HelperSelection(cli, scene, requestMessage, unplayableIndices));
    }

    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        super.manage(controller, requestMessage);
        controller.getHelpersGUIComponent().listenToInput(requestMessage);
    }
}
