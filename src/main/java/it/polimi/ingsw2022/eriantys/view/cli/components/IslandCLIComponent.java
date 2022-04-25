package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

public class IslandCLIComponent extends CLIComponent {

    private static final String ISLAND_DEFAULT_COLOR = RESET;
    private static final String MOTHER_NATURE_COLOR = RESET;

    private String color;
    private String teamColor;
    private int index;
    private boolean tower, mother;
    private int red, green, yellow, blue, pink;

    public IslandCLIComponent(int index) {

        super(14, 7);

        color     = ISLAND_DEFAULT_COLOR;
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
        setRow(5, color + "  \\___" + MOTHER_NATURE_COLOR + UNDERLINED + (mother ? "MM" : "  ") + RESET + color + "___/  " + RESET);
        setRow(6, color + "      " + (index < 10 ? "0" : "") + index + "      " + RESET);
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

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {

        if (index < 1 || index > 12) throw new InvalidParameterException("Index must be between 1 and 12");
        this.index = index;
    }

    public void setTeamColor(String teamAnsiColor) {
        
        teamColor = teamAnsiColor;
    }

    public void setTower(boolean tower) {

        this.tower = tower;
    }

    public void setMother(boolean mother) {

        this.mother = mother;
    }

    public void setRed(int red) {

        if (red < 0 || red > 99) throw new InvalidParameterException("Red must be >= 0 and <= 99");
        this.red = red;
    }

    public void setGreen(int green) {

        if (green < 0 || green > 99) throw new InvalidParameterException("Green must be >= 0 and <= 99");
        this.green = green;
    }

    public void setYellow(int yellow) {

        if (yellow < 0 || yellow > 99) throw new InvalidParameterException("Yellow must be >= 0 and <= 99");
        this.yellow = yellow;
    }

    public void setBlue(int blue) {

        if (blue < 0 || blue > 99) throw new InvalidParameterException("Blue must be >= 0 and <= 99");
        this.blue = blue;
    }

    public void setPink(int pink) {

        if (pink < 0 || pink > 99) throw new InvalidParameterException("Pink must be >= 0 and <= 99");
        this.pink = pink;
    }
}
