package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.AckMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.InvalidUsernameMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

public class UsernameChoiceMessage extends Message {

    public final String username;

    public UsernameChoiceMessage(String username) {

        this.username = username;
    }

    @Override
    public void manageAndReply(Socket responseSocket) throws IOException {

        EriantysServer server = EriantysServer.getInstance();
        if(server.isAvailableUsername(username)) {

            server.addClient(responseSocket, username);
            server.sendToClient(new AckMessage(), responseSocket);
        }
        else server.sendToClient(new InvalidUsernameMessage(false, true), responseSocket);
        System.out.println("Added client with username: " + username);
    }
}
