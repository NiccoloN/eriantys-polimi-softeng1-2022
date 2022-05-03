package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;

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
        GameSettings gameSettings = client.getGameSettings();
        if(gameSettings != null) client.sendToServer(new GameSettingsMessage(this, gameSettings));
        else client.sendToServer(new AbortMessage(this));
    }
}
