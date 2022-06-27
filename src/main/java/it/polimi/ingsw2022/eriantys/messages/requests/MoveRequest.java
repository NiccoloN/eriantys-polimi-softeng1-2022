package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

import java.io.Serializable;

/**
 * This class represents a request for a move, sent by the server for a client. The request is encapsulated in a
 * MoveRequestMessage. Every move has a sentence that will be displayed as a hint for the player, so that he knows
 * what to do in every circumstance.
 * @see MoveRequestMessage
 */
public abstract class MoveRequest implements Serializable {

    public final String promptSentence;

    public MoveRequest(String promptSentence) {

        this.promptSentence = promptSentence;
    }

    /**
     * This method is called by the CLI client when a move request arrives. It prints as a hint the sentence,
     * and modifies the cli and the scene in order to let the player make the requested move.
     * (Generally it sets the right state of a scene)
     * @param cli the command line interface of the client.
     * @param scene the current game scene.
     * @param requestMessage the request message that contains this request.
     */
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        EriantysClient.getInstance().log(promptSentence);
    }
}
