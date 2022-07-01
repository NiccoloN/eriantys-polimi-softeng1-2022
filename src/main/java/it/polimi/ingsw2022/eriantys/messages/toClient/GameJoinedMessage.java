package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;

import java.io.IOException;

/**
 * This class represents a message that lets the client know when he successfully joined a game.
 * This message is sent to the currently connecting player in order to display information in the lobby.
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class GameJoinedMessage extends Message {
    
    public final GameSettings gameSettings;
    private final String[] playerUsernames;
    
    public GameJoinedMessage(String[] playerUsernames, GameSettings gameSettings) {
        
        this.playerUsernames = playerUsernames;
        this.gameSettings = gameSettings;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient.getInstance().showUpdatedLobby(playerUsernames, gameSettings);
    }
}
