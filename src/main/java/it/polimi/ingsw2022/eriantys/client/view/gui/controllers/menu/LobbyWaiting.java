package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyWaiting extends SceneController implements Initializable {
    
    private final String[] playerUsernames;
    private final GameSettings gameSettings;
    @FXML
    TextArea info;
    
    public LobbyWaiting(EriantysGUI gui, String[] playerUsernames, GameSettings gameSettings) {
        
        super(gui);
        this.playerUsernames = playerUsernames;
        this.gameSettings = gameSettings;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        int playersRemaining = gameSettings.numberOfPlayers - playerUsernames.length;
        
        info.setWrapText(true);
        
        assert gameSettings.gameMode != null;
        info.setText("Gamemode: " + gameSettings.gameMode.name() + "  Players: " + gameSettings.numberOfPlayers + "\n\n");
        info.appendText("Players in lobby:\n");
        
        for (String playerUsername : playerUsernames) info.appendText(playerUsername + "\n");
        
        info.appendText("\nWaiting for " + playersRemaining + " more players...");
        
        info.setEditable(false);
    }
}
