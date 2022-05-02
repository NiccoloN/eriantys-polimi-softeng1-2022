package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;

import java.io.IOException;
import java.net.Socket;

public class InvalidUsernameMessage extends Message {

    static {

        validResponses.add(UsernameChoiceMessage.class);
        validResponses.add(AbortMessage.class);
    }

    public final boolean invalidFormat;
    public final boolean alreadyTaken;

    public InvalidUsernameMessage(boolean invalidFormat, boolean alreadyTaken) {

        this.invalidFormat = invalidFormat;
        this.alreadyTaken = alreadyTaken;
    }

    @Override
    public void manageAndReply(Socket responseSocket) throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        String username = client.getUsername();
        if(username != null) client.sendToServer(new UsernameChoiceMessage(username));
        else client.sendToServer(new AbortMessage());
    }
}
