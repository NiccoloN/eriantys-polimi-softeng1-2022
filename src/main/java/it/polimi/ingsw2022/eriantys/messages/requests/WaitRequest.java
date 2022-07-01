package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import javafx.application.Platform;

/**
 * This class represents the request of waiting for the turn of other players to take place.
 */
public class WaitRequest extends MoveRequest {
    
    public WaitRequest() {
        
        super("Waiting for other players...");
    }
    
    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {
        
        super.manage(cli, scene, requestMessage);
    }
    
    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {
        
        super.manage(controller, requestMessage);
        Platform.runLater(controller::stopListeningToInputs);
    }
}
