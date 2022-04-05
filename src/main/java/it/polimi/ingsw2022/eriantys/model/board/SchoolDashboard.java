package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.ColoredPawn;
import it.polimi.ingsw2022.eriantys.model.PawnColor;
import it.polimi.ingsw2022.eriantys.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a school. Every school is associated to a player
 * @author Niccolò Nicolosi
 * @see Player
 */
public class SchoolDashboard {

    public final Player player;
    private final List<ColoredPawn> entranceStudents;
    private final Map<PawnColor, List<ColoredPawn>> studentsTables;
    private final List<ColoredPawn> professors;
    private int towers;

    /**
     * Constructs a school associated to the given player
     * @param player the player to associate this school with
     */
    public SchoolDashboard(Player player) {

        this.player = player;
        entranceStudents = new ArrayList<>();
        studentsTables = new HashMap<>();
        for (PawnColor color : PawnColor.values()) studentsTables.put(color, new ArrayList<>());
        professors = new ArrayList<>();
    }

    /**
     * Place a colored pawn at the entrance of this school. Only pawns that represent students should be placed at the entrance
     * @param student the student to place
     * @throws RuntimeException if the entrance of this school already contains 7 or more students
     */
    public void addToEntrance(ColoredPawn student) {

        if (entranceStudents.size() >= 7) throw new RuntimeException("Maximum number of students at the entrance already reached");
        entranceStudents.add(student);
    }

    /**
     * Removes the given student from the entrance of this school, if present
     * @param student the student to remove
     * @return the removed student for chaining
     */
    public ColoredPawn removeFromEntrance(ColoredPawn student) {

        return entranceStudents.stream().filter((x) -> x == student).findAny().orElseThrow();
    }

    /**
     * Places the given student at the table of the corresponding color
     * @param student the student to place
     * @return the coins obtained by placing the student
     * @throws RuntimeException if the table of the corresponding color already contains 9 or more students
     */
    public int addToTable(ColoredPawn student) {

        List<ColoredPawn> table = studentsTables.get(student.color);
        if (table.size() >= 9) throw new RuntimeException("Maximum number of students at the " + student.color + " table already reached");
        table.add(student);
        return 0; //TODO classe separata per table
    }

    /**
     * Removes the last placed student from the table of the given color
     * @param color the color of the table from which to remove the student
     * @throws RuntimeException if the table of the given color is empty
     */
    public ColoredPawn removeFromTable(PawnColor color) {

        List<ColoredPawn> table = studentsTables.get(color);
        if (table.isEmpty()) throw new RuntimeException("Cannot remove from an empty table");
        return table.remove(table.size() - 1);
    }

    /**
     * @param color the color of the professor to check for
     * @return whether the professor of the given color is in this school
     */
    public boolean containsProfessor(PawnColor color) {

        return professors.stream().anyMatch((x) -> x.color == color);
    }
}
