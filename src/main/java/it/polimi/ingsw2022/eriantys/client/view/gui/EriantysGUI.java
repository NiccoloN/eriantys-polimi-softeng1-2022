package it.polimi.ingsw2022.eriantys.client.view.gui;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class EriantysGUI extends Application implements View {

    @Override
    public void start(boolean showLog) throws TimeoutException {

        Application.launch(EriantysGUI.class);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Eriantys");

        Parent root = FXMLLoader.load(getClass().getResource("/FXML/WelcomeScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void askUsername(Message requestMessage) {

        //TODO
    }

    @Override
    public void askGameSettings(Message requestMessage) {

        //TODO
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
}
