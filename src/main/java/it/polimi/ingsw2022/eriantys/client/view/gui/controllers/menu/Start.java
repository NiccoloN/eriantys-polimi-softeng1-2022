package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import javafx.event.ActionEvent;

import java.io.IOException;

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
