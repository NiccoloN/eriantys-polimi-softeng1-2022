package it.polimi.ingsw2022.eriantys.model;

public enum PawnColor {
    YELLOW("Gnomes"), BLUE("Unicorns"), GREEN("Frogs"), RED("Dragons"), PINK("Fairies");

    public final String name;

    PawnColor(String name){
        this.name = name;
    }
}
