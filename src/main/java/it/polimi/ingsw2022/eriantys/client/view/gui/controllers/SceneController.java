package it.polimi.ingsw2022.eriantys.client.view.gui.controllers;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;

/**
 * This class represents a generic scene controller, associated to a gui
 * @author Niccolò Nicolosi
 */
public abstract class SceneController {
    
    private final EriantysGUI gui;
    
    /**
     * Constructs a scene controller associated with the given gui
     * @param gui the gui to associate to this scene
     */
    public SceneController(EriantysGUI gui) {
        
        this.gui = gui;
    }
    
    /**
     * @return the gui associated to this controller
     */
    public EriantysGUI getGui() {
        
        return gui;
    }
}
