package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;

import java.io.IOException;

/**
 * This class represent a request message for a move sent from the server to the client.
 * It specifies what kind of move through the enumeration MoveType
 * @author Emanuele Musto
 */
public class MoveRequestMessage extends ToClientMessage {

    public final MoveRequest moveRequest;

    static {

        validResponses.add(PerformedMoveMessage.class);
    }

    public MoveRequestMessage(MoveRequest moveRequest) {
        this.moveRequest = moveRequest;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().requestMove(this);
    }
}
