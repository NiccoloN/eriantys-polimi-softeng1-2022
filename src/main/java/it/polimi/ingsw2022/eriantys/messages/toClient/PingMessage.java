package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.toServer.PongMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represents a ping message sent in order to establish that the connection is still alive between
 * client and server. It's a timed message, so the client has 10 seconds in order to respond with a pong message,
 * otherwise the connection is considered cut and the server shutdowns.
 * @author Emanuele Musto
 * @author Niccolò Nicolosi
 * @see PongMessage
 */
public class PingMessage extends TimedMessage {
    
    static {
        
        validResponses.add(PongMessage.class);
    }
    
    public PingMessage() {
        
        super();
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient.getInstance().sendToServer(new PongMessage(this));
    }
    
    @Override
    public void waitForValidResponse() throws InterruptedException {
        
        waitForValidResponse(10, () -> {
            try {
                System.out.println("Ping response timeout");
                EriantysServer.getInstance().shutdown(true);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
