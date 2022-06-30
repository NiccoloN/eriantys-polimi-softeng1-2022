package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.TextAreaCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Optional;

import static it.polimi.ingsw2022.eriantys.client.EriantysClient.ADDRESS_FILE_NAME;

/**
 * This class represents a menu scene state in which the user can enter the ip of the server he wants to connect to
 *
 * @author Niccolò Nicolosi
 */
public class EnterServerIp extends MenuSceneState {
    
    private String serverIP;
    
    /**
     * Constructs an enter-server-ip state
     *
     * @param cli   the cli associated to this state
     * @param scene the menu scene associated to this state
     */
    public EnterServerIp(EriantysCLI cli, MenuScene scene) {
        
        super(cli, scene);
        serverIP = EriantysClient.getInstance().loadSavedServerIp();
    }
    
    @Override
    public void enter() {
        
        getScene().getEnterServerIpPrompt().setHidden(false);
        getScene().getTextArea().setHidden(false);
        getScene().getTextArea().setText(serverIP);
    }
    
    @Override
    public void exit() {
        
        getScene().getEnterServerIpPrompt().setHidden(true);
        getScene().getTextArea().setHidden(true);
        getScene().getTextArea().setText("");
    }
    
    @Override
    public void manageInput(Input input) throws IOException {
        
        if (input.triggersAction(Action.SELECT)) {
            
            if (serverIP.length() > 0) {
                
                File file = new File(ADDRESS_FILE_NAME);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write(serverIP);
                outputStreamWriter.close();
            }
            
            getScene().setState(new Start(getCli(), getScene()));
            return;
        }
        
        Optional<Character> character = input.getChar();
        if (character.isPresent()) {
            
            char c = character.get();
            
            TextAreaCLIComponent textArea = getScene().getTextArea();
            
            if (c == 8 || c == 127) {
                
                String prevText = textArea.getText();
                textArea.setText(prevText.substring(0, Math.max(0, prevText.length() - 1)));
            }
            else if ((c == '.' || (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) && serverIP.length() < 21)
                textArea.appendText(String.valueOf(c));
            
            serverIP = textArea.getText();
        }
    }
}
