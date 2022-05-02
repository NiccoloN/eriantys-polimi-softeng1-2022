package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

public class ConnectedMessage extends Message {

    @Override
    public void manageAndReply() {

        System.out.println("Successfully connected to server");
    }
}
