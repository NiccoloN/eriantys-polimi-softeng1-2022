package it.polimi.ingsw2022.eriantys.view.cli.components;

import it.polimi.ingsw2022.eriantys.view.cli.Frame;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

/**
 * This class represents an island cli component
 * @author Niccolò Nicolosi
 */
public class IslandCLIComponent extends CLIComponent {

    public static final String DEFAULT_COLOR = RESET;

    private String color;
    private String teamColor;
    private int index;
    private boolean tower, mother;
    private int red, green, yellow, blue, pink;

    /**
     * Constructs an island cli component with the given index
     * @param index the index of this island
     */
    public IslandCLIComponent(int index) {

        super(14, 7);

        color     = DEFAULT_COLOR;
        teamColor = RESET;
        
        setIndex(index);

        tower = false;
        mother = false;
        red = 0;
        green = 0;
        yellow = 0;
        blue = 0;
        pink = 0;
        
        buildRows();
    }
    
    private void buildRows() {
        
        setRow(0, color + "   ________   " + RESET);
        setRow(1, color + "  /   " + teamColor + (tower ? UNDERLINED + "II" : "  ") + RESET + color + "   \\  " + RESET);
        setRow(2, color + " / " +
                  RED + (red < 10 ? "0" : "") + red + "    " +
                  GREEN_BRIGHT + (green < 10 ? "0" : "") + green + color + " \\ " + RESET);
        setRow(3,color + "|     " +
                 YELLOW + (yellow < 10 ? "0" : "") + yellow + color + "     |" + RESET);
        setRow(4, color + " \\ " +
                  BLUE_BRIGHT + (blue < 10 ? "0" : "") + blue + "    " +
                  PURPLE_BRIGHT + (pink < 10 ? "0" : "") + pink + color + " / " + RESET);
        setRow(5, color + "  \\___" + UNDERLINED + (mother ? "MM" : "  ") + RESET + color + "___/  " + RESET);
        setRow(6, color + "      " + (index < 10 ? "0" : "") + index + "      " + RESET);
    }

    @Override
    public void printToFrame(Frame frame) {

        buildRows();
        super.printToFrame(frame);
    }

    @Override
    public void setColor(String ansiColor) {

        color = ansiColor;
    }

    /**
     * @return the index of this island
     */
    public int getIndex() {

        return index;
    }

    /**
     * Sets the index of this island
     * @param index the new index
     */
    public void setIndex(int index) {

        if (index < 1 || index > 12) throw new InvalidParameterException("Index must be between 1 and 12");
        this.index = index;
    }

    /**
     * Sets the color of the tower of this island to the given team color
     * @param teamAnsiColor the new team color
     */
    public void setTeamColor(String teamAnsiColor) {
        
        teamColor = teamAnsiColor;
    }

    /**
     * Sets if the tower of this island is visible
     * @param tower the visibility of the tower: visible if true, hidden otherwise
     */
    public void setTower(boolean tower) {

        this.tower = tower;
    }

    /**
     * Sets if mother nature is visible on this island
     * @param mother the visibility of mother nature: visible if true, hidden otherwise
     */
    public void setMother(boolean mother) {

        this.mother = mother;
    }

    /**
     * Sets the red students visualized on this island
     * @param red the number of red students to visualize
     */
    public void setRed(int red) {

        if (red < 0 || red > 99) throw new InvalidParameterException("Red must be >= 0 and <= 99");
        this.red = red;
    }

    /**
     * Sets the green students visualized on this island
     * @param green the number of green students to visualize
     */
    public void setGreen(int green) {

        if (green < 0 || green > 99) throw new InvalidParameterException("Green must be >= 0 and <= 99");
        this.green = green;
    }

    /**
     * Sets the yellow students visualized on this island
     * @param yellow the number of yellow students to visualize
     */
    public void setYellow(int yellow) {

        if (yellow < 0 || yellow > 99) throw new InvalidParameterException("Yellow must be >= 0 and <= 99");
        this.yellow = yellow;
    }

    /**
     * Sets the blue students visualized on this island
     * @param blue the number of blue students to visualize
     */
    public void setBlue(int blue) {

        if (blue < 0 || blue > 99) throw new InvalidParameterException("Blue must be >= 0 and <= 99");
        this.blue = blue;
    }

    /**
     * Sets the pink students visualized on this island
     * @param pink the number of pink students to visualize
     */
    public void setPink(int pink) {

        if (pink < 0 || pink > 99) throw new InvalidParameterException("Pink must be >= 0 and <= 99");
        this.pink = pink;
    }
}
