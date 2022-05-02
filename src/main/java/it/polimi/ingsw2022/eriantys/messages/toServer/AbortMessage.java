package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;

public class AbortMessage extends ToServerMessage {

    public AbortMessage(Message previousMessage) {

        super(previousMessage);
    }
}
