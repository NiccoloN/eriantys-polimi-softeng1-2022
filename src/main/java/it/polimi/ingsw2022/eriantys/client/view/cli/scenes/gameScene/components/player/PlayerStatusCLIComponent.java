package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.HelperCardCLIComponent;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * This class represents a player status cli component: an aggregate of a school tables cli component and a player stats cli component
 *
 * @author Niccolò Nicolosi
 * @see SchoolTablesCLIComponent
 * @see PlayerStatsCLIComponent
 */
public class PlayerStatusCLIComponent implements CLIComponent {
    
    static final String PLAYER_STATUS_DEFAULT_COLOR = RESET;
    
    private final boolean helperLeft;
    private final SchoolTablesCLIComponent tablesCLIComponent;
    private final PlayerStatsCLIComponent statsCLIComponent;
    private HelperCardCLIComponent lastHelperCLIComponent;
    
    /**
     * Constructs a player status cli component with the given index, nickname and team color
     *
     * @param nickname      the nickname to associate to this component
     * @param teamAnsiColor the team color to associate to this component
     * @param helperLeft    whether the last played helper should be placed on the left instead of on the right of this component
     * @throws InvalidParameterException if the given index is not a positive single-digit integer
     * @throws InvalidParameterException if the given nickname is invalid
     * @throws InvalidParameterException if the given team color is not an ansi sequence
     */
    public PlayerStatusCLIComponent(String nickname, String teamAnsiColor, boolean helperLeft, GameMode gameMode) {
        
        this.helperLeft = helperLeft;
        tablesCLIComponent = new SchoolTablesCLIComponent(nickname, gameMode);
        statsCLIComponent = new PlayerStatsCLIComponent(teamAnsiColor, gameMode);
        setPosition(0, 0);
    }
    
    @Override
    public void printToFrame(Frame frame) {
        
        tablesCLIComponent.printToFrame(frame);
        statsCLIComponent.printToFrame(frame);
        if (lastHelperCLIComponent != null) lastHelperCLIComponent.printToFrame(frame);
    }
    
    @Override
    public int getFrameX() {
        
        return tablesCLIComponent.getFrameX();
    }
    
    @Override
    public float getX() {
        
        return helperLeft ? tablesCLIComponent.getX() - HelperCardCLIComponent.WIDTH - 1 : tablesCLIComponent.getX();
    }
    
    @Override
    public void setX(float x) {
        
        if (helperLeft) {
            
            if (lastHelperCLIComponent != null) lastHelperCLIComponent.setX(x);
            tablesCLIComponent.setX(x + HelperCardCLIComponent.WIDTH + 1);
            statsCLIComponent.setX(tablesCLIComponent.getX() + tablesCLIComponent.getWidth());
        }
        else {
            
            tablesCLIComponent.setX(x);
            statsCLIComponent.setX(tablesCLIComponent.getX() + tablesCLIComponent.getWidth());
            if (lastHelperCLIComponent != null)
                lastHelperCLIComponent.setX(statsCLIComponent.getX() + statsCLIComponent.getWidth() + 1);
        }
    }
    
    @Override
    public int getFrameY() {
        
        return tablesCLIComponent.getFrameY();
    }
    
    @Override
    public float getY() {
        
        return tablesCLIComponent.getY();
    }
    
    @Override
    public void setY(float y) {
        
        tablesCLIComponent.setY(y);
        statsCLIComponent.setY(y);
        if (lastHelperCLIComponent != null)
            lastHelperCLIComponent.setY(y + (getHeight() - lastHelperCLIComponent.getHeight()) / 2f);
    }
    
    @Override
    public void setPosition(float x, float y) {
        
        setX(x);
        setY(y);
    }
    
    @Override
    public int getWidth() {
        
        return tablesCLIComponent.getWidth() + statsCLIComponent.getWidth() + HelperCardCLIComponent.WIDTH + 1;
    }
    
    @Override
    public int getHeight() {
        
        return tablesCLIComponent.getHeight();
    }
    
    @Override
    public void setColor(String ansiColor) {
        
        tablesCLIComponent.setColor(ansiColor);
        statsCLIComponent.setColor(ansiColor);
    }
    
    @Override
    public void setHidden(boolean hidden) {
        
        tablesCLIComponent.setHidden(hidden);
        statsCLIComponent.setHidden(hidden);
        if (lastHelperCLIComponent != null) lastHelperCLIComponent.setHidden(hidden);
    }
    
    public String getNickname() {
        
        return tablesCLIComponent.getUsername();
    }
    
    /**
     * Sets the students of the given color to visualize at the entrance
     *
     * @param color  the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 9
     */
    public void setEntranceStudents(PawnColor color, int number) {
        
        statsCLIComponent.setEntranceStudents(color, number);
    }
    
    /**
     * Sets the students to visualize at the table of the given color
     *
     * @param color  the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 10
     */
    public void setTableStudents(PawnColor color, int number) {
        
        tablesCLIComponent.setTableStudents(color, number);
    }
    
    /**
     * Sets if the professor of the given color is visible on this component
     *
     * @param color the color of the professor
     * @param prof  whether the professor is visible or not
     */
    public void setProfessor(PawnColor color, boolean prof) {
        
        tablesCLIComponent.setProfessor(color, prof);
    }
    
    /**
     * Sets the coins to visualize on this component
     *
     * @param coins the coins to visualize on this component
     * @throws InvalidParameterException if number is not between 0 and 99
     */
    public void setCoins(int coins) {
        
        statsCLIComponent.setCoins(coins);
    }
    
    /**
     * Sets the towers to visualize on this component
     *
     * @param towers the towers to visualize on this component
     * @throws InvalidParameterException if number is not between 0 and 9
     */
    public void setTowers(int towers) {
        
        statsCLIComponent.setTowers(towers);
    }
    
    /**
     * Sets the last played helper to visualize on this component
     *
     * @param lastHelperCLIComponent the helper component to visualize
     */
    public void setLastHelperCLIComponent(HelperCardCLIComponent lastHelperCLIComponent) {
        
        if (lastHelperCLIComponent == null)
            throw new InvalidParameterException("Last helper component must be != null");
        this.lastHelperCLIComponent = lastHelperCLIComponent;
        setPosition(getX(), getY());
    }
}
