package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;

import java.io.IOException;

public class UpdateMessage extends ToClientMessage {

    private Update update;

    public UpdateMessage(Update update) {
        this.update = update;
    }

    @Override
    public void manageAndReply() throws IOException {
        EriantysClient client = EriantysClient.getInstance();
        client.applyUpdate(update);
    }
}
