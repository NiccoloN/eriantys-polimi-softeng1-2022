package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toClient.AckMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidGameSettingsMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.TimedMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represents a message that answers to the game settings request.
 * It is sent by the client when he has to choose the game mode of the game.
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @see GameSettings
 */
public class GameSettingsMessage extends ToServerMessage {
    
    static {
        
        validResponses.add(AckMessage.class);
    }
    
    public final GameSettings gameSettings;
    
    public GameSettingsMessage(Message previousMessage, GameSettings gameSettings) {
        
        super(previousMessage);
        this.gameSettings = gameSettings;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        super.manageAndReply();
        
        EriantysServer server = EriantysServer.getInstance();
        if(gameSettings.isValid()) {
            
            ((TimedMessage) getPreviousMessage()).acceptResponse();
            server.addGameSettings(this.gameSettings);
            server.sendToClient(new AckMessage(), clientUsername);
        }
        else {
            
            server.sendToClient(new InvalidGameSettingsMessage(this, getPreviousMessage()), clientUsername);
        }
    }
}
