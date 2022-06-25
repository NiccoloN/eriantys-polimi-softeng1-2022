package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

import java.io.Serializable;

public abstract class MoveRequest implements Serializable {

    public final String promptSentence;

    public MoveRequest(String promptSentence) {

        this.promptSentence = promptSentence;
    }

    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        EriantysClient.getInstance().log(promptSentence);
    }

    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        controller.setHintsText(promptSentence);
    }
}
