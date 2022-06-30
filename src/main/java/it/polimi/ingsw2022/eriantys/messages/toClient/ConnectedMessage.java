package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;

import java.io.IOException;

/**
 * This class represents a message to let the client know that the connection has successfully been established
 * between client and server.
 *
 * @author Niccolò Nicolosi
 */
public class ConnectedMessage extends ToClientMessage {
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient.getInstance().log("Successfully connected to server");
    }
}
