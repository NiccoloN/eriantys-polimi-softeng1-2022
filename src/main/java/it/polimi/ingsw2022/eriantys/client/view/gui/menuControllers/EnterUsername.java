package it.polimi.ingsw2022.eriantys.client.view.gui.menuControllers;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EnterUsername extends SceneController {

    private final Message requestMessage;
    @FXML
    private TextField usernameField;

    public EnterUsername(EriantysGUI gui, Message requestMessage) {

        super(gui);
        this.requestMessage = requestMessage;
    }

    public void sendUsername() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.setUsername(usernameField.getText());
        EriantysClient.getInstance().sendToServer(new UsernameChoiceMessage(requestMessage));
    }

}
