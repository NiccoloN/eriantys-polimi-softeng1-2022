package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents cli component containing the stats of a player
 * @author Niccolò Nicolosi
 * @see PlayerStatusCLIComponent
 */
class PlayerStatsCLIComponent extends BasicCLIComponent {
    
    public static final int WIDTH = 8, HEIGHT = 8;
    private final String teamColor;
    private final GameMode gameMode;
    private final Map<PawnColor, Integer> entranceStudents;
    private String color;
    private int coins, towers;
    
    /**
     * Constructs a player stats cli component with the given team color
     * @param teamColor the team color to associate to this component
     */
    PlayerStatsCLIComponent(String teamColor, GameMode gameMode) {
        
        super(WIDTH, HEIGHT);
        
        color = PlayerStatusCLIComponent.PLAYER_STATUS_DEFAULT_COLOR;
        
        if (!isAnsiSequence(teamColor)) throw new InvalidParameterException("TeamColor is not an ansi sequence");
        this.teamColor = teamColor;
        
        this.gameMode = gameMode;
        
        coins = 1;
        towers = 0;
        
        entranceStudents = new HashMap<>(5);
        for (PawnColor color : PawnColor.values()) entranceStudents.put(color, 0);
        
        buildRows();
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
     * Sets the students of the given color visualized on this component
     * @param color  the color of the students
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
    
    private void buildRows() {
        
        int red = entranceStudents.get(PawnColor.RED);
        int green = entranceStudents.get(PawnColor.GREEN);
        int yellow = entranceStudents.get(PawnColor.YELLOW);
        int blue = entranceStudents.get(PawnColor.BLUE);
        int pink = entranceStudents.get(PawnColor.PINK);
        
        setRow(0, color + "_______ " + RESET);
        setRow(1, color + "       |" + RESET);
        setRow(2, color + " " + (gameMode == GameMode.EXPERT ? (YELLOW + "C" + RESET + "x" + (coins < 10 ? "0" : "") + coins + color) : "    ") + "  |" + RESET);
        setRow(3, color + "       |" + RESET);
        setRow(4, color + " " + teamColor + UNDERLINED + "II" + RESET + "x" + towers + color + "  |" + RESET);
        setRow(5, color + "       |" + RESET);
        setRow(6, color + " " + PawnColor.RED.ansiForegroundColor + (red == 0 ? " " : red) + PawnColor.GREEN.ansiForegroundColor + (green == 0 ? " " : green) + PawnColor.YELLOW.ansiForegroundColor + (yellow == 0 ? " " : yellow) + PawnColor.BLUE.ansiForegroundColor + (blue == 0 ? " " : blue) + PawnColor.PINK.ansiForegroundColor + (pink == 0 ? " " : pink) + color + " |" + RESET);
        setRow(7, color + "_______|" + RESET);
    }
}
