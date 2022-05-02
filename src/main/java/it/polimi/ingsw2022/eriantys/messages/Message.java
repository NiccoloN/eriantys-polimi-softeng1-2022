package it.polimi.ingsw2022.eriantys.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Message implements Serializable {

    protected static final List<Class<? extends Message>> validResponses = new ArrayList<>(2);

    public boolean isValidResponse(Message response) {

        return validResponses.contains(response.getClass());
    }
}
