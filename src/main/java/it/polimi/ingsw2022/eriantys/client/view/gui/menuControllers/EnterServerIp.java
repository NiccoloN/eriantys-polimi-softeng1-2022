package it.polimi.ingsw2022.eriantys.client.view.gui.menuControllers;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw2022.eriantys.client.EriantysClient.ADDRESS_FILE_NAME;

public class EnterServerIp extends SceneController implements Initializable {

    @FXML
    private TextField ipAddressField;

    public EnterServerIp(EriantysGUI gui) {
        super(gui);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ipAddressField.setText(EriantysClient.getInstance().loadSavedServerIp());
    }

    public void saveServerIp(ActionEvent event) throws IOException {

        String serverIpAddress = ipAddressField.getText().trim();

        File file = new File(ADDRESS_FILE_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(serverIpAddress);
        outputStreamWriter.close();

        getGui().setScene("WelcomeScreen.fxml", new Start(getGui()));

    }
}
