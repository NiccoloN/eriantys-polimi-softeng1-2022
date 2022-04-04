package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.ColoredPawn;
import it.polimi.ingsw2022.eriantys.model.PawnColor;

import java.util.List;

/**
 * @author Niccolò Nicolosi
 * This class represents an island tile: the smallest piece of an island.
 * Island tiles can be aggregated to form a bigger island.
 * @see CompoundIslandTile
 * Colored pawns can be placed on an island tile. However, only pawns that represent students should be placed on it.
 * @see ColoredPawn
 * A tower can be placed on an island tile. Once a tower is placed, it cannot be removed.
 */
public class IslandTile {

    private List<ColoredPawn> students;
    private boolean tower;

    /**
     * @param color the color of the students to count
     * @return the number of students of the given color currently placed on this tile
     */
    public int countStudents(PawnColor color) {

        int count = 0;
        for (ColoredPawn student : students) if (student.color == color) count++;
        return count;
    }

    /**
     * Places a student onto this tile
     * @param student the student to place
     */
    public void addStudent(ColoredPawn student) {

        students.add(student);
    }

    /**
     * @return whether a tower is placed on this tile
     */
    public boolean hasTower() {

        return tower;
    }

    /**
     * Places a tower onto this tile
     */
    public void addTower() {

        tower = true;
    }
}
