package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a message sent to the client when the move made by the player is invalid.
 * It also specifies the reason.
 * @author Emanuele Musto
 */
public class InvalidMoveMessage extends ToClientMessage{

    private final String cause;
    private final Message response, request;

    public InvalidMoveMessage(Message response, Message request, String cause){
        this.cause = cause;
        this.response = response;
        this.request = request;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().log("The given response (" + response.getClass().getSimpleName() +
                ") was invalid for the request (" + request.getClass().getSimpleName() + ")." + cause);
    }
}
