package it.polimi.ingsw2022.eriantys.server.model.pawns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a bag of student pawns. It can be used to store and randomly extract students
 * @author Emanuele Musto
 */
public class StudentsBag implements Serializable {
    
    private final List<ColoredPawn> students;
    
    /**
     * Initializes the bag with an empty ArrayList
     */
    public StudentsBag() {
        
        students = new ArrayList<>();
    }
    
    /**
     * Checks whether the bag is empty
     * @return True if empty, false otherwise
     */
    public boolean isEmpty() {
        
        return (students.isEmpty());
    }
    
    /**
     * Adds a student to the bag
     * @param student the student to add
     * @throws RuntimeException if the student is already in the bag
     */
    public void addStudent(ColoredPawn student) {
        
        assert student != null;
        if (students.contains(student)) throw new RuntimeException("Student is already in the bag");
        students.add(student);
    }
    
    /**
     * Extracts a random student from the bag
     * @return the student extracted
     * @throws RuntimeException if the bag is empty
     */
    public ColoredPawn extractRandomStudent() {
        
        if (students.isEmpty()) throw new RuntimeException("Bag is empty");
        int extractionIndex = (int) (Math.random() * (students.size()));
        return (students.remove(extractionIndex));
    }
    
    public int getNumberOfStudents() {
        
        return this.students.size();
    }
}
