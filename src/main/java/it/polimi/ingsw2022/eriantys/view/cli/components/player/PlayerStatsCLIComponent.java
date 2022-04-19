package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

class PlayerStatsCLIComponent extends CLIComponent {

    private String color;
    private final String teamColor;
    private int coins, towers;
    private int red, green, yellow, blue, pink;

    PlayerStatsCLIComponent(String teamColor) {

        super(8, 9);

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

        setRow(0, "_______ ");
        setRow(1, "       |");
        setRow(2, " " + YELLOW + "C" + color + "x" + String.format("%02d", coins) + "  |");
        setRow(3, "       |");
        setRow(4, " " + teamColor + "II" + color + "x" + towers + "  |");
        setRow(5, "       |");
        setRow(6, " " +
                  RED + red +
                  GREEN_BRIGHT + green +
                  YELLOW + yellow +
                  BLUE_BRIGHT + blue +
                  PURPLE_BRIGHT + pink +
                  RESET + " |");
        setRow(7, "       |");
        setRow(8, "_______|");
    }

    @Override
    public String[] getRows() {

        buildRows();
        return super.getRows();
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
