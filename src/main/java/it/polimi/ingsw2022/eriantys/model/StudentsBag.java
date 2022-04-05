package it.polimi.ingsw2022.eriantys.model;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the student bag. This makes possible to store, add, and extract randomly
 * the students contained
 * @author Emanuele Musto
 */

public class StudentsBag {

    private final List<ColoredPawn> students;

    /**
     * This method initializes the bag as an ArrayList.
     */

    public StudentsBag(){
        students = new ArrayList<>();
    }

    /**
     * This method checks whether the StudentsBag is empty.
     * @return True if empty, false otherwise.
     */

    public boolean isEmpty(){
        return(students.isEmpty());
    }

    /**
     * This method adds a student to the StudentsBag.
     * @param studentToAdd the student to add to the bag.
     * @throws RuntimeException if the student is already in the StudentsBag.
     */

    public void addStudent(ColoredPawn studentToAdd){

        if(students.contains(studentToAdd)){ throw new RuntimeException("Student already in StudentBag"); }

        students.add(studentToAdd);
    }

    /**
     * This method generates randomly ad index, used to extract a student from the bag.
     * @return the student extracted randomly.
     * @throws RuntimeException if the StudentsBag is empty.
     */

    public ColoredPawn extractRandomStudent() {

        if (students.isEmpty()) { throw new RuntimeException("StudentBag is empty."); }
        int ExtractionIndex = (int) (Math.random() * (students.size()));
        return (students.remove(ExtractionIndex));

    }
}