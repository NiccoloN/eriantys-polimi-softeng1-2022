package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents an island cli component
 * @author Niccolò Nicolosi
 */
public class IslandCLIComponent extends BasicCLIComponent {

    public static final int WIDTH = 14, HEIGHT = 7;
    public static final String DEFAULT_COLOR = RESET;

    private String color;
    private String teamColor;
    private int index;
    private boolean tower, mother;
    private final Map<PawnColor, Integer> students;

    /**
     * Constructs an island cli component with the given index
     * @param index the index of this island
     */
    public IslandCLIComponent(int index) {

        super(WIDTH, HEIGHT);

        color     = DEFAULT_COLOR;
        teamColor = RESET;
        
        setIndex(index);

        tower = false;
        mother = false;
        
        students = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) students.put(color, 0);
        
        buildRows();
    }
    
    private void buildRows() {
        
        setRow(0, color + "   ________   " + RESET);
        setRow(1, color + "  /   " + teamColor + (tower ? UNDERLINED + "II" : "  ") + RESET + color + "   \\  " + RESET);
        setRow(2, color + " / " +
                  PawnColor.RED.ansiForegroundColor + (students.get(PawnColor.RED) < 10 ? "0" : "") + students.get(PawnColor.RED) + "    " +
                  PawnColor.GREEN.ansiForegroundColor + (students.get(PawnColor.GREEN) < 10 ? "0" : "") + students.get(PawnColor.GREEN) + color + " \\ " + RESET);
        setRow(3,color + "|     " +
                 PawnColor.YELLOW.ansiForegroundColor + (students.get(PawnColor.YELLOW) < 10 ? "0" : "") + students.get(PawnColor.YELLOW) + color + "     |" + RESET);
        setRow(4, color + " \\ " +
                  PawnColor.BLUE.ansiForegroundColor + (students.get(PawnColor.BLUE) < 10 ? "0" : "") + students.get(PawnColor.BLUE) + "    " +
                  PawnColor.PINK.ansiForegroundColor + (students.get(PawnColor.PINK) < 10 ? "0" : "") + students.get(PawnColor.PINK) + color + " / " + RESET);
        setRow(5, color + "  \\___" + UNDERLINED + (mother ? "MM" : "  ") + RESET + color + "___/  " + RESET);
        setRow(6, color + "      " + (index < 10 ? "0" : "") + index + "      " + RESET);
    }

    @Override
    public void printToFrame(Frame frame) {

        buildRows();
        super.printToFrame(frame);
    }

    @Override
    public void setColor(String ansiColor) {

        color = ansiColor;
    }

    /**
     * @return the index of this island
     */
    public int getIndex() {

        return index;
    }

    /**
     * Sets the index of this island
     * @param index the new index
     */
    public void setIndex(int index) {

        if (index < 0 || index > 11) throw new InvalidParameterException("Index must be between 1 and 12");
        this.index = index;
    }

    /**
     * Sets the color of the tower of this island to the given team color
     * @param teamAnsiColor the new team color
     */
    public void setTeamColor(String teamAnsiColor) {
        
        teamColor = teamAnsiColor;
    }

    /**
     * Sets if the tower of this island is visible
     * @param tower the visibility of the tower: visible if true, hidden otherwise
     */
    public void setTower(boolean tower) {

        this.tower = tower;
    }

    /**
     * Sets if mother nature is visible on this island
     * @param mother the visibility of mother nature: visible if true, hidden otherwise
     */
    public void setMother(boolean mother) {

        this.mother = mother;
    }

    /**
     * Sets the students of the given color to visualize on this island
     * @param color the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 99
     */
    public void setStudents(PawnColor color, int number) {

        if (number < 0 || number > 99) throw new InvalidParameterException("Number must be >= 0 and <= 99");
        students.put(color, number);
    }
}
