package it.polimi.ingsw2022.eriantys.messages;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a generic message. It has a list of valid responses, if the receiver does not respond with
 * a valid answer, the message is rejected.
 *
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public abstract class Message implements Serializable {
    
    protected static final List<Class<? extends Message>> validResponses = new ArrayList<>(2);
    
    /**
     * @param response the answer received for this message.
     * @return true is the response if valid, false otherwise.
     */
    public boolean isValidResponse(Message response) {
        
        return validResponses.contains(response.getClass());
    }
    
    /**
     * Method called when a message is received, it automatically manages the message received and reply with an answer.
     */
    public abstract void manageAndReply() throws IOException;
}
