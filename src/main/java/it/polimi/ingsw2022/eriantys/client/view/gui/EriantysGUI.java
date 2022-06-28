package it.polimi.ingsw2022.eriantys.client.view.gui;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu.EnterUsername;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu.LoadOrCreateGame;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu.LobbyWaiting;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu.Start;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EriantysGUI extends Application implements View {

    private static EriantysGUI instance;

    public static final double DEFAULT_SCENE_WIDTH = 1280;
    public static final double DEFAULT_SCENE_HEIGHT = 720;

    private Stage mainStage;
    private Scene currentScene;
    private GameController gameController;
    private boolean running;

    public static EriantysGUI launch(boolean showLog) throws InterruptedException {

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {

            try {

                instance.stop();
            }
            catch(Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();
        });

        new Thread(() -> Application.launch(EriantysGUI.class)).start();
        while(instance == null) Thread.sleep(100);

        return instance;
    }

    public EriantysGUI() {

        instance = this;
        running = true;
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Eriantys");
        mainStage = stage;

        Image icon = new Image("Images/application_logo.png");
        stage.getIcons().add(icon);

        setScene("WelcomeScreen.fxml", new Start(this));
    }

    public void setScene(String FXMLFileName, SceneController controller) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/" + FXMLFileName));
        loader.setController(controller);
        Parent root = loader.load();

        Scene previousScene = currentScene;

        if(previousScene != null) {

            currentScene = new Scene(root, previousScene.getWidth(), previousScene.getHeight());
            resizeScene();
        }
        else currentScene = new Scene(root);

        currentScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/LabelStyles.css")).toExternalForm());

        Platform.runLater(() -> {

            mainStage.setScene(currentScene);
            mainStage.show();

            ChangeListener<Number> resizeListener = (observableValue, number, t1) -> resizeScene();
            currentScene.widthProperty().addListener(resizeListener);
            currentScene.heightProperty().addListener(resizeListener);

            currentScene.setOnKeyPressed((keyEvent -> {

                if(keyEvent.getCode() == KeyCode.F11) mainStage.setFullScreen(!mainStage.isFullScreen());
            }));
        });
    }

    private void resizeScene() {

        double scaleFactor = currentScene.getWidth() / DEFAULT_SCENE_WIDTH;

        Scale scale = new Scale(scaleFactor, scaleFactor);
        scale.setPivotX(0);
        scale.setPivotY(0);
        currentScene.getRoot().getTransforms().setAll(scale);

        currentScene.getRoot().setTranslateY((currentScene.getHeight() - DEFAULT_SCENE_HEIGHT * scaleFactor) / 2);
    }

    @Override
    public void askUsername(Message requestMessage) throws IOException {

        setScene("UsernameSelection.fxml", new EnterUsername(this, requestMessage));
    }

    @Override
    public void askGameSettings(Message requestMessage) throws IOException {

        setScene("LoadOrCreateGameSelection.fxml", new LoadOrCreateGame(this, requestMessage));
    }

    @Override
    public void showUpdatedLobby(String[] playerUsernames, GameSettings gameSettings) throws IOException {

        setScene("Lobby.fxml", new LobbyWaiting(this, playerUsernames, gameSettings));
    }

    @Override
    public void startGame(List<Player> players, GameMode gameMode) throws IOException {

        gameController = new GameController(this, players);
        setScene("Game.fxml", gameController);
    }

    @Override
    public void applyUpdate(Update update) {

        if(gameController!=null) update.applyChanges(gameController);
    }

    @Override
    public void requestMove(MoveRequestMessage requestMessage) {

        requestMessage.moveRequest.manage(gameController, requestMessage);
    }

    @Override
    public void endGame(Team team) {

        gameController.endGame(team);
    }

    @Override
    public void stop() throws Exception {

        if(running) {

            super.stop();
            EriantysClient client = EriantysClient.getInstance();
            if(client.isRunning()) client.exit(true);
        }
    }
}
