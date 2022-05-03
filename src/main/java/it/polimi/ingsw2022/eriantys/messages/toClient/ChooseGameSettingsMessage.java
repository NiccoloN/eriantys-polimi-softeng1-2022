package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class ChooseGameSettingsMessage extends ToClientMessage {

    static {

        validResponses.add(GameSettingsMessage.class);
        validResponses.add(AbortMessage.class);
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        GameSettings gameSettings = client.getGameSettings();
        client.sendToServer(new GameSettingsMessage(this, gameSettings));
    }
}
