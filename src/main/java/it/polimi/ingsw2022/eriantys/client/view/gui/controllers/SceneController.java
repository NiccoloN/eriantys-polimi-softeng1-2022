package it.polimi.ingsw2022.eriantys.client.view.gui.controllers;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;

public abstract class SceneController {
    
    private final EriantysGUI gui;
    
    public SceneController(EriantysGUI gui) {this.gui = gui;}
    
    public EriantysGUI getGui() {
        
        return gui;
    }
}
