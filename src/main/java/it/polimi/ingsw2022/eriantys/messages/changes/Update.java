package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an update sent from the server to the client or clients.
 * The update consists of many changes, one for every part of the model that changed during the game.
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 * @see Change
 */
public class Update implements Serializable {
    
    private final List<Change> changes;
    
    public Update() {
        
        this.changes = new ArrayList<>(1);
    }
    
    /**
     * Adds a change to this update.
     * @param change the change to add to the update.
     */
    public void addChange(Change change) {
        
        changes.add(change);
    }
    
    /**
     * Applies the single changes one by one, using their specific methods to modify the view.
     * @param scene the scene to update.
     */
    public void applyChanges(GameScene scene) {
        
        for (Change change : changes) change.apply(scene);
    }
    
    public void applyChanges(GameController controller) {
        
        for (Change change : changes) change.apply(controller);
    }
}
