package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GUIGamePhase;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

public class WaitRequest extends MoveRequest {

    public WaitRequest() {
        super("Waiting for other players...");
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
    }

    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        super.manage(controller, requestMessage);
        controller.setGamePhase(GUIGamePhase.STALL, requestMessage);
    }
}