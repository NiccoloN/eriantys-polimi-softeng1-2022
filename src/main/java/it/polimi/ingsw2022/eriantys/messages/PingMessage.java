package it.polimi.ingsw2022.eriantys.messages;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
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
    
    public final String clientUsername;
    
    public PingMessage() {
        
        super();
        this.clientUsername = null;
    }
    
    public PingMessage(String clientUsername) {
        
        super(true);
        this.clientUsername = clientUsername;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        //client-side
        if (clientUsername != null) EriantysServer.getInstance().sendToClient(new PongMessage(this), clientUsername);
            //server-side
        else EriantysClient.getInstance().sendToServer(new PongMessage(this));
    }
    
    @Override
    public void waitForValidResponse() throws InterruptedException {
        
        waitForValidResponse(10, () -> {
            try {
                
                System.out.println("Ping response timeout");
                if (clientUsername != null) EriantysClient.getInstance().exit(false);
                else EriantysServer.getInstance().shutdown(true);
            }
            catch (IOException e) {
                
                e.printStackTrace();
            }
        });
    }
}
