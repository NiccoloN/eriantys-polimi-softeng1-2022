package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.ColoredPawn;

import java.util.List;

/**
 * This class represents a cloud
 * @author Niccolò Nicolosi
 */
public class CloudTile {

    private List<ColoredPawn> students;

    /**
     * Places a colored pawn onto this cloud. Only pawns that represent students should be placed on a cloud
     * @param student the student to place
     */
    public void addStudent(ColoredPawn student) {

        students.add(student);
    }

    /**
     * Removes all the students from this cloud
     * @return an array containing the removed students
     */
    public ColoredPawn[] withdrawStudents() {

        ColoredPawn[] students = new ColoredPawn[this.students.size()];
        this.students.toArray(students);
        return students;
    }

    /**
     * @return whether this cloud doesn't contain students
     */
    public boolean isEmpty() {

        return students.isEmpty();
    }
}
