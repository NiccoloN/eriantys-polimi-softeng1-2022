package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.AckMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidUsernameMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class UsernameChoiceMessage extends ToServerMessage {

    public UsernameChoiceMessage(Message previousMessage) {

        super(previousMessage);
    }

    @Override
    public void manageAndReply() throws IOException {

        super.manageAndReply();

        EriantysServer server = EriantysServer.getInstance();

        if(clientUsername.length() < 1 || clientUsername.length() > 15)
            server.sendToClient(new InvalidUsernameMessage(true, false), server.getCurrentlyConnectingClient());

        else if(server.isAvailableUsername(clientUsername)) {

            server.acceptResponse();
            server.addClient(server.getCurrentlyConnectingClient(), clientUsername);
            server.sendToClient(new AckMessage(), clientUsername);
        }

        else server.sendToClient(new InvalidUsernameMessage(false, true), server.getCurrentlyConnectingClient());
    }
}
