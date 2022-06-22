package it.polimi.ingsw2022.eriantys.client.view.gui.menuControllers;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Start extends SceneController {

    public Start(EriantysGUI gui) {
        super(gui);
    }

    public void startGame(ActionEvent event) throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.connectToServer(client.loadSavedServerIp());
    }

    public void setServerIpScene(ActionEvent event) throws IOException {

        getGui().setScene("ServerSelection.fxml", new EnterServerIp(getGui()));
    }
}
