package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.TimedMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represents a message to request the username to a player.
 * It accepts as a response a UsernameChoiceMessage containing the username of the player,
 * or AbortMessage if the player does not want to continue starting the game.
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 * @see UsernameChoiceMessage
 */
public class ChooseUsernameMessage extends TimedMessage {
    
    static {
        
        validResponses.add(UsernameChoiceMessage.class);
    }
    
    public ChooseUsernameMessage() {
        
        super();
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient client = EriantysClient.getInstance();
        client.askUsername(this);
    }
    
    @Override
    public void waitForValidResponse() throws InterruptedException {
        
        waitForValidResponse(300, () -> {
            
            System.out.println("Username response timeout");
            EriantysServer.getInstance().shutdown(true);
        });
    }
}
