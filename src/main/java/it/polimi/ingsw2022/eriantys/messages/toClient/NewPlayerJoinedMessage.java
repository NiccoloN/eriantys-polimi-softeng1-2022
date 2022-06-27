package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;

import java.io.IOException;

/**
 * This class represents a message sent by the server to every client when a new player joined the game.
 * It's used to update the lobby of the players that already joined the game.
 * @see GameJoinedMessage
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class NewPlayerJoinedMessage extends ToClientMessage {

    private final String[] playerUsernames;

    public NewPlayerJoinedMessage(String[] playerUsernames) {

        this.playerUsernames = playerUsernames;
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().showUpdatedLobby(playerUsernames);
    }
}
