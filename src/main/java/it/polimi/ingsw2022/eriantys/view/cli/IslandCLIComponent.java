package it.polimi.ingsw2022.eriantys.view.cli;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiColorCodes.*;

public class IslandCLIComponent extends CLIComponent {

    private static final String ISLAND_DEFAULT_COLOR = YELLOW;

    private String islandColor;
    private String teamColor;
    private int index;
    private boolean tower, mother;
    private int red, green, yellow, blue, pink;

    public IslandCLIComponent(int index, String teamAnsiColor) {

        super(16, 6);

        islandColor = ISLAND_DEFAULT_COLOR;
        teamColor = teamAnsiColor;
        
        setIndex(index);
        setTower(false);
        setMother(false);
        setRed(0);
        setGreen(0);
        setYellow(0);
        setBlue(0);
        setPink(0);
        
        buildRows();
    }
    
    private void buildRows() {
        
        setRow(0, islandColor + "  .----------.  ");
        setRow(1, " /     " + teamColor + (tower ? "II" : "  ") + islandColor + "     \\ ");
        setRow(2,"|  " +
                 RED + String.format("%02d", red) +
                 GREEN_BRIGHT + String.format("%02d", green) +
                 YELLOW_BRIGHT + String.format("%02d", yellow) +
                 BLUE_BRIGHT + String.format("%02d", blue) +
                 PURPLE_BRIGHT + String.format("%02d", pink) +
                 islandColor + "  |");
        setRow(3, " \\     " + CYAN + (mother ? "MM" : "  ") + islandColor + "     / ");
        setRow(4,  "  '----------'  " + RESET);
        setRow(5, "       " + String.format("%02d", index) + "       ");
    }

    @Override
    public String[] getRows() {

        buildRows();
        return super.getRows();
    }

    @Override
    public void setColor(String ansiColor) {

        islandColor = ansiColor;
    }

    public void setIndex(int index) {

        assert index >= 1 && index <= 12;
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

        assert red >= 0;
        this.red = red;
    }

    public void setGreen(int green) {

        assert green >= 0;
        this.green = green;
    }

    public void setYellow(int yellow) {

        assert yellow >= 0;
        this.yellow = yellow;
    }

    public void setBlue(int blue) {

        assert blue >= 0;
        this.blue = blue;
    }

    public void setPink(int pink) {

        assert pink >= 0;
        this.pink = pink;
    }
}
