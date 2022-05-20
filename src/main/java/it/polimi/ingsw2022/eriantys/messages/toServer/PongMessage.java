package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.PingMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;


public class PongMessage extends ToServerMessage{

    static {

        validResponses.add(PingMessage.class);
    }

    public PongMessage(Message previousMessage) {
        super(previousMessage);
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysServer.getInstance().sendToClient(previousMessage, EriantysClient.getInstance().getUsername());
    }
}
