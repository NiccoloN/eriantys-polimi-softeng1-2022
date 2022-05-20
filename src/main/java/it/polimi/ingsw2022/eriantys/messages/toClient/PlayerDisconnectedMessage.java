package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;

import java.io.IOException;

public class PlayerDisconnectedMessage extends ToClientMessage{

    public PlayerDisconnectedMessage(){}

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().log("A player disconnected. Game ended.");
    }
}
