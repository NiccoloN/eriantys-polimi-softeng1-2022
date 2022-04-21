package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.pawns.ColoredPawn;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a cloud
 * @author Niccolò Nicolosi
 */
public class CloudTile {

    private List<ColoredPawn> students;

    public CloudTile() {
        this.students = new ArrayList<>();
    }

    /**
     * Places a colored pawn onto this cloud. Only pawns that represent students should be placed on a cloud
     * @param student the student to place
     * @throws RuntimeException if this island already contains 3 or more students
     * @throws RuntimeException if the given student is already on this cloud
     */
    public void addStudent(ColoredPawn student) {

        if (students.size() >= 3) throw new RuntimeException("Maximum number of placed students already reached on this island");
        if (students.contains(student)) throw new RuntimeException("No duplicates allowed");
        students.add(student);
    }

    /**
     * Removes all the students from this cloud
     * @return an array containing the removed students
     */
    public List<ColoredPawn> withdrawStudents() {
        List<ColoredPawn> students = new ArrayList<>(this.students);
        this.students.clear();
        return students;
    }

    /**
     * @return whether this cloud doesn't contain students
     */
    public boolean isEmpty() {

        return students.isEmpty();
    }
}
