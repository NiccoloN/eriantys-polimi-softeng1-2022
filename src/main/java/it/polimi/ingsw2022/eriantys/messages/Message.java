package it.polimi.ingsw2022.eriantys.messages;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Message implements Serializable {

    protected static final List<Class<? extends Message>> validResponses = new ArrayList<>(2);

    public abstract void manageAndReply() throws IOException;

    public boolean isValidResponse(Message message) {

        return validResponses.contains(message.getClass());
    }
}
