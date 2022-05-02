package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

public class InvalidResponseMessage extends ToClientMessage {

    private final Message response, request;

    public InvalidResponseMessage(Message response, Message request) {

        this.response = response;
        this.request = request;
    }

    @Override
    public void manageAndReply() throws IOException {

        System.out.println("The given response (" + response.getClass().getSimpleName() +
                           ") was invalid for the request (" + request.getClass().getSimpleName() + ")");
    }
}
