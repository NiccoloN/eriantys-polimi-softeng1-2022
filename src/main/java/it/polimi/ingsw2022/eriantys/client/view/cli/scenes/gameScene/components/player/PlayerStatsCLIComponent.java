package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

class PlayerStatsCLIComponent extends BasicCLIComponent {

    private String color;
    private final String teamColor;
    private int coins, towers;
    private final Map<PawnColor, Integer> entranceStudents;

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

        entranceStudents = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) entranceStudents.put(color, 0);

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
                  RED + entranceStudents.get(PawnColor.RED) +
                  GREEN_BRIGHT + entranceStudents.get(PawnColor.GREEN) +
                  YELLOW + entranceStudents.get(PawnColor.YELLOW) +
                  BLUE_BRIGHT + entranceStudents.get(PawnColor.BLUE) +
                  PURPLE_BRIGHT + entranceStudents.get(PawnColor.PINK) +
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
     * Sets the students of the given color to visualize on this component
     * @param color the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 9
     */
    public void setEntranceStudents(PawnColor color, int number) {

        if (number < 0 || number > 9) throw new InvalidParameterException("Number must be >= 0 and <= 9");
        entranceStudents.put(color, number);
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
