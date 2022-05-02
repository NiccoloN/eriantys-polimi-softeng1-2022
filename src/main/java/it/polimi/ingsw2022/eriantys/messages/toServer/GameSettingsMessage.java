package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

public class GameSettingsMessage extends Message {
    public final GameSettings gameSettings;

    public GameSettingsMessage(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    @Override
    public void manageAndReply(Socket responseSocket) throws IOException {
        EriantysServer server = EriantysServer.getInstance();
        server.addGameSettings(this.gameSettings);
    }
}
