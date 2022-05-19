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
    public void manageAndReply(Socket responseSocket) throws IOException {

        super.manageAndReply(responseSocket);

        EriantysServer server = EriantysServer.getInstance();

        if(clientUsername.length() < 1 || clientUsername.length() > 15)
            server.sendToClient(new InvalidUsernameMessage(true, false), responseSocket);

        else if(server.isAvailableUsername(clientUsername)) {

            server.addClient(responseSocket, clientUsername);
            server.sendToClient(new AckMessage(), responseSocket);
        }

        else server.sendToClient(new InvalidUsernameMessage(false, true), responseSocket);
    }
}
