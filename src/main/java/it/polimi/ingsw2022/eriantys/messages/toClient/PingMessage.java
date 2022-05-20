package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.toServer.PongMessage;

import java.io.IOException;

public class PingMessage extends ToClientMessage{

    static {

        validResponses.add(PongMessage.class);
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().sendToServer(new PongMessage(this));
    }
}
