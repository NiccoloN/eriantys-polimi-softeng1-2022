package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.CloudCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.CloudGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.server.model.board.CloudTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.application.Platform;

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

        CloudCLIComponent cliCloud = scene.getCloud(cloudIndex);
        for(PawnColor color : PawnColor.values()) cliCloud.setStudents(color, cloud.countStudents(color));
    }

    @Override
    public void apply(GameController controller) {

        Platform.runLater(() -> {

            CloudGUIComponent guiCloud = controller.getCloudGUIComponentOfIndex(cloudIndex);

            if(cloud.isEmpty()) guiCloud.clearStudents();
            else for(PawnColor color : PawnColor.values()) guiCloud.setStudents(color, cloud.countStudents(color));
        });
    }
}
