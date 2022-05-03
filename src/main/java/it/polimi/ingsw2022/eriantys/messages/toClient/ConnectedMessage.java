package it.polimi.ingsw2022.eriantys.messages.toClient;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 */
public class ConnectedMessage extends ToClientMessage {

    @Override
    public void manageAndReply() throws IOException {

        System.out.println("Successfully connected to server");
    }
}
