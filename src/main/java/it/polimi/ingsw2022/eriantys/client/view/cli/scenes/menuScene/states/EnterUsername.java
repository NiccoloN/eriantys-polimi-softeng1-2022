package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.TextAreaCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;

import java.io.IOException;
import java.util.Optional;

/**
 * This class represents a menu scene state in which the user is asked to provide a username
 *
 * @author Niccolò Nicolosi
 */
public class EnterUsername extends MenuSceneState {
    
    private final Message requestMessage;
    private String username;
    
    /**
     * Constructs an enter-username state
     *
     * @param cli            the cli associated to this state
     * @param scene          the menu scene associated to this state
     * @param requestMessage the message that requested this state
     */
    public EnterUsername(EriantysCLI cli, MenuScene scene, Message requestMessage) {
        
        super(cli, scene);
        
        this.requestMessage = requestMessage;
        username = "";
    }
    
    @Override
    public void enter() {
        
        getScene().getEnterUsernamePrompt().setHidden(false);
        getScene().getTextArea().setHidden(false);
    }
    
    @Override
    public void exit() {
        
        getScene().getEnterUsernamePrompt().setHidden(true);
        getScene().getTextArea().setHidden(true);
    }
    
    @Override
    public void manageInput(Input input) throws IOException {
        
        if (input.triggersAction(Action.SELECT)) {
            
            EriantysClient client = EriantysClient.getInstance();
            client.setUsername(username);
            client.sendToServer(new UsernameChoiceMessage(requestMessage));
            
            //TODO client.sendToServer(new AbortMessage(requestMessage));
        }
        
        Optional<Character> character = input.getChar();
        if (character.isPresent()) {
            
            char c = character.get();
            
            TextAreaCLIComponent textArea = getScene().getTextArea();
            
            if (c == 8 || c == 127) {
                
                String prevText = textArea.getText();
                textArea.setText(prevText.substring(0, Math.max(0, prevText.length() - 1)));
            }
            else if (username.length() < EriantysServer.MAX_USERNAME_LENGTH) textArea.appendText(String.valueOf(c));
            
            username = textArea.getText();
        }
    }
}
