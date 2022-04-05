package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.ColoredPawn;
import it.polimi.ingsw2022.eriantys.model.PawnColor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an island tile: the smallest piece that forms an island.
 * Island tiles can be aggregated to form a bigger island.
 * @author Niccolò Nicolosi
 * @see CompoundIslandTile
 */
public class IslandTile {

    private final List<ColoredPawn> students;
    private boolean tower;

    public IslandTile() {

        students = new ArrayList<>();
    }

    /**
     * @return the number of students placed on this tile
     */
    public int countStudents() {

        return students.size();
    }

    /**
     * @param color the color of students to count
     * @return the number of students of the given color currently placed on this tile
     */
    public int countStudents(PawnColor color) {

        int count = 0;
        for (ColoredPawn student : students) if (student.color == color) count++;
        return count;
    }

    /**
     * Places a colored pawn onto this tile. Only pawns that represent students should be placed on an island tile
     * @param student the student to place
     */
    public void addStudent(ColoredPawn student) {

        students.add(student);
    }

    /**
     * Removes the last placed student from this tile
     * @return the removed student
     * @throws IndexOutOfBoundsException if no students are placed on this tile
     */
    ColoredPawn removeStudent() {

        return students.remove(0);
    }

    /**
     * @return whether a tower has been placed on this tile
     */
    public boolean hasTower() {

        return tower;
    }

    /**
     * Places a tower onto this tile. Once a tower is placed, it cannot be removed.
     */
    public void addTower() {

        tower = true;
    }
}
