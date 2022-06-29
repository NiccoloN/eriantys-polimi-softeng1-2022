package it.polimi.ingsw2022.eriantys.messages.toServer;

import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.TimedMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.AckMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidUsernameMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;

/**
 * This class represents a message sent as a response to the request of choosing a username.
 * It contains the username of the player, and also the checks for the validity of the username.
 * @author Niccolò Nicolosi
 * @author Francesco Melegati Maccari
 * @author Emanuele Musto
 * @see it.polimi.ingsw2022.eriantys.messages.toClient.ChooseUsernameMessage
 */
public class UsernameChoiceMessage extends ToServerMessage {
    
    public UsernameChoiceMessage(Message previousMessage) {
        
        super(previousMessage);
    }
    
    @Override
    public void manageAndReply() throws IOException {
        
        super.manageAndReply();
        
        EriantysServer server = EriantysServer.getInstance();
        
        if(clientUsername.length() < 1 || clientUsername.length() > EriantysServer.MAX_USERNAME_LENGTH) {
            
            server.sendToClient(new InvalidUsernameMessage(this, getPreviousMessage(), true, false), server.getCurrentlyConnectingClient());
        }
        
        else if(server.isAvailableUsername(clientUsername)) {
            
            TimedMessage request = (TimedMessage) getPreviousMessage();
            request.acceptResponse();
            server.addClient(server.getCurrentlyConnectingClient(), clientUsername);
            server.sendToClient(new AckMessage(), clientUsername);
        }
        
        else server.sendToClient(new InvalidUsernameMessage(this, getPreviousMessage(), false, true), server.getCurrentlyConnectingClient());
    }
}
