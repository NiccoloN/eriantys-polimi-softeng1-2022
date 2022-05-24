package it.polimi.ingsw2022.eriantys.messages.toClient;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;

import java.io.IOException;

/**
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 */
public class InvalidUsernameMessage extends InvalidResponseMessage {

    static {

        validResponses.add(UsernameChoiceMessage.class);
        validResponses.add(AbortMessage.class);
    }

    public final boolean invalidFormat;
    public final boolean alreadyTaken;

    public InvalidUsernameMessage(Message response, Message request, boolean invalidFormat, boolean alreadyTaken) {

        super(response, request);
        this.invalidFormat = invalidFormat;
        this.alreadyTaken = alreadyTaken;
    }

    @Override
    public void manageAndReply() throws IOException {

        String logMessage = "Invalid username provided: ";
        if(invalidFormat) logMessage += "invalid format";
        else if (alreadyTaken) logMessage += "already taken";
        EriantysClient.getInstance().log(logMessage);
        request.manageAndReply();
    }
}
