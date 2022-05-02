package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.AckMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

public class GameSettingsMessage extends ToServerMessage {

    static {

        validResponses.add(AckMessage.class);
    }

    public final GameSettings gameSettings;

    public GameSettingsMessage(Message previousMessage, GameSettings gameSettings) {

        super(previousMessage);
        this.gameSettings = gameSettings;
    }

    @Override
    public void manageAndReply(Socket responseSocket) throws IOException {

        super.manageAndReply(responseSocket);

        EriantysServer server = EriantysServer.getInstance();
        server.addGameSettings(this.gameSettings);
    }
}
