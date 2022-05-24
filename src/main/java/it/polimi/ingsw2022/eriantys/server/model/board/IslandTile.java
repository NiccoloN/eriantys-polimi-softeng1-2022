package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an island tile: the smallest piece that forms an island.
 * Island tiles can be aggregated to form a bigger island.
 * @author Niccolò Nicolosi
 * @see CompoundIslandTile
 */
public class IslandTile implements Serializable {

    private final List<ColoredPawn> students;
    private boolean motherNature;
    private boolean tower;

    IslandTile() {

        students = new ArrayList<>();
        motherNature = false;
        tower = false;
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
        return (int) students.stream().filter((x) -> x.color == color).count();
    }

    /**
     * @param student the student to check for
     * @return whether the given student is placed on this island
     */
    public boolean containsStudent(ColoredPawn student) {

        return students.contains(student);
    }

    /**
     * Places a colored pawn onto this tile. Only pawns that represent students should be placed on an island tile
     * @param student the student to place
     * @throws RuntimeException if the given student is already in this tile
     */
    void addStudent(ColoredPawn student) {

        if (students.contains(student)) throw new RuntimeException("No duplicates allowed");
        students.add(student);
    }

    /**
     * Removes the last placed student from this tile
     * @return the removed student
     * @throws IndexOutOfBoundsException if no students are placed on this tile
     */
    ColoredPawn removeStudent() {

        return students.remove(students.size() - 1);
    }


    /**
     * @return whether mother nature is currently on this specific tile of an island
     */
    public boolean hasMotherNature() {

        return motherNature;
    }

    /**
     * Sets whether mother nature is currently on this specific tile of an island
     * @param motherNature true to place mother nature on this tile, false to remove it
     */
    void setMotherNature(boolean motherNature) {

        this.motherNature = motherNature;
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
    void addTower() {
        tower = true;
    }
}
