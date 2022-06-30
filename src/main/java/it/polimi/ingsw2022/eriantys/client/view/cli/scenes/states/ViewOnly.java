package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;

/**
 * This class represents a "view only" state of a scene. In this state inputs are ignored
 *
 * @author Niccolò Nicolosi
 */
public class ViewOnly extends CLISceneState {
    
    public ViewOnly(EriantysCLI cli, CLIScene scene) {
        
        super(cli, scene);
    }
    
    @Override
    public void enter() {
    
    }
    
    @Override
    public void exit() {
    
    }
    
    @Override
    public void manageInput(Input input) {
    
    }
}
