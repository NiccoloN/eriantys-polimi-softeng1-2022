package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.menu;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class EnterUsername extends SceneController implements Initializable {

    private final Message requestMessage;
    @FXML
    private TextField usernameField;

    public EnterUsername(EriantysGUI gui, Message requestMessage) {

        super(gui);
        this.requestMessage = requestMessage;
    }

    public void sendUsername() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.setUsername(usernameField.getText());
        EriantysClient.getInstance().sendToServer(new UsernameChoiceMessage(requestMessage));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Pattern pattern = Pattern.compile(".{0,20}");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);

        usernameField.setTextFormatter(formatter);
    }
}
