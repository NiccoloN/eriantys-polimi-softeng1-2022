package it.polimi.ingsw2022.eriantys.server.model.cards;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a character card
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class CharacterCard extends Card {
    
    public final String effect;
    private final int initialCost;
    private final List<ColoredPawn> students;
    private int cost;
    private boolean costIncremented = false;
    private int denyTilesNumber;
    
    CharacterCard(int index, String effect, int cost) {
        
        super(index);
        this.effect = effect;
        this.cost = cost;
        initialCost = cost;
        
        students = new ArrayList<>(4);
        denyTilesNumber = -1;
    }
    
    public int getCost() {
        
        return cost;
    }
    
    /**
     * Increments the cost of the character card if it's the first time being used.
     */
    public void incrementCost() {
        
        if (initialCost == cost) {
            
            this.cost = this.cost + 1;
            costIncremented = true;
        }
    }
    
    public ColoredPawn getStudent(PawnColor color) {
        
        return students.stream().filter((x) -> x.color == color).findAny().orElseThrow();
    }
    
    /**
     * Adds a student to the character card.
     * @param student the student to add.
     */
    public void addStudent(ColoredPawn student) {
        
        students.add(student);
    }
    
    /**
     * Removes a student from the character card.
     * @param student the student to remove from the character card.
     */
    public void removeStudent(ColoredPawn student) {
        
        students.remove(student);
    }
    
    /**
     * @param color the color of students to count
     * @return the number of students of the given color currently on this card
     */
    public int countStudents(PawnColor color) {
        
        return (int) students.stream().filter((x) -> x.color == color).count();
    }
    
    /**
     * @return a list of all the students' colors present on the character card.
     */
    public List<PawnColor> getStudentsColors() {
        
        ArrayList<PawnColor> availableColors = new ArrayList<>();
        for (PawnColor color : PawnColor.values())
            if (students.stream().anyMatch((x) -> x.color == color)) availableColors.add(color);
        
        return availableColors;
    }
    
    public int getDenyTilesNumber() {
        
        return denyTilesNumber;
    }
    
    /**
     * Decrements by one the number on deny tiles on the character card if possible.
     * (Not possible when there are none already)
     */
    public void decrementDenyTiles() {
        
        if (denyTilesNumber == 0) throw new RuntimeException("Number of deny tiles can't be negative");
        this.denyTilesNumber--;
    }
    
    /**
     * Increments by one the number of deny tiles on the character card if possible.
     * (Not possible when there are already four, the maximum number of deny tiles)
     */
    public void incrementDenyTiles() {
        
        if (denyTilesNumber == 4) throw new RuntimeException("Number of deny tiles can't be greater than 4");
        this.denyTilesNumber++;
    }
    
    public boolean isCostIncremented() {
        
        return costIncremented;
    }
}
