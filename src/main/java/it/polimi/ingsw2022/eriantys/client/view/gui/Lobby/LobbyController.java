package it.polimi.ingsw2022.eriantys.client.view.gui.Lobby;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.io.IOException;

public class LobbyController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField ipAddressField;

    public void startGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/ServerSelection.fxml"));
        stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void joinServer(ActionEvent event) {
        String ipAddress = ipAddressField.getText().trim();
        EriantysClient.getInstance().connectToServer(ipAddress);
    }
}

