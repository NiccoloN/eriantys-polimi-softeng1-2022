package it.polimi.ingsw2022.eriantys.messages.toClient;

import java.io.IOException;

/**
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

        //TODO aggiornare view con le info sui nuovi player
    }
}
