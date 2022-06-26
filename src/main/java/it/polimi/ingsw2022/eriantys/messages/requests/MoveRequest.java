package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import javafx.application.Platform;

import java.io.Serializable;

public abstract class MoveRequest implements Serializable {

    public final String promptSentence;
    private boolean canPlayCharacter;

    public MoveRequest(String promptSentence) {

        this.promptSentence = promptSentence;
        canPlayCharacter = false;
    }

    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        scene.getHintTextArea().setText(promptSentence);
    }

    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        Platform.runLater(() -> controller.setHintsText(promptSentence));
    }

    public boolean canPlayCharacter() {

        return canPlayCharacter;
    }

    public void setCanPlayCharacter(boolean canPlayCharacter) {

        this.canPlayCharacter = canPlayCharacter;
    }
}
