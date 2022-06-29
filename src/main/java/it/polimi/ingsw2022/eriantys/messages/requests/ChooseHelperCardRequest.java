package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.HelperSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.CharacterGUIComponent;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import javafx.application.Platform;

import java.util.List;

/**
 * This class represents the request of choosing a helper card amongst the ones available.
 * The attribute unplayableIndices indicates the indexes of the helper cards that are still in the hand of the player,
 * but are unplayable due to the fact that some other player has chosen the same card in this round.
 */
public class ChooseHelperCardRequest extends MoveRequest {
    
    private final List<Integer> unplayableIndices;
    
    public ChooseHelperCardRequest(List<Integer> unplayableIndices, boolean canPlayCharacter) {
        
        super("Choose an helper card");
        this.unplayableIndices = unplayableIndices;
        setCanPlayCharacter(canPlayCharacter);
    }
    
    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {
        
        super.manage(cli, scene, requestMessage);
        scene.setState(new HelperSelection(cli, scene, requestMessage, unplayableIndices));
    }
    
    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {
        
        super.manage(controller, requestMessage);
        
        Platform.runLater(() -> {
            
            controller.getHelpersGUIComponent().listenToInput(requestMessage, unplayableIndices);
            
            if(EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT && canPlayCharacter())
                for(CharacterGUIComponent character : controller.getCharacterGUIComponents()) character.listenToInput(requestMessage);
        });
    }
}
