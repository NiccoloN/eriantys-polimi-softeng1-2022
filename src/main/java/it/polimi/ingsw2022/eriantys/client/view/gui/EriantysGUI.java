package it.polimi.ingsw2022.eriantys.client.view.gui;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.gui.menuControllers.Start;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class EriantysGUI extends Application implements View {

    private Stage mainStage;
    private Message previousMessage;
    private Scene currentScene;

    public void setScene(String FXMLFileName, SceneController controller) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/" + FXMLFileName));
        loader.setController(controller);
        Parent root = loader.load();

        currentScene = new Scene(root);

        Platform.runLater( () -> {
                mainStage.setScene(currentScene);
                mainStage.show();
        });
    }

    public Message getPreviousMessage() {
        return previousMessage;
    }

    @Override
    public void start(boolean showLog) throws TimeoutException {

        Application.launch(EriantysGUI.class);
    }


    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        stage.setTitle("Eriantys");
        Image icon = new Image("Images/application_logo.png");
        stage.getIcons().add(icon);

        setScene("WelcomeScreen.fxml", new Start(this));
    }

    @Override
    public void askUsername(Message requestMessage) throws IOException {
        previousMessage = requestMessage;
        //setScene("UsernameSelection.fxml");
    }

    @Override
    public void askGameSettings(Message requestMessage) throws IOException {
        //setScene(("GamemodeSelection.fxml"));
    }

    @Override
    public void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings) {

        //TODO
    }

    @Override
    public void startGame(List<Player> players, Mode gameMode) {

        //TODO
    }

    @Override
    public void applyUpdate(Update update) {

        //TODO
    }

    @Override
    public void requestMove(MoveRequestMessage requestMessage) {

        //TODO
    }

    @Override
    public void endGame(Team team) {

    }

    public Stage getMainStage() {
        return mainStage;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}
