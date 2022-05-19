package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidResponseMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public abstract class ToServerMessage extends Message {

    public final Message previousMessage;
    public final String clientUsername;

    public ToServerMessage(Message previousMessage) {

        this.previousMessage = previousMessage;
        clientUsername = EriantysClient.getInstance().getUsername();
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysServer server = EriantysServer.getInstance();

        if(previousMessage != null && !previousMessage.isValidResponse(this))
                server.sendToClient(new InvalidResponseMessage(this, previousMessage), clientUsername);
    }
}
