package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;

import java.io.IOException;

public class StartingGameMessage extends ToClientMessage {

    private final String[] players;
    private final Update update;

    public StartingGameMessage(String[] players, Update update) {

        this.update = update;
        this.players = players;
    }

    @Override
    public void manageAndReply() throws IOException {

        //TODO set players and order
        EriantysClient client = EriantysClient.getInstance();
        client.applyUpdate(update);
    }
}
