package it.polimi.ingsw2022.eriantys.client.view.gui.menuControllers;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Start extends SceneController implements Initializable {

    @FXML
    ImageView backgroundImage;

    public Start(EriantysGUI gui) {
        super(gui);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        //imageView.setPreserveRatio(true);  //uncomment to keep image ratio.
        backgroundImage.fitHeightProperty().bind(getGui().getMainStage().heightProperty());
        backgroundImage.fitWidthProperty().bind(getGui().getMainStage().widthProperty());
    }

    public void startGame(ActionEvent event) throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.connectToServer(client.loadSavedServerIp());
    }

    public void setServerIpScene(ActionEvent event) throws IOException {

        getGui().setScene("ServerSelection.fxml", new EnterServerIp(getGui()));
    }
}
