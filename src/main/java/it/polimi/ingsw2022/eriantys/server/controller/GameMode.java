package it.polimi.ingsw2022.eriantys.server.controller;

/**
 * This enum contains every possible game mode.
 * @author Francesco Melegati Maccari
 */
public enum GameMode {
    
    BASIC("Basic"), EXPERT("Expert");
    
    private final String name;
    
    GameMode(String name) {
        
        this.name = name;
    }
}
