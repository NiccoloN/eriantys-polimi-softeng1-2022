package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a generic message sent from the server to the client.
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class AckMessage extends Message {
    
    @Override
    public void manageAndReply() throws IOException {}
}
