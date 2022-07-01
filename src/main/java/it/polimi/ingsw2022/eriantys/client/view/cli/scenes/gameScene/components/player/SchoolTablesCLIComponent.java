package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents cli component containing the tables of a school (the dining room)
 * @author Niccolò Nicolosi
 * @see PlayerStatusCLIComponent
 */
class SchoolTablesCLIComponent extends BasicCLIComponent {
    
    public static final int WIDTH = 33, HEIGHT = 8;
    private final GameMode gameMode;
    private final Map<PawnColor, Boolean> professors;
    private final Map<PawnColor, Integer> tableStudents;
    private String color;
    private String username;
    
    /**
     * Constructs a school table cli component with the given username
     * @param username the username to associate to this component
     * @throws InvalidParameterException if the given username is not valid
     */
    SchoolTablesCLIComponent(String username, GameMode gameMode) {
        
        super(WIDTH, HEIGHT);
        
        color = PlayerStatusCLIComponent.PLAYER_STATUS_DEFAULT_COLOR;
        
        setUsername(username);
        
        this.gameMode = gameMode;
        
        professors = new HashMap<>(5);
        for (PawnColor color : PawnColor.values()) professors.put(color, false);
        
        tableStudents = new HashMap<>(5);
        for (PawnColor color : PawnColor.values()) tableStudents.put(color, 0);
        
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
    
    public String getUsername() {
        
        return username;
    }
    
    void setUsername(String username) {
        
        if (username.length() == 0) throw new InvalidParameterException("Username must be at least 1 character");
        if (username.length() > EriantysServer.MAX_USERNAME_LENGTH)
            throw new InvalidParameterException("Username must be at most " + EriantysServer.MAX_USERNAME_LENGTH + " characters");
        this.username = username;
    }
    
    /**
     * Sets the students of the given color to visualize on this component
     * @param color  the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 10
     */
    public void setTableStudents(PawnColor color, int number) {
        
        if (number < 0 || number > 10) throw new InvalidParameterException("Number must be >= 0 and <= 10");
        tableStudents.put(color, number);
    }
    
    /**
     * Sets if the professor of the given color is visible on this component
     * @param color the color of the professor
     * @param prof  whether the professor is visible or not
     */
    public void setProfessor(PawnColor color, boolean prof) {
        
        professors.put(color, prof);
    }
    
    private void buildRows() {
        
        setRow(0, color + " _" + username + "_".repeat(getWidth() - username.length() - 3) + " " + RESET);
        setRow(1, color + "|  " + ("_____" + YELLOW + "_" + RESET + color).repeat(3) + "___   ___  |" + RESET);
        setRow(2, buildTableRow(PawnColor.GREEN.ansiBackgroundColor, tableStudents.get(PawnColor.GREEN), professors.get(PawnColor.GREEN)));
        setRow(3, buildTableRow(PawnColor.RED.ansiBackgroundColor, tableStudents.get(PawnColor.RED), professors.get(PawnColor.RED)));
        setRow(4, buildTableRow(PawnColor.YELLOW.ansiBackgroundColor, tableStudents.get(PawnColor.YELLOW), professors.get(PawnColor.YELLOW)));
        setRow(5, buildTableRow(PawnColor.PINK.ansiBackgroundColor, tableStudents.get(PawnColor.PINK), professors.get(PawnColor.PINK)));
        setRow(6, buildTableRow(PawnColor.BLUE.ansiBackgroundColor, tableStudents.get(PawnColor.BLUE), professors.get(PawnColor.BLUE)));
        setRow(7, color + "|" + "_".repeat(getWidth() - 2) + "|" + RESET);
    }
    
    private String buildTableRow(String ansiColor, int students, boolean prof) {
        
        StringBuilder row = new StringBuilder(color);
        row.append("| |");
        for (int n = 0; n < 10; n++) {
            
            row.append("_");
            if (n < students) row.append(ansiColor);
            if ((n + 1) % 3 == 0) {
                
                row.append(YELLOW).append(UNDERLINED);
                if (gameMode == GameMode.EXPERT && n >= students) row.append("C");
                else row.append("_");
            }
            else row.append("_");
            
            row.append(RESET).append(color);
        }
        row.append("_| |_").append(prof ? ansiColor : RESET + color).append("_").append(RESET).append(color).append("_| |").append(RESET);
        return row.toString();
    }
}
