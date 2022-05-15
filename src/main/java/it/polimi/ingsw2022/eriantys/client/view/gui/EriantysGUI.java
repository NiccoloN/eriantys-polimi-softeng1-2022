package it.polimi.ingsw2022.eriantys.client.view.gui;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.TimeoutException;

public class EriantysGUI extends Application implements View {

    @Override
    public void start(boolean showLog) throws TimeoutException {

        Application.launch(EriantysGUI.class);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Eriantys");

        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 800, 600);

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
    public void startGame(String[] playerUsernames, Team[] playerTeams, Mode gameMode) {

        //TODO
    }

    @Override
    public void applyChange(IslandChange change) {

        //TODO
    }

    @Override
    public void applyChange(CloudChange change) {

    }

    @Override
    public void applyChange(SchoolDashboardChange change) {

    }

    @Override
    public void applyChange(StudentsBagChange change) {

    }

    @Override
    public void applyChange(HelperCardsChange change) {

    }

    @Override
    public void askMoveType(MoveRequestMessage requestMessage) {
    }

    @Override
    public void applyChange(GameInitChange change) {

        //TODO
    }
}
