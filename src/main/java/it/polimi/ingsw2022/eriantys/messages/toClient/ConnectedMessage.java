package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 */
public class ConnectedMessage extends ToClientMessage {

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().log("Successfully connected to server");
    }
}
