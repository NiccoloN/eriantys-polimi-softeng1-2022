package it.polimi.ingsw2022.eriantys.view.cli.components.player;

import it.polimi.ingsw2022.eriantys.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.view.cli.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.RESET;

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
     * Sets the red students visualized at the entrance
     * @param red the number of red students to visualize
     */
    public void setRedEntrance(int red) {

        statsCLIComponent.setRed(red);
    }

    /**
     * Sets the  green students visualized at the entrance
     * @param  green the number of  green students to visualize
     */
    public void setGreenEntrance(int green) {

        statsCLIComponent.setGreen(green);
    }

    /**
     * Sets the yellow students visualized at the entrance
     * @param yellow the number of yellow students to visualize
     */
    public void setYellowEntrance(int yellow) {

        statsCLIComponent.setYellow(yellow);
    }

    /**
     * Sets the blue students visualized at the entrance
     * @param blue the number of blue students to visualize
     */
    public void setBlueEntrance(int blue) {

        statsCLIComponent.setBlue(blue);
    }

    /**
     * Sets the pink students visualized at the entrance
     * @param pink the number of pink students to visualize
     */
    public void setPinkEntrance(int pink) {

        statsCLIComponent.setPink(pink);
    }

    /**
     * Sets the red students visualized at the corresponding table
     * @param red the number of red students to visualize
     */
    public void setRed(int red) {

        tablesCLIComponent.setRed(red);
    }

    /**
     * Sets the green students visualized at the corresponding table
     * @param green the number of green students to visualize
     */
    public void setGreen(int green) {

        tablesCLIComponent.setGreen(green);
    }

    /**
     * Sets the yellow students visualized at the corresponding table
     * @param yellow the number of yellow students to visualize
     */
    public void setYellow(int yellow) {

        tablesCLIComponent.setYellow(yellow);
    }

    /**
     * Sets the blue students visualized at the corresponding table
     * @param blue the number of blue students to visualize
     */
    public void setBlue(int blue) {

        tablesCLIComponent.setBlue(blue);
    }

    /**
     * Sets the pink students visualized at the corresponding table
     * @param pink the number of pink students to visualize
     */
    public void setPink(int pink) {

        tablesCLIComponent.setPink(pink);
    }

    /**
     * Sets if the red professor is visible
     * @param redProf the visibility of the red professor: visible if true, hidden otherwise
     */
    public void setRedProf(boolean redProf) {

        tablesCLIComponent.setRedProf(redProf);
    }

    /**
     * Sets if the green professor is visible
     * @param greenProf the visibility of the green professor: visible if true, hidden otherwise
     */
    public void setGreenProf(boolean greenProf) {

        tablesCLIComponent.setGreenProf(greenProf);
    }

    /**
     * Sets if the yellow professor is visible
     * @param yellowProf the visibility of the yellow professor: visible if true, hidden otherwise
     */
    public void setYellowProf(boolean yellowProf) {

        tablesCLIComponent.setYellowProf(yellowProf);
    }

    /**
     * Sets if the blue professor is visible
     * @param blueProf the visibility of the blue professor: visible if true, hidden otherwise
     */
    public void setBlueProf(boolean blueProf) {

        tablesCLIComponent.setBlueProf(blueProf);
    }

    /**
     * Sets if the pink professor is visible
     * @param pinkProf the visibility of the pink professor: visible if true, hidden otherwise
     */
    public void setPinkProf(boolean pinkProf) {

        tablesCLIComponent.setPinkProf(pinkProf);
    }
}
