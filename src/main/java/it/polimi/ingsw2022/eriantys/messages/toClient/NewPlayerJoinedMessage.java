package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * This class represents a message sent by the server to every client when a new player joined the game.
 * It's used to update the lobby of the players that already joined the game.
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 * @see GameJoinedMessage
 */
public class NewPlayerJoinedMessage extends Message {
    
    private final String[] playerUsernames;
    
    public NewPlayerJoinedMessage(String[] playerUsernames) {
        
        this.playerUsernames = playerUsernames;
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        EriantysClient.getInstance().showUpdatedLobby(playerUsernames);
    }
}
