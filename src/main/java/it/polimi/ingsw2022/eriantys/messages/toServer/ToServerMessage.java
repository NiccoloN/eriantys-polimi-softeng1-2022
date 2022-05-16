package it.polimi.ingsw2022.eriantys.messages.toServer;

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

    public ToServerMessage(Message previousMessage, String clientUsername) {

        this.previousMessage = previousMessage;
        this.clientUsername = clientUsername;
    }

    public void manageAndReply(Socket responseSocket) throws IOException {

        EriantysServer server = EriantysServer.getInstance();

        if(previousMessage != null) {

            if(!previousMessage.isValidResponse(this)) server.sendToClient(new InvalidResponseMessage(this, previousMessage), responseSocket);
        }
    }
}
