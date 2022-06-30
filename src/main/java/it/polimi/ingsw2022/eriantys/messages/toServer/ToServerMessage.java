package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidResponseMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represents a generic message sent by the client to the server. It stores the username of the client that
 * sent it, and the previous message that caused the client to send this message.
 *
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 * @see Message
 */
public abstract class ToServerMessage extends Message {
    
    public final String clientUsername;
    private final Message previousMessage;
    
    public ToServerMessage(Message previousMessage) {
        
        clientUsername = EriantysClient.getInstance().getUsername();
        this.previousMessage = previousMessage;
    }
    
    /**
     * It checks if this message is a valid response for the previously sent message by the server.
     */
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysServer server = EriantysServer.getInstance();
        
        if (previousMessage != null && !previousMessage.isValidResponse(this))
            server.sendToClient(new InvalidResponseMessage(this, previousMessage), clientUsername);
    }
    
    /**
     * @return the message sent by the server that caused this message to be sent as a response.
     */
    public Message getPreviousMessage() {
        
        return previousMessage;
    }
}
