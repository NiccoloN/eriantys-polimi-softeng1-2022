package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;

import java.io.IOException;

public class ChooseUsernameMessage extends Message {

    static {

        validResponses.add(UsernameChoiceMessage.class);
        validResponses.add(AbortMessage.class);
    }

    @Override
    public void manageAndReply() throws IOException {

        EriantysClient client = EriantysClient.getInstance();
        client.sendToServer(new UsernameChoiceMessage(client.getUsername()));
    }
}
