package it.polimi.ingsw2022.eriantys.server.model.pawns;

import it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes;

import java.io.Serializable;

/**
 * This class represents the five possible colors of the pawns used in the game and the associated names
 * @author Emanuele Musto
 */
public enum PawnColor implements Serializable {

    RED(AnsiCodes.RED, AnsiCodes.RED_BACKGROUND),
    GREEN(AnsiCodes.GREEN_BRIGHT, AnsiCodes.GREEN_BACKGROUND_BRIGHT),
    YELLOW(AnsiCodes.YELLOW, AnsiCodes.YELLOW_BACKGROUND),
    BLUE(AnsiCodes.BLUE_BRIGHT, AnsiCodes.BLUE_BACKGROUND_BRIGHT),
    PINK(AnsiCodes.PURPLE_BRIGHT, AnsiCodes.PURPLE_BACKGROUND_BRIGHT);

    public final String ansiForegroundColor;
    public final String ansiBackgroundColor;

    /**
     * Initializes a color
     * @param ansiForegroundColor the ansi foreground olor corresponding to this color in the cli
     * @param ansiBackgroundColor the ansi background olor corresponding to this color in the cli
     */
    PawnColor(String ansiForegroundColor, String ansiBackgroundColor) {

        this.ansiForegroundColor = ansiForegroundColor;
        this.ansiBackgroundColor = ansiBackgroundColor;
    }
}
