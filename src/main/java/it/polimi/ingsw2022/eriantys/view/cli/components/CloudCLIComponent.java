package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

/**
 * This class represents a cloud cli component
 * @author Niccolò Nicolosi
 */
public class CloudCLIComponent extends CLIComponent {

    public static final String DEFAULT_COLOR = CYAN;

    private String color;
    private final int index;
    private int red, green, yellow, blue, pink;

    /**
     * Constructs a cloud cli component with the given index
     * @param index the index of this cloud
     */
    public CloudCLIComponent(int index) {

        super(9, 5);

        color = DEFAULT_COLOR;

        if (index < 0 || index > 9) throw new InvalidParameterException("Index must be a positive single digit integer");
        this.index = index;

        red = 0;
        green = 0;
        yellow = 0;
        blue = 0;
        pink = 0;

        buildRows();
    }

    private void buildRows() {

        setRow(0, color + "  _____  " + RESET);
        setRow(1, color + " (     ) " + RESET);
        setRow(2,color + "( " +
                 RED + red +
                 GREEN_BRIGHT + green +
                 YELLOW + yellow +
                 BLUE_BRIGHT + blue +
                 PURPLE_BRIGHT + pink +
                 color + " )" + RESET);
        setRow(3, color + " (_____) " + RESET);
        setRow(4, RESET + "    " + String.format("%01d", index) + "    ");
    }

    @Override
    public void printToFrame(String[][] frame) {

        buildRows();
        super.printToFrame(frame);
    }

    @Override
    public void setColor(String ansiColor) {

        color = ansiColor;
    }

    /**
     * @return the index of this cloud
     */
    public int getIndex() {

        return index;
    }
    
    /**
     * Sets the red students visualized on this cloud
     * @param red the number of red students to visualize
     */
    public void setRed(int red) {

        if (red < 0) throw new InvalidParameterException("Red must be >= 0");
        this.red = red;
    }

    /**
     * Sets the green students visualized on this cloud
     * @param green the number of green students to visualize
     */
    public void setGreen(int green) {

        if (green < 0) throw new InvalidParameterException("Green must be >= 0");
        this.green = green;
    }

    /**
     * Sets the yellow students visualized on this cloud
     * @param yellow the number of yellow students to visualize
     */
    public void setYellow(int yellow) {

        if (yellow < 0) throw new InvalidParameterException("Yellow must be >= 0");
        this.yellow = yellow;
    }
    
    /**
     * Sets the blue students visualized on this cloud
     * @param blue the number of blue students to visualize
     */
    public void setBlue(int blue) {

        if (blue < 0) throw new InvalidParameterException("Blue must be >= 0");
        this.blue = blue;
    }

    /**
     * Sets the pink students visualized on this cloud
     * @param pink the number of pink students to visualize
     */
    public void setPink(int pink) {

        if (pink < 0) throw new InvalidParameterException("Pink must be >= 0");
        this.pink = pink;
    }
}
