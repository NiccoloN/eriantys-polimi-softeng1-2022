package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

class PlayerStatsCLIComponent extends CLIComponent {

    private String color;
    private final String teamColor;
    private int coins, towers;
    private int red, green, yellow, blue, pink;

    PlayerStatsCLIComponent(String teamColor) {

        super(8, 8);

        color = PlayerStatusCLIComponent.PLAYER_STATUS_DEFAULT_COLOR;
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

    void setRed(int red) {

        if (red < 0 || red > 9) throw new InvalidParameterException("Red must be >= 0 and <= 9");
        this.red = red;
    }

    void setGreen(int green) {

        if (green < 0 || green > 9) throw new InvalidParameterException("Green must be >= 0 and <= 9");
        this.green = green;
    }

    void setYellow(int yellow) {

        if (yellow < 0 || yellow > 9) throw new InvalidParameterException("Yellow must be >= 0 and <= 9");
        this.yellow = yellow;
    }

    void setBlue(int blue) {

        if (blue < 0 || blue > 9) throw new InvalidParameterException("Blue must be >= 0 and <= 9");
        this.blue = blue;
    }

    void setPink(int pink) {

        if (pink < 0 || pink > 9) throw new InvalidParameterException("Pink must be >= 0 and <= 9");
        this.pink = pink;
    }

    void setCoins(int coins) {

        if (coins < 0 || coins > 99) throw new InvalidParameterException("Coins must be >= 0 and <= 99");
        this.coins = coins;
    }

    void setTowers(int towers) {

        if (towers < 0 || towers > 9) throw new InvalidParameterException("Towers must be >= 0 and <= 9");
        this.towers = towers;
    }
}
