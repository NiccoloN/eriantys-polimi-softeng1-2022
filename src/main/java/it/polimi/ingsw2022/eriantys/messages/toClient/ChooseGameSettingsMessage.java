package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.TimedMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represents a message to request the settings of the game to the first client that connects.
 * It accepts as a response a GameSettingsMessage containing the settings, or AbortMessage if the player
 * does not want to continue starting the game.
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 * @see GameSettingsMessage
 */
public class ChooseGameSettingsMessage extends TimedMessage {
    
    static {
        
        validResponses.add(GameSettingsMessage.class);
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient client = EriantysClient.getInstance();
        client.askGameSettings(this);
    }
    
    @Override
    public void waitForValidResponse() throws InterruptedException {
        
        waitForValidResponse(300, () -> {
            
            System.out.println("Game settings response timeout");
            EriantysServer.getInstance().shutdown(true);
        });
    }
}
