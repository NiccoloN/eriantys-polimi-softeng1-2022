package it.polimi.ingsw2022.eriantys.model;

import java.util.ArrayList;

public class StudentsBag {

    private final ArrayList<ColoredPawn> Students;

    public StudentsBag(){
        Students = new ArrayList<>();
    }

    public boolean isEmpty(){
        return(Students.isEmpty());
    }

    public void addStudent(ColoredPawn student){

        Students.add(student);
    }

    public ColoredPawn extractRandomStudent(){

        int ExtractionIndex = (int)(Math.random() * (Students.size()) );
        return(Students.remove(ExtractionIndex));
        }
}