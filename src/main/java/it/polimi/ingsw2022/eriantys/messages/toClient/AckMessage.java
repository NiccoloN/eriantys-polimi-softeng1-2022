package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

import java.net.Socket;

public class AckMessage extends Message {

    @Override
    public void manageAndReply(Socket responseSocket) {}
}
