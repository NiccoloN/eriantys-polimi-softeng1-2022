package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a message sent by the server when an error occurs (e.g. the server throws an exception or a player disconnects
 * (voluntarily or involuntarily). The message is sent to every connected player to let them know that the server is shutting down.
 */
public class ErrorMessage extends Message {
    
    private final String errorDescription;
    
    public ErrorMessage(String errorDescription) {
        
        this.errorDescription = errorDescription;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient.getInstance().log(errorDescription);
        EriantysClient.getInstance().exit(false);
    }
}
