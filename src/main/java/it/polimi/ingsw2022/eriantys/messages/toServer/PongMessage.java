package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.PingMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.TimedMessage;

import java.io.IOException;

/**
 * This class represents a message sent by the client as a response to a ping message from the server.
 * It is used to test if the connection between client and server is working.
 * @author Emanuele Musto
 * @author Niccolò Nicolosi
 * @see PingMessage
 */
public class PongMessage extends ToServerMessage {
    
    static {
        
        validResponses.add(PingMessage.class);
    }
    
    public PongMessage(Message previousMessage) {
        
        super(previousMessage);
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        super.manageAndReply();
        ((TimedMessage) getPreviousMessage()).acceptResponse();
    }
}
