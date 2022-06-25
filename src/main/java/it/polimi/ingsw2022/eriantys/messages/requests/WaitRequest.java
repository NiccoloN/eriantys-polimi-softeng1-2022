package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GUIGamePhase;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.CharacterGUIComponent;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;

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
        if (EriantysClient.getInstance().getGameSettings().gameMode == Mode.EXPERT) {
            for (CharacterGUIComponent character : controller.getCharacterGUIComponents())
                character.stopListeningToInput();
        }
    }
}
