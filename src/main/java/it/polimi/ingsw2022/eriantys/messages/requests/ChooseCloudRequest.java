package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.CloudSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.CharacterGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.CloudGUIComponent;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import javafx.application.Platform;

/**
 * This class represents the request of choosing one cloud amongst the available in the game.
 */
public class ChooseCloudRequest extends MoveRequest {

    public ChooseCloudRequest(boolean canPlayCharacter) {

        super("Choose a cloud from which to take new students");
        setCanPlayCharacter(canPlayCharacter);
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setState(new CloudSelection(cli, scene, requestMessage));
    }

    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        super.manage(controller, requestMessage);

        Platform.runLater(() -> {

            for(CloudGUIComponent cloud : controller.getCloudGUIComponents())
                if(!cloud.isEmpty()) cloud.listenToInput(requestMessage);

            if (EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT && canPlayCharacter()) {

                for (CharacterGUIComponent character : controller.getCharacterGUIComponents()) {

                    character.stopListeningToInput();
                    character.listenToInput(requestMessage);
                }
            }
        });
    }
}
