package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

public class ConnectedMessage extends ToClientMessage {

    @Override
    public void manageAndReply() throws IOException {

        System.out.println("Successfully connected to server");
    }
}
