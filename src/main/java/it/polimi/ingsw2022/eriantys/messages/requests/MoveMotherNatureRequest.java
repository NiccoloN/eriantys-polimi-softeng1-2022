package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.IslandSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.CharacterGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.IslandGUIComponent;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import javafx.application.Platform;

public class MoveMotherNatureRequest extends MoveRequest {

    public final int motherNatureMaxSteps;
    private static boolean additionalSteps = false;

    public MoveMotherNatureRequest(int maxMotherNatureMaxSteps) {

        super("Choose how many steps mother nature will take");
        if(!additionalSteps) this.motherNatureMaxSteps = maxMotherNatureMaxSteps;
        else this.motherNatureMaxSteps = maxMotherNatureMaxSteps + 2;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setState(new IslandSelection(cli, scene, requestMessage, motherNatureMaxSteps));
    }

    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        super.manage(controller, requestMessage);

        Platform.runLater(() -> {

            for (IslandGUIComponent island : controller.getIslandGUIComponents()) island.listenToInput(requestMessage);

            if (EriantysClient.getInstance().getGameSettings().gameMode == Mode.EXPERT) {

                for (CharacterGUIComponent character : controller.getCharacterGUIComponents()) {

                    character.stopListeningToInput();
                    character.listenToInput(requestMessage);
                }
            }
        });
    }

    public static void setAdditionalSteps(boolean additionalSteps) {
        MoveMotherNatureRequest.additionalSteps = additionalSteps;
    }
}
