package it.polimi.ingsw2022.eriantys.client.view.gui.menuControllers;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.messages.Message;


import java.io.IOException;

public class LoadOrCreateGame extends SceneController {

    private final Message requestMessage;

    public LoadOrCreateGame(EriantysGUI gui, Message requestMessage) {

        super(gui);
        this.requestMessage = requestMessage;
    }

    public void newGame() throws IOException {

        getGui().setScene("GameModeSelection.fxml", new GameMode(getGui(), requestMessage));
    }

    public void loadGame() throws IOException {

        //EriantysClient.getInstance().sendToServer(new GameSettingsMessage
                //(getGui().getPreviousMessage(), new GameSettings()));
    }
}
