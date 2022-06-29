package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a message sent by the server when it receives a response from the client that he was not expecting.
 * (ex. username choice in place of game settings choice)
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class InvalidResponseMessage extends ToClientMessage {
    
    protected final Message response, request;
    
    public InvalidResponseMessage(Message response, Message request) {
        
        this.response = response;
        this.request  = request;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        if(response != null && request != null) {
            
            EriantysClient.getInstance().log("The given response (" + response.getClass().getSimpleName() + ") was invalid for the request (" + request.getClass().getSimpleName() + ")");
        }
    }
}
