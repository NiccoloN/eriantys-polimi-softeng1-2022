package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

/**
 * This class represents a menu scene state
 * @author Niccolò Nicolosi
 */
public abstract class MenuSceneState extends CLISceneState {
    
    /**
     * Constructs a menu scene state
     * @param cli   the cli associated to this state
     * @param scene the menu scene associated to this state
     */
    public MenuSceneState(EriantysCLI cli, MenuScene scene) {
        
        super(cli, scene);
    }
    
    @Override
    public MenuScene getScene() {
        
        return (MenuScene) super.getScene();
    }
}
