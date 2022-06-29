package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represent a request message for a move sent from the server to the client, containing a move request.
 * @author Emanuele Musto
 * @see MoveRequest
 */
public class MoveRequestMessage extends TimedMessage {
    
    static {
        
        validResponses.add(PerformedMoveMessage.class);
    }
    
    public final MoveRequest moveRequest;
    
    public MoveRequestMessage(MoveRequest moveRequest) {
        
        this.moveRequest = moveRequest;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient.getInstance().requestMove(this);
    }
    
    @Override
    public void waitForValidResponse() throws InterruptedException {
        
        waitForValidResponse(300, () -> {
            
            try {
                
                System.out.println("Move response timeout");
                EriantysServer.getInstance().shutdown(true);
                //TODO mossa casuale
            }
            
            catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
