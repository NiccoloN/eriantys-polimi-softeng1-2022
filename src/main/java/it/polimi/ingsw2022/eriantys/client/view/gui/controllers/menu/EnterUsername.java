package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EnterUsername extends SceneController implements Initializable {
    
    private final Message requestMessage;
    
    @FXML
    private TextField usernameField;
    
    public EnterUsername(EriantysGUI gui, Message requestMessage) {
        
        super(gui);
        this.requestMessage = requestMessage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            
            if(newValue.length() > 20) usernameField.setText(oldValue);
        });
        
        usernameField.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            
            if(keyEvent.getCode() == KeyCode.ENTER) {
                
                try {
                    
                    sendUsername();
                }
                catch(IOException e) {
                    
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void sendUsername() throws IOException {
        
        EriantysClient client = EriantysClient.getInstance();
        client.setUsername(usernameField.getText());
        EriantysClient.getInstance().sendToServer(new UsernameChoiceMessage(requestMessage));
    }
}
