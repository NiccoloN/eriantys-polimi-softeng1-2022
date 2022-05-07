package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * This class represents a player status cli component: an aggregate of a school tables cli component and a player stats cli component
 * @see SchoolTablesCLIComponent
 * @see PlayerStatsCLIComponent
 * @author Niccolò Nicolosi
 */
public class PlayerStatusCLIComponent implements CLIComponent {

    static final String PLAYER_STATUS_DEFAULT_COLOR = RESET;

    private final int index;
    private final SchoolTablesCLIComponent tablesCLIComponent;
    private final PlayerStatsCLIComponent statsCLIComponent;

    /**
     * Constructs a player status cli component with the given index, nickname and team color
     * @param index the index of this component
     * @param nickname the nickname to associate to this component
     * @param teamAnsiColor the team color to associate to this component
     * @throws InvalidParameterException if the given index is not a positive single-digit integer
     * @throws InvalidParameterException if the given nickname is invalid
     * @throws InvalidParameterException if the given team color is not an ansi sequence
     */
    public PlayerStatusCLIComponent(int index, String nickname, String teamAnsiColor) {

        if (index < 0 || index > 9) throw new InvalidParameterException("Index must be a positive single digit-integer");
        this.index = index;

        tablesCLIComponent = new SchoolTablesCLIComponent(nickname);
        statsCLIComponent = new PlayerStatsCLIComponent(teamAnsiColor);
    }

    @Override
    public void printToFrame(Frame frame) {

        tablesCLIComponent.printToFrame(frame);
        statsCLIComponent.printToFrame(frame);
    }

    @Override
    public void setColor(String ansiColor) {

        tablesCLIComponent.setColor(ansiColor);
        statsCLIComponent.setColor(ansiColor);
    }

    @Override
    public void setHidden(boolean b) {

        tablesCLIComponent.setHidden(b);
        statsCLIComponent.setHidden(b);
    }

    @Override
    public int getFrameX() {

        return tablesCLIComponent.getFrameX();
    }

    @Override
    public float getX() {

        return tablesCLIComponent.getFrameX();
    }

    @Override
    public void setX(float x) {

        tablesCLIComponent.setX(x);
        statsCLIComponent.setX(x + tablesCLIComponent.getWidth());
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
    }

    @Override
    public void setPosition(float x, float y) {

        setX(x);
        setY(y);
    }

    @Override
    public int getWidth() {

        return tablesCLIComponent.getWidth() + statsCLIComponent.getWidth();
    }

    @Override
    public int getHeight() {

        return tablesCLIComponent.getHeight();
    }

    public int getIndex() {

        return index;
    }

    /**
     * Sets the students of the given color to visualize at the entrance
     * @param color the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 9
     */
    public void setEntranceStudents(PawnColor color, int number) {

        statsCLIComponent.setEntranceStudents(color, number);
    }

    /**
     * Sets the students to visualize at the table of the given color
     * @param color the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 10
     */
    public void setTableStudents(PawnColor color, int number) {

        tablesCLIComponent.setTableStudents(color, number);
    }

    /**
     * Sets if the professor of the given color is visible on this component
     * @param color the color of the professor
     * @param prof whether the professor is visible or not
     */
    public void setProfessor(PawnColor color, boolean prof) {

        tablesCLIComponent.setProfessor(color, prof);
    }
}