package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public abstract class ToClientMessage extends Message {

    public abstract void manageAndReply() throws IOException;
}
