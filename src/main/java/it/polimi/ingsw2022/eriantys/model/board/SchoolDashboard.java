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

    /**
     * The player associated to this school
     */
    public final Player player;

    private final List<ColoredPawn> entranceStudents;
    private final Map<PawnColor, List<ColoredPawn>> studentsTables;
    private final List<ColoredPawn> professors;
    private final int maxTowers;
    private int towers;

    /**
     * Constructs a school associated to the given player, with a maximum number of towers
     * @param player the player to associate this school with
     */
    public SchoolDashboard(Player player, int maxTowers) {

        this.player = player;
        entranceStudents = new ArrayList<>();
        studentsTables = new HashMap<>();
        for (PawnColor color : PawnColor.values()) studentsTables.put(color, new ArrayList<>());
        professors = new ArrayList<>();
        this.maxTowers = maxTowers;
        towers = maxTowers;
    }

    /**
     * Place a colored pawn at the entrance of this school. Only pawns that represent students should be placed at the entrance
     * @param student the student to place
     * @throws RuntimeException if the entrance of this school already contains 7 or more students
     * @throws RuntimeException if the given student is already at the entrance of this school
     */
    public void addToEntrance(ColoredPawn student) {

        if (entranceStudents.size() >= 7) throw new RuntimeException("Maximum number of students at the entrance already reached");
        if (entranceStudents.contains(student)) throw new RuntimeException("No duplicates allowed");
        entranceStudents.add(student);
    }

    /**
     * Removes the given student from the entrance of this school, if present
     * @param student the student to remove
     * @return the removed student
     */
    public ColoredPawn removeFromEntrance(ColoredPawn student) {

        return entranceStudents.stream().filter((x) -> x == student).findAny().orElseThrow();
    }

    /**
     * Places a colored pawn at the table of the corresponding color. Only pawns that represent students should be placed with this method
     * @param student the student to place
     * @return the coins obtained by placing the student
     * @throws RuntimeException if the table of the corresponding color already contains 9 or more students
     * @throws RuntimeException if the given student is already at the table of its color
     */
    public int addToTable(ColoredPawn student) {

        List<ColoredPawn> table = studentsTables.get(student.color);
        if (table.size() >= 9) throw new RuntimeException("Maximum number of students at the " + student.color + " table already reached");
        if (table.contains(student)) throw new RuntimeException("No duplicates allowed");
        table.add(student);

        return table.size() % 3 == 0 ? 1 : 0;
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

    /**
     * Place a colored pawn into this school. Only pawns that represent professors should be place with this method
     * @param professor the professor to place
     * @throws RuntimeException if the given professor is already in this school
     */
    public void addProfessor(ColoredPawn professor) {

        if (professors.contains(professor)) throw new RuntimeException("No duplicates allowed");
        professors.add(professor);
    }

    /**
     * Removes the professor of the given color from this school
     * @param color the color of the professor to remove
     * @return the removed professor
     */
    public ColoredPawn removeProfessor(PawnColor color) {

        return professors.stream().filter((x) -> x.color == color).findAny().orElseThrow();
    }

    /**
     * Places a tower in this school
     * @throws RuntimeException if the number of towers in this school is already at its maximum
     */
    public void addTower() {

        if (towers >= maxTowers) throw new RuntimeException("Cannot add anymore towers");
        towers++;
    }

    /**
     * Removes a tower from this school
     * @throws RuntimeException if the number of towers in this school is 0
     */
    public void removeTower() {

        if (towers <= 0) throw new RuntimeException("There are no towers in this school");
        towers--;
    }
}
