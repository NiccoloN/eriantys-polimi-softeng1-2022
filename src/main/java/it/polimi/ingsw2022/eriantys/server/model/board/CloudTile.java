package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a cloud
 * @author Niccolò Nicolosi
 */
public class CloudTile implements Serializable {

    private final List<ColoredPawn> students;

    public CloudTile() {
        this.students = new ArrayList<>(2);
    }

    /**
     * Places a colored pawn onto this cloud. Only pawns that represent students should be placed on a cloud
     * @param student the student to place
     * @throws RuntimeException if this island already contains 3 or more students
     * @throws RuntimeException if the given student is already on this cloud
     */
    public void addStudent(ColoredPawn student) {

        if (students.size() >= 3) throw new RuntimeException("Maximum number of placed students already reached on this cloud");
        if (students.contains(student)) throw new RuntimeException("No duplicates allowed");
        students.add(student);
    }

    /**
     * Removes all the students from this cloud
     * @return a List containing the removed students
     */
    public List<ColoredPawn> withdrawStudents() {
        List<ColoredPawn> students = new ArrayList<>(this.students);
        this.students.clear();
        return students;
    }

    /**
     * @param color the color of students to count
     * @return the number of students of the given color currently placed on this tile
     */
    public int countStudents(PawnColor color) {

        return (int) students.stream().filter((x) -> x.color == color).count();
    }

    /**
     * @return whether this cloud doesn't contain students
     */
    public boolean isEmpty() {

        return students.isEmpty();
    }
}
