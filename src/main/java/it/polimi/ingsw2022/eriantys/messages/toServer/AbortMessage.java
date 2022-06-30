package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;

/**
 * This class represents a message sent by the client when he wants to quit what he is doing and stop the connection.
 *
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 */
public class AbortMessage extends ToServerMessage {
    
    public AbortMessage(Message previousMessage) {
        
        super(previousMessage);
    }
}
