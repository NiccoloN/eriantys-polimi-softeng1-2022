package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.IslandSelection;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

public class MoveMotherNatureRequest extends MoveRequest {

    private final int motherNatureMaxSteps;

    public MoveMotherNatureRequest(int maxMotherNatureMaxSteps) {

        super("Choose how many steps mother nature will take");
        this.motherNatureMaxSteps = maxMotherNatureMaxSteps;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setState(new IslandSelection(cli, scene, requestMessage, motherNatureMaxSteps));
    }
}
