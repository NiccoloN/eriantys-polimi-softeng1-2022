package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;

import java.io.IOException;

/**
 * This class represents the gui controller of the load-or-create-game selection scene
 * @author Emanuele Musto
 */
public class LoadOrCreateGame extends SceneController {
    
    private final Message requestMessage;
    
    public LoadOrCreateGame(EriantysGUI gui, Message requestMessage) {
        
        super(gui);
        this.requestMessage = requestMessage;
    }
    
    /**
     * Sets the next gui scene to create a new game
     * @throws IOException if an IOException occurs while setting the new scene
     */
    public void newGame() throws IOException {
        
        getGui().setScene("GameModeSelection.fxml", new GameMode(getGui(), requestMessage));
    }
    
    /**
     * Communicates to the server to load an existing game
     * @throws IOException if an IOException occurs sending the message to the server
     */
    public void loadGame() throws IOException {
        
        EriantysClient.getInstance().sendToServer(new GameSettingsMessage(requestMessage, new GameSettings()));
    }
}
