package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

public class CloudCLIComponent extends CLIComponent {

    private static final String CLOUD_DEFAULT_COLOR = CYAN;

    private String color;
    private final int index;
    private int red, green, yellow, blue, pink;

    public CloudCLIComponent(int index) {

        super(9, 5);

        color = CLOUD_DEFAULT_COLOR;

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

        setRow(0, color + "  _____  ");
        setRow(1, " (     ) ");
        setRow(2,"( " +
                 RED + red +
                 GREEN_BRIGHT + green +
                 YELLOW + yellow +
                 BLUE_BRIGHT + blue +
                 PURPLE_BRIGHT + pink +
                 color + " )");
        setRow(3, " (_____) ");
        setRow(4, "    " + String.format("%01d", index) + "    ");
    }

    @Override
    public String[] getRows() {

        buildRows();
        return super.getRows();
    }

    @Override
    public void setColor(String ansiColor) {

        color = ansiColor;
    }

    public void setRed(int red) {

        if (red < 0) throw new InvalidParameterException("Red must be >= 0");
        this.red = red;
    }

    public void setGreen(int green) {

        if (green < 0) throw new InvalidParameterException("Green must be >= 0");
        this.green = green;
    }

    public void setYellow(int yellow) {

        if (yellow < 0) throw new InvalidParameterException("Yellow must be >= 0");
        this.yellow = yellow;
    }

    public void setBlue(int blue) {

        if (blue < 0) throw new InvalidParameterException("Blue must be >= 0");
        this.blue = blue;
    }

    public void setPink(int pink) {

        if (pink < 0) throw new InvalidParameterException("Pink must be >= 0");
        this.pink = pink;
    }
}
