package it.polimi.ingsw2022.eriantys.client.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.components.BasicCLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

class PlayerStatsCLIComponent extends BasicCLIComponent {

    private String color;
    private final String teamColor;
    private int coins, towers;
    private int red, green, yellow, blue, pink;

    /**
     * Constructs a player stats cli component with the given team color
     * @param teamColor the team color to associate to this component
     */
    PlayerStatsCLIComponent(String teamColor) {

        super(8, 8);

        color = PlayerStatusCLIComponent.PLAYER_STATUS_DEFAULT_COLOR;

        if(!isAnsiSequence(teamColor)) throw new InvalidParameterException("TeamColor is not an ansi sequence");
        this.teamColor = teamColor;

        coins = 1;
        towers = 0;
        red = 0;
        green = 0;
        yellow = 0;
        blue = 0;
        pink = 0;

        buildRows();
    }

    private void buildRows() {

        setRow(0, color + "_______ " + RESET);
        setRow(1, color + "       |" + RESET);
        setRow(2, color + " " + YELLOW + "C" + color + "x" + (coins < 10 ? "0" : "") + coins + "  |" + RESET);
        setRow(3, color + "       |" + RESET);
        setRow(4, color + " " + teamColor + UNDERLINED + "II" + RESET + color + "x" + towers + "  |" + RESET);
        setRow(5, color + "       |" + RESET);
        setRow(6, color + " " +
                  RED + red +
                  GREEN_BRIGHT + green +
                  YELLOW + yellow +
                  BLUE_BRIGHT + blue +
                  PURPLE_BRIGHT + pink +
                  color + " |" + RESET);
        setRow(7, color + "_______|" + RESET);
    }

    @Override
    public void printToFrame(Frame frame) {

        buildRows();
        super.printToFrame(frame);
    }

    @Override
    public void setColor(String color) {

        this.color = color;
    }

    /**
     * Sets the red students visualized on this component
     * @param red the number of red students to visualize
     * @throws InvalidParameterException if red is not between 0 and 9
     */
    void setRed(int red) {

        if (red < 0 || red > 9) throw new InvalidParameterException("Red must be >= 0 and <= 9");
        this.red = red;
    }

    /**
     * Sets the green students visualized on this component
     * @param green the number of green students to visualize
     * @throws InvalidParameterException if green is not between 0 and 9
     */
    void setGreen(int green) {

        if (green < 0 || green > 9) throw new InvalidParameterException("Green must be >= 0 and <= 9");
        this.green = green;
    }

    /**
     * Sets the yellow students visualized on this component
     * @param yellow the number of yellow students to visualize
     * @throws InvalidParameterException if yellow is not between 0 and 9
     */
    void setYellow(int yellow) {

        if (yellow < 0 || yellow > 9) throw new InvalidParameterException("Yellow must be >= 0 and <= 9");
        this.yellow = yellow;
    }

    /**
     * Sets the blue students visualized on this component
     * @param blue the number of blue students to visualize
     * @throws InvalidParameterException if blue is not between 0 and 9
     */
    void setBlue(int blue) {

        if (blue < 0 || blue > 9) throw new InvalidParameterException("Blue must be >= 0 and <= 9");
        this.blue = blue;
    }

    /**
     * Sets the pink students visualized on this component
     * @param pink the number of pink students to visualize
     * @throws InvalidParameterException if pink is not between 0 and 9
     */
    void setPink(int pink) {

        if (pink < 0 || pink > 9) throw new InvalidParameterException("Pink must be >= 0 and <= 9");
        this.pink = pink;
    }

    /**
     * Sets the number of coins visualized on this component
     * @param coins the number of coins to visualize
     * @throws InvalidParameterException if coins is not between 0 and 99
     */
    void setCoins(int coins) {

        if (coins < 0 || coins > 99) throw new InvalidParameterException("Coins must be >= 0 and <= 99");
        this.coins = coins;
    }

    /**
     * Sets the number of towers visualized on this component
     * @param towers the number of towers to visualize
     * @throws InvalidParameterException if towers is not between 0 and 9
     */
    void setTowers(int towers) {

        if (towers < 0 || towers > 9) throw new InvalidParameterException("Towers must be >= 0 and <= 9");
        this.towers = towers;
    }
}
