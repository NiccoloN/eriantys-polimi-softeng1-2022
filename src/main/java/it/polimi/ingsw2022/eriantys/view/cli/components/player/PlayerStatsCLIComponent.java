package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

public class PlayerStatsCLIComponent extends CLIComponent {

    private String color;
    private final String teamColor;
    private int coins, towers;
    private int red, green, yellow, blue, pink;

    public PlayerStatsCLIComponent(String teamColor) {

        super(9, 9);

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
        setRow(2, " " + YELLOW + "C" + color + "x01  |");
        setRow(3, "       |");
        setRow(4, " " + teamColor + "II" + color + "x8  |");
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
}
