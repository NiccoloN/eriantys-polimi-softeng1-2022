package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.toServer.PongMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.net.Socket;

public class PingMessage extends TimedMessage{

    static {

        validResponses.add(PongMessage.class);
    }

    public PingMessage() {
        super();
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient.getInstance().sendToServer(new PongMessage(this));
    }

    @Override
    public void waitForValidResponse() throws InterruptedException {

        waitForValidResponse(20, () -> {
            try {
                System.out.println("Ping response timeout");
                EriantysServer.getInstance().shutdown();
            }

            catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
