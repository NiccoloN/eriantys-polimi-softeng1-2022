package it.polimi.ingsw2022.eriantys.client.view.gui.Lobby;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.io.IOException;

public class LobbyController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField ipAddressField;
    @FXML
    private TextField usernameField;

    @FXML
    private ToggleGroup players;
    @FXML
    private ComboBox gamemode;


    public void joinServer(ActionEvent event) {
        String ipAddress = ipAddressField.getText().trim();
        EriantysClient.getInstance().connectToServer(ipAddress);
    }

    public void startGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/ServerSelection.fxml"));
        stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setUsername() throws IOException {
        String username = usernameField.getText().trim();
        EriantysClient.getInstance().setUsername(username);
        EriantysClient.getInstance().sendToServer(new UsernameChoiceMessage(EriantysGUI.getPreviousMessage()));
    }

    public void setGameSettings() {
        int numberOfPlayers = Integer.parseInt(((RadioButton) players.getSelectedToggle()).getText());
        String selectedGamemode = gamemode.getAccessibleText();

        System.out.println(numberOfPlayers);
        System.out.println(selectedGamemode);

        // EriantysClient.getInstance().sendToServer(new GameSettingsMessage());

    }
}

