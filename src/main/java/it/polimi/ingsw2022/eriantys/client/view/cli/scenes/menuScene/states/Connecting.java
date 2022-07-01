package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.BLACK;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.BLUE_BACKGROUND_BRIGHT;

/**
 * This class represents a menu scene state in which the user is notified that the client is trying to connect to the server
 * @author Niccolò Nicolosi
 */
public class Connecting extends MenuSceneState {
    
    private final String serverIP;
    private final CLIComponent connectingLabel;
    
    /**
     * Constructs a connecting state
     * @param cli   the cli associated to this state
     * @param scene the menu scene associated to this state
     */
    protected Connecting(EriantysCLI cli, MenuScene scene) {
        
        super(cli, scene);
        
        serverIP = EriantysClient.getInstance().loadSavedServerIp();
        
        connectingLabel = new BasicCLIComponent(17 + serverIP.length(), new String[]{"Connecting to " + serverIP + "..."});
        connectingLabel.setColor(BLACK + BLUE_BACKGROUND_BRIGHT);
    }
    
    @Override
    public void enter() {
        
        getScene().setConnectingLabel(connectingLabel);
        
        new Thread(() -> {
            
            EriantysClient client = EriantysClient.getInstance();
            if (!client.connectToServer(serverIP)) {
                
                //add a little delay to actually see the connecting label in case the connection gets refused instantly
                try {
                    
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    
                    e.printStackTrace();
                }
                getScene().setState(new Start(getCli(), getScene()));
            }
        }).start();
    }
    
    @Override
    public void exit() {
        
        getScene().getConnectingLabel().setHidden(true);
    }
    
    @Override
    public void manageInput(Input input) {
    
    }
}
