package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.IslandSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.IslandGUIComponent;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

public class ChooseIslandRequest extends MoveRequest {

    public final int characterCardIndex;

    public ChooseIslandRequest(int characterCardIndex, String promptSentence) {

        super(promptSentence);
        this.characterCardIndex = characterCardIndex;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setState(new IslandSelection(cli, scene, requestMessage, null, characterCardIndex));
    }

    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        super.manage(controller, requestMessage);
        for(IslandGUIComponent island : controller.getIslandGUIComponents()) island.listenToInput(requestMessage, characterCardIndex);
    }
}
