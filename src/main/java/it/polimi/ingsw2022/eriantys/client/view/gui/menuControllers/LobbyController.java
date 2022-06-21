package it.polimi.ingsw2022.eriantys.client.view.gui.menuControllers;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;


import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

import static it.polimi.ingsw2022.eriantys.client.EriantysClient.ADDRESS_FILE_NAME;

public class LobbyController implements Initializable{

    private Stage stage;
    private Scene scene;
    private String serverIpAddress;
    @FXML
    private TextField ipAddressField;
    @FXML
    private TextField usernameField;

    @FXML
    private ToggleGroup players;
    @FXML
    private ComboBox gamemode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getSavedServerIpAddress();
        ipAddressField.setText(serverIpAddress);
    }

    public void saveServerIp(ActionEvent event) throws IOException {

        serverIpAddress = ipAddressField.getText().trim();

        File file = new File(ADDRESS_FILE_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(serverIpAddress);
        outputStreamWriter.close();

        setWelcomeScene();

    }

    public void startGame(ActionEvent event) throws IOException {

        EriantysClient.getInstance().connectToServer(serverIpAddress);
    }

    public void setUsername() throws IOException {
        String username = usernameField.getText().trim();
        EriantysClient.getInstance().setUsername(username);
        //EriantysClient.getInstance().sendToServer(new UsernameChoiceMessage(EriantysGUI.getPreviousMessage()));
    }

    public void setGameSettings() {
        int numberOfPlayers = Integer.parseInt(((RadioButton) players.getSelectedToggle()).getText());
        String selectedGamemode = gamemode.getAccessibleText();

        System.out.println(numberOfPlayers);
        System.out.println(selectedGamemode);

        // EriantysClient.getInstance().sendToServer(new GameSettingsMessage());

    }

    public void setWelcomeScene() throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/WelcomeScreen.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setServerIpScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/ServerSelection.fxml")));
        stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void getSavedServerIpAddress() {

        try {
            FileInputStream fileInputStream = new FileInputStream(ADDRESS_FILE_NAME);
            Scanner scanner = new Scanner(fileInputStream);
            serverIpAddress = scanner.next();
            scanner.close();
        } catch (FileNotFoundException e) {
            serverIpAddress = "localhost";
        }
    }
}

