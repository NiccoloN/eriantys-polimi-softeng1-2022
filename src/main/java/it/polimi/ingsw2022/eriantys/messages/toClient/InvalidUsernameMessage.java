package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class InvalidUsernameMessage extends ToClientMessage {

    static {

        validResponses.add(UsernameChoiceMessage.class);
        validResponses.add(AbortMessage.class);
    }

    public final boolean invalidFormat;
    public final boolean alreadyTaken;

    public InvalidUsernameMessage(boolean invalidFormat, boolean alreadyTaken) {

        this.invalidFormat = invalidFormat;
        this.alreadyTaken = alreadyTaken;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.askUsername(this);
    }
}
