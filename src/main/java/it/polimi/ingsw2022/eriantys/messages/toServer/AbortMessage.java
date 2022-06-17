package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.moves.Abort;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 */
public class AbortMessage extends ToServerMessage {

    public AbortMessage(Message previousMessage) {

        super(previousMessage);
    }
}
