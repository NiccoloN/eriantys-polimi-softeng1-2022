package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Start extends SceneController {

    @FXML
    TextField connectingTextField;

    public Start(EriantysGUI gui) {
        super(gui);
    }

    public void startGame() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        String serverIP = client.loadSavedServerIp();

        connectingTextField.setText("Connecting to " + serverIP + "...");
        connectingTextField.setVisible(true);

        new Thread(() -> {

            if(!client.connectToServer(serverIP)) connectingTextField.setVisible(false);
        }).start();
    }

    public void setServerIpScene() throws IOException {

        getGui().setScene("ServerSelection.fxml", new EnterServerIp(getGui()));
    }
}
