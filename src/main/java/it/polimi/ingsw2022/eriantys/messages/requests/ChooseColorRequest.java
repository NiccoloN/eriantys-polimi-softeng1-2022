package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.ColorSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.application.Platform;

import java.util.List;

public class ChooseColorRequest extends MoveRequest {

    public final ColoredPawnOriginDestination fromWhere;
    private final List<PawnColor> availableColors;
    public final int characterCardIndex;

    public ChooseColorRequest(int characterCardIndex, List<PawnColor> availableColors, ColoredPawnOriginDestination fromWhere, String promptSentence) {

        super(promptSentence);
        this.characterCardIndex = characterCardIndex;
        this.availableColors = availableColors;
        this.fromWhere = fromWhere;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setColors(availableColors);
        scene.setState(new ColorSelection(cli, scene, requestMessage, characterCardIndex));
    }

    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        super.manage(controller, requestMessage);

        Platform.runLater(() -> {

            controller.stopListeners();
            controller.getColorsGUIComponent().listenToInput(requestMessage, availableColors, characterCardIndex);
        });
    }
}
