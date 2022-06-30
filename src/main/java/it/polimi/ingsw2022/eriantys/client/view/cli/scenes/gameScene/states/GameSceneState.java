package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.GREEN;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.GREEN_BRIGHT;

/**
 * This class represents a state of the game scene
 *
 * @author Niccolò Nicolosi
 */
public abstract class GameSceneState extends CLISceneState {
    
    public final MoveRequestMessage requestMessage;
    protected BlinkingCLIComponent prompt;
    
    /**
     * Constructs a game scene state
     *
     * @param cli            the cli to associate to this state
     * @param scene          the game scene to associate to this state
     * @param requestMessage the message that requested this state or the previous one
     */
    public GameSceneState(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {
        
        super(cli, scene);
        this.requestMessage = requestMessage;
        
        prompt = new BlinkingCLIComponent(1, new String[]{"V"});
        prompt.setFirstColor(GREEN_BRIGHT);
        prompt.setSecondColor(GREEN);
    }
    
    @Override
    public void enter() {
        
        getScene().setPrompt(prompt);
    }
    
    @Override
    public void exit() {
        
        getScene().setPrompt(null);
    }
    
    @Override
    public GameScene getScene() {
        
        return (GameScene) super.getScene();
    }
}
