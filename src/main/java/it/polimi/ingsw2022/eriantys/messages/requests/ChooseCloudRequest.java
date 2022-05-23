package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.CloudSelection;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

public class ChooseCloudRequest extends MoveRequest {

    public ChooseCloudRequest() {

        super("Choose a cloud from which to take new students");
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setState(new CloudSelection(cli, scene, requestMessage));
    }
}