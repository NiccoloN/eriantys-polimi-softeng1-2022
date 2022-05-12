package it.polimi.ingsw2022.eriantys.server.model.pawns;

/**
 * This class represents the five possible colors of the pawns used in the game and the associated names
 * @author Emanuele Musto
 */
public enum PawnColor {

    YELLOW("gnome", "yellow"),
    BLUE("unicorn", "blue"),
    GREEN("frog", "green"),
    RED("dragon", "red"),
    PINK("fairy", "pink");

    public final String name;
    public final String colorName;

    /**
     * Initializes a color
     * @param name name of the species associated to the color
     * @param colorName string associated to the color
     */
    PawnColor(String name, String colorName) {

        this.name = name;
        this.colorName = colorName;
    }
}
