package it.polimi.ingsw2022.eriantys.messages;

import java.io.IOException;

/**
 * This class represents a message sent by the client as a response to a ping message from the server.
 * It is used to test if the connection between client and server is working.
 * @author Emanuele Musto
 * @author Niccolò Nicolosi
 * @see PingMessage
 */
public class PongMessage extends Message {
    
    private final PingMessage previousMessage;
    
    static {
        
        validResponses.add(PingMessage.class);
    }
    
    public PongMessage(PingMessage previousMessage) {
        
        super();
        this.previousMessage = previousMessage;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        previousMessage.acceptResponse();
    }
}
