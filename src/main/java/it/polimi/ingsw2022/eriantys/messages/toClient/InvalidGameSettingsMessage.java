package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class InvalidGameSettingsMessage extends InvalidResponseMessage {

    public InvalidGameSettingsMessage(Message response, Message request) {

        super(response, request);
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().log("Invalid game settings provided");
        request.manageAndReply();
    }
}
