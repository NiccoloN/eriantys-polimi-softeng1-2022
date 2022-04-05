package it.polimi.ingsw2022.eriantys.model.pawns;

/**
 * This class represents the five possible colors of the pawns used in the game.
 * @author Emanuele Musto
 */

public enum PawnColor {

    YELLOW("Gnome", "yellow"),
    BLUE("Unicorn", "blue"),
    GREEN("Frog", "green"),
    RED("Dragon", "red"),
    PINK("Fairy", "pink");

    public final String name;
    public final String colorName;

    /**
     * This method initializes the possible names.
     * @param name name of the species associated to the colors.
     * @param colorName string associated to the color.
     */

    PawnColor(String name, String colorName){
        this.name = name;
        this.colorName = colorName;
    }

}
