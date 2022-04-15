package it.polimi.ingsw2022.eriantys.view.cli.components;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

public class IslandCLIComponent extends CLIComponent {

    private static final String ISLAND_DEFAULT_COLOR = RESET;
    private static final String MOTHER_NATURE_COLOR = RESET_UNDERLINED;

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
        
        setRow(0, color + "   ________   ");
        setRow(1, "  /   " + teamColor + (tower ? "II" : "  ") + color + "   \\  ");
        setRow(2, " / " +
                  RED + String.format("%02d", red) + "    " +
                  GREEN_BRIGHT + String.format("%02d", green) + color + " \\");
        setRow(3,"|     " +
                 YELLOW + String.format("%02d", yellow) + color + "     |");
        setRow(4, " \\ " +
                  BLUE_BRIGHT + String.format("%02d", blue) + "    " +
                  PURPLE_BRIGHT + String.format("%02d", pink) + color + " / ");
        setRow(5, "  \\___" + MOTHER_NATURE_COLOR + (mother ? "MM" : "  ") + color + "___/ " + RESET);
        setRow(6, "      " + String.format("%02d", index) + "      ");
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
