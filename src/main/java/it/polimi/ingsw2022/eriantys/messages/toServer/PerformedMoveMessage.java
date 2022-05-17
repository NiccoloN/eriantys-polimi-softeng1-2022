package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

/**
 * This class represents a Move performed by a player
 * @author Emanuele Musto
 */
public class PerformedMoveMessage extends ToServerMessage{

    public final Move move;

    static {

        validResponses.add(InvalidMoveMessage.class);
    }

    public PerformedMoveMessage(Message previousMessage, Move move, String username) {

        super(previousMessage, username);
        this.move = move;
    }

    @Override
    public void manageAndReply(Socket responseSocket) throws IOException {

        super.manageAndReply(responseSocket);

        EriantysServer server = EriantysServer.getInstance();

        server.setPerformedMoveMessage(this);
    }
}
