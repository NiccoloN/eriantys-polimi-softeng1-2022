package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a menu scene state in which the user is asked to select to load an existing game or create a new one
 * @author Niccolò Nicolosi
 */
public class LoadOrCreateGame extends MenuSceneState {
    
    private final Message requestMessage;
    
    private CLIComponent currentSelected;
    
    /**
     * Constructs a load-or-create menu scene state
     * @param cli            the cli associated to this state
     * @param scene          the menu scene associated to this state
     * @param requestMessage the message that requested this state
     */
    public LoadOrCreateGame(EriantysCLI cli, MenuScene scene, Message requestMessage) {
        
        super(cli, scene);
        this.requestMessage = requestMessage;
    }
    
    @Override
    public void enter() {
        
        BlinkingCLIComponent prompt = new BlinkingCLIComponent(1, new String[]{">"});
        prompt.setFirstColor(GREEN_BRIGHT);
        prompt.setSecondColor(GREEN);
        
        getScene().setPrompt(prompt);
        
        getScene().getPanel().setHidden(false);
        getScene().getNewGameOption().setHidden(false);
        getScene().getLoadGameOption().setHidden(false);
        
        switchSelected();
    }
    
    @Override
    public void exit() {
        
        getScene().setPrompt(null);
        
        getScene().getPanel().setHidden(true);
        getScene().getNewGameOption().setHidden(true);
        getScene().getLoadGameOption().setHidden(true);
    }
    
    @Override
    public void manageInput(Input input) throws IOException {
        
        if (input.triggersAction(Action.SELECT)) {
            
            if (currentSelected == getScene().getLoadGameOption())
                EriantysClient.getInstance().sendToServer(new GameSettingsMessage(requestMessage, new GameSettings()));
            
            else getScene().setState(new NumberOfPlayersSelection(getCli(), getScene(), requestMessage));
        }
        
        if (input.triggersAction(Action.UP) || input.triggersAction(Action.DOWN)) switchSelected();
    }
    
    private void switchSelected() {
        
        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = currentSelected == getScene().getLoadGameOption() ? getScene().getNewGameOption() : getScene().getLoadGameOption();
        currentSelected.setColor(GREEN);
        
        getScene().getPrompt().setPosition(currentSelected.getFrameX() - 2, currentSelected.getFrameY());
    }
}
