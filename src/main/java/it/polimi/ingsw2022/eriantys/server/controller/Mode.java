package it.polimi.ingsw2022.eriantys.server.controller;

/**
 * This enum contains every possible game mode.
 * @author Francesco Melegati Maccari
 */
public enum Mode {

    BASIC("Basic"),
    EXPERT("Expert");

    private String name;

    private Mode(String name) {
        this.name = name;
    }
}
