package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;

import java.io.IOException;

public class LoadOrCreateGame extends SceneController {
    
    private final Message requestMessage;
    
    public LoadOrCreateGame(EriantysGUI gui, Message requestMessage) {
        
        super(gui);
        this.requestMessage = requestMessage;
    }
    
    public void newGame() throws IOException {
        
        getGui().setScene("GameModeSelection.fxml", new GameMode(getGui(), requestMessage));
    }
    
    public void loadGame() throws IOException {
        
        EriantysClient.getInstance().sendToServer(new GameSettingsMessage(requestMessage, new GameSettings()));
    }
}
