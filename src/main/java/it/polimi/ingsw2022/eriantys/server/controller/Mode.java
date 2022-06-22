package it.polimi.ingsw2022.eriantys.server.controller;

public enum Mode {

    BASIC("Basic"),
    EXPERT("Expert");

    private String name;

    private Mode(String name) {
        this.name = name;
    }
}
