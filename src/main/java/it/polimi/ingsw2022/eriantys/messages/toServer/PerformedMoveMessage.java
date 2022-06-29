package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.moves.Move;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represents a Move performed by a player. It is sent by the client whenever the player makes a move
 * in response to a move request message, and contains the performed move.
 * @author Emanuele Musto
 * @see Move
 * @see MoveRequestMessage
 */
public class PerformedMoveMessage extends ToServerMessage {
    
    static {
        
        validResponses.add(InvalidMoveMessage.class);
    }
    
    public final Move move;
    
    public PerformedMoveMessage(MoveRequestMessage previousMessage, Move move) {
        
        super(previousMessage);
        this.move = move;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        super.manageAndReply();
        EriantysServer.getInstance().managePerformedMoveMessage(this);
    }
    
    @Override
    public MoveRequestMessage getPreviousMessage() {
        
        return (MoveRequestMessage) super.getPreviousMessage();
    }
}
