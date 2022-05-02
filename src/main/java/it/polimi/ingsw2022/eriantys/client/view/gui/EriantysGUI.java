package it.polimi.ingsw2022.eriantys.client.view.gui;

import it.polimi.ingsw2022.eriantys.client.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.TimeoutException;

public class EriantysGUI extends Application implements View {

    @Override
    public void start() throws TimeoutException {

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
    public String getUsername() {

        return "Prova";
    }
}
