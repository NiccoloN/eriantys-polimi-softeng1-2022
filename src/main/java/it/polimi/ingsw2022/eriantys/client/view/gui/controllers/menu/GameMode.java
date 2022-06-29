package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

import java.io.IOException;

public class GameMode extends SceneController {
    
    private final Message requestMessage;
    @FXML
    private RadioButton pButton2, pButton3, pButton4, basicButton, expertButton;
    
    public GameMode(EriantysGUI gui, Message requestMessage) {
        
        super(gui);
        this.requestMessage = requestMessage;
    }
    
    public void sendGameSettings() throws IOException {
        
        int numberOfPlayers;
        
        if(pButton2.isSelected()) numberOfPlayers = 2;
        else if(pButton3.isSelected()) numberOfPlayers = 3;
        else if(pButton4.isSelected()) numberOfPlayers = 4;
        else numberOfPlayers = 0;
        
        it.polimi.ingsw2022.eriantys.server.controller.GameMode gameMode;
        
        if(basicButton.isSelected()) gameMode = it.polimi.ingsw2022.eriantys.server.controller.GameMode.BASIC;
        else if(expertButton.isSelected()) gameMode = it.polimi.ingsw2022.eriantys.server.controller.GameMode.EXPERT;
        else gameMode = null;
        
        EriantysClient.getInstance().sendToServer(new GameSettingsMessage(requestMessage, new GameSettings(numberOfPlayers, gameMode)));
    }
}
