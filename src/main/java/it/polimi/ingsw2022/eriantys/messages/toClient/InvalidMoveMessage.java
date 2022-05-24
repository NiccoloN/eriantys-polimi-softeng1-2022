package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a message sent to the client when the move made by the player is invalid.
 * It also specifies the reason.
 * @author Emanuele Musto
 */
public class InvalidMoveMessage extends InvalidResponseMessage {

    private final String cause;

    public InvalidMoveMessage(Message response, Message request, String cause) {

        super(response, request);
        this.cause = cause;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().log("Invalid move: " + cause);
        request.manageAndReply();
    }
}
