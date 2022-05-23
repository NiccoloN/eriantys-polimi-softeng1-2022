package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

public class DisconnectMessage extends ToServerMessage {

    public DisconnectMessage() {

        super(null);
    }

    @Override
    public void manageAndReply() throws IOException {

        super.manageAndReply();
        EriantysServer.getInstance().shutdown(true);
    }
}
