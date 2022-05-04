package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;

import java.io.IOException;

public class StartingGameMessage extends ToClientMessage {

    private String[] players;
    private Update update;

    public StartingGameMessage(String[] players, Update update) {
        this.update = update;
        this.players = players;
    }

    @Override
    public void manageAndReply() throws IOException {

    }
}
