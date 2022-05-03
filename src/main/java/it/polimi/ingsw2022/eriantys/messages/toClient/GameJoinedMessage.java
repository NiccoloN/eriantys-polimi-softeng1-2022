package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class GameJoinedMessage extends ToClientMessage {

    private final String[] playerUsernames;
    public final GameSettings gameSettings;

    public GameJoinedMessage(String[] playerUsernames, GameSettings gameSettings) {

        this.playerUsernames = playerUsernames;
        this.gameSettings = gameSettings;
    }

    @Override
    public void manageAndReply() throws IOException {

        //TODO notificare view delle info sulla lobby
    }
}
