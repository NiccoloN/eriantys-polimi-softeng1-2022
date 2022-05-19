package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.CloudCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.board.CloudTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;

public class CloudChange implements Change, Serializable {

    private final Integer cloudIndex;
    private final CloudTile cloud;

    public CloudChange(Integer cloudIndex, CloudTile cloud) {

        this.cloudIndex = cloudIndex;
        this.cloud = cloud;
    }

    @Override
    public void apply(GameScene scene) {

        CloudCLIComponent cliCloud = scene.getCloud(cloudIndex - 1);
        for(PawnColor color : PawnColor.values()) cliCloud.setStudents(color, cloud.countStudents(color));
    }
}
