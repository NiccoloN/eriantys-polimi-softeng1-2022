package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class InvalidGameSettingsMessage extends ToClientMessage {

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.askGameSettings(this);
    }
}
