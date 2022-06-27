package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.HelperSelection;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

import java.util.List;

/**
 * This class represents the request of choosing a helper card amongst the ones available.
 * The attribute unplayableIndices indicates the indexes of the helper cards that are still in the hand of the player,
 * but are unplayable due to the fact that some other player has chosen the same card in this round.
 */
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
}
