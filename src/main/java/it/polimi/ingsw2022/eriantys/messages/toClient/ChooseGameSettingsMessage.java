package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class ChooseGameSettingsMessage extends TimedMessage {

    static {

        validResponses.add(GameSettingsMessage.class);
        validResponses.add(AbortMessage.class);
    }

    public ChooseGameSettingsMessage() {
        super();
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.askGameSettings(this);
    }

    @Override
    public void waitForValidResponse() throws InterruptedException {

        waitForValidResponse(60, () -> {
            try {
                System.out.println("Game settings response timeout");
                EriantysServer.getInstance().shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
