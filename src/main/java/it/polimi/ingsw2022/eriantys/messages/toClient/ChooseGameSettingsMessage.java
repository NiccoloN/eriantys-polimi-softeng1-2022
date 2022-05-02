package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

public class ChooseGameSettingsMessage extends Message {

    @Override
    public void manageAndReply(Socket serverSocket) throws IOException {
        EriantysClient client = EriantysClient.getInstance();
        EriantysServer server = EriantysServer.getInstance();

        GameSettings gameSettings = client.getGameSettings();
        client.sendToServer(new GameSettingsMessage(gameSettings));
    }
}
