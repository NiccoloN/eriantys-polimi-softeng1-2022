package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
public class UsernameChoiceMessage extends Message {

    public final String username;

    public UsernameChoiceMessage(String username) {

        this.username = username;
    }

    @Override
    public void manageAndReply() {

        EriantysServer server = EriantysServer.getInstance();
        server.addCurrentlyConnectingClient(username);
        System.out.println("Added client with username: " + username);
    }
}
