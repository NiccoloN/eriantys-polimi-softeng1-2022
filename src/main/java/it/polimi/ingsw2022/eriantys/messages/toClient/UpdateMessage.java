package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;

import java.io.IOException;

/**
 * This class represents a message sent from the server when a change occurred in the model, and the view must be updated.
 * It contains an update that consists in various changes.
 *
 * @see Update
 * @see it.polimi.ingsw2022.eriantys.messages.changes.Change
 */
public class UpdateMessage extends ToClientMessage {
    
    private final Update update;
    
    public UpdateMessage(Update update) {
        
        this.update = update;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient client = EriantysClient.getInstance();
        client.applyUpdate(update);
    }
}
