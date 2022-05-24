package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class InvalidResponseMessage extends ToClientMessage {

    protected final Message response, request;

    public InvalidResponseMessage(Message response, Message request) {

        this.response = response;
        this.request = request;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().log("The given response (" + response.getClass().getSimpleName() +
                           ") was invalid for the request (" + request.getClass().getSimpleName() + ")");
    }
}
