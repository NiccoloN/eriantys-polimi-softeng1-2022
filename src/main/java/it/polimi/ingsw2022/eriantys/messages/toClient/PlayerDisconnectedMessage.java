package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;

import java.io.IOException;

/**
 * This class represents a message sent by the server when a player disconnects (voluntarily or involuntarily).
 * The message is sent to every other player to let them know that the connection has been cut.
 */
public class PlayerDisconnectedMessage extends ToClientMessage{

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().log("A player disconnected from the server");
        throw new RuntimeException("Server shutdown");
    }
}
