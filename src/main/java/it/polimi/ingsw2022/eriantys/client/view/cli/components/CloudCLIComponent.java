package it.polimi.ingsw2022.eriantys.client.view.cli.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a cloud cli component
 * @author Niccolò Nicolosi
 */
public class CloudCLIComponent extends BasicCLIComponent {

    public static final String DEFAULT_COLOR = CYAN;

    private String color;
    private final int index;
    private final Map<PawnColor, Integer> students;

    /**
     * Constructs a cloud cli component with the given index
     * @param index the index of this cloud
     */
    public CloudCLIComponent(int index) {

        super(9, 5);

        color = DEFAULT_COLOR;

        if (index < 0 || index > 9) throw new InvalidParameterException("Index must be a positive single digit integer");
        this.index = index;

        students = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) students.put(color, 0);

        buildRows();
    }

    private void buildRows() {

        setRow(0, color + "  _____  " + RESET);
        setRow(1, color + " (     ) " + RESET);
        setRow(2,color + "( " +
                 RED + students.get(PawnColor.RED) +
                 GREEN_BRIGHT + students.get(PawnColor.GREEN) +
                 YELLOW + students.get(PawnColor.YELLOW) +
                 BLUE_BRIGHT + students.get(PawnColor.BLUE) +
                 PURPLE_BRIGHT + students.get(PawnColor.PINK) +
                 color + " )" + RESET);
        setRow(3, color + " (_____) " + RESET);
        setRow(4, RESET + "    " + String.format("%01d", index) + "    ");
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
     * @return the index of this cloud
     */
    public int getIndex() {

        return index;
    }

    /**
     * Sets the students of the given color to visualize on this cloud
     * @param color the color of the students
     * @param number the number of students to visualize
     * @throws InvalidParameterException if number is not between 0 and 9
     */
    public void setStudents(PawnColor color, int number) {

        if (number < 0 || number > 9) throw new InvalidParameterException("Number must be >= 0 and <= 9");
        students.put(color, number);
    }
}
