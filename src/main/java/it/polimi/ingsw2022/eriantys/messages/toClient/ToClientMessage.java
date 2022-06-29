package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a generic message sent to the client by the server.
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 * @see Message
 */
public abstract class ToClientMessage extends Message {
    
    @Override
    public abstract void manageAndReply() throws IOException;
}
