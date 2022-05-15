package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;

import java.io.IOException;

/**
 * This class represent a request message for a move sent from the server to the client.
 * It specifies what kind of move through the enumeration MoveType
 * @author Emanuele Musto
 */
public class MoveRequestMessage extends ToClientMessage{

    public final MoveType requestedMove;

    static {

        validResponses.add(PerformedMoveMessage.class);
    }

    public MoveRequestMessage(MoveType moveType) {
        this.requestedMove = moveType;
    }

    @Override
    public void manageAndReply() throws IOException {
        EriantysClient.getInstance().log(requestedMove.sentence);
        EriantysClient.getInstance().askMoveType(this);
    }
}
