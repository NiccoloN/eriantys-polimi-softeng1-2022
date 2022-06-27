package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a generic message sent to the client by the server.
 * @see Message
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public abstract class ToClientMessage extends Message {

    @Override
    public abstract void manageAndReply() throws IOException;
}
