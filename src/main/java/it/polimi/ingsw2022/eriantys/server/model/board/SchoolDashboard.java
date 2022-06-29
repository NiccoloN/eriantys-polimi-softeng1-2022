package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a school. Every school is associated to a player
 * @author Niccolò Nicolosi
 * @see Player
 */
public class SchoolDashboard implements Serializable {
    
    /**
     * The player associated to this school
     */
    public final Player player;
    
    private final List<ColoredPawn> entranceStudents;
    private final Map<PawnColor, List<ColoredPawn>> studentsTables;
    private final List<ColoredPawn> professors;
    private final int maxTowers;
    private int towers;
    
    /**
     * Constructs a school associated to the given player, with a maximum number of towers
     * @param player the player to associate this school with
     */
    public SchoolDashboard(Player player, int maxTowers) {
        
        if(maxTowers != 6 && maxTowers != 8) throw new RuntimeException("Number of maxTowers is wrong");
        
        this.player      = player;
        entranceStudents = new ArrayList<>();
        studentsTables   = new HashMap<>();
        for(PawnColor color : PawnColor.values()) studentsTables.put(color, new ArrayList<>());
        professors     = new ArrayList<>();
        this.maxTowers = maxTowers;
        towers         = maxTowers;
    }
    
    /**
     * @return students contained in entrance
     */
    public List<ColoredPawn> getEntranceStudents() {
        
        return new ArrayList<>(entranceStudents);
    }
    
    /**
     * @param color the color of students to count
     * @return the number of students of the given color currently placed at the entrance of this school
     */
    public int countEntranceStudents(PawnColor color) {
        
        return (int) entranceStudents.stream().filter((x) -> x.color == color).count();
    }
    
    /**
     * Place a colored pawn at the entrance of this school. Only pawns that represent students should be placed at the entrance
     * @param student the student to place
     * @throws RuntimeException if the entrance of this school already contains 7 or more students
     * @throws RuntimeException if the given student is already at the entrance of this school
     */
    public void addToEntrance(ColoredPawn student) {
        
        if(entranceStudents.size() >= 7) throw new RuntimeException("Maximum number of students at the entrance already reached");
        if(entranceStudents.contains(student)) throw new RuntimeException("No duplicates allowed");
        entranceStudents.add(student);
    }
    
    public List<PawnColor> getAvailableEntranceColors() {
        
        ArrayList<PawnColor> availableColors = new ArrayList<>();
        for(PawnColor color : PawnColor.values())
            if(entranceStudents.stream().anyMatch((x) -> x.color == color)) availableColors.add(color);
        
        return availableColors;
    }
    
    public List<PawnColor> getAvailableTableColors() {
        
        ArrayList<PawnColor> availableColors = new ArrayList<>();
        for(PawnColor color : PawnColor.values())
            if(countTableStudents(color) > 0) availableColors.add(color);
        
        return availableColors;
    }
    
    /**
     * @param color the color of students to count
     * @return the number of students of the given color currently at the table of the corresponding color
     */
    public int countTableStudents(PawnColor color) {
        
        return studentsTables.get(color).size();
    }
    
    /**
     * Removes the given student from the entrance of this school, if present
     * @param student the student to remove
     * @throws NoSuchElementException if the given student is not at the entrance of this school
     */
    public void removeFromEntrance(ColoredPawn student) {
        
        if(!entranceStudents.remove(student)) throw new NoSuchElementException();
    }
    
    /**
     * Removes from the school entrance a student pawn of the given color, if present.
     * @param color the given color.
     * @return the pawn removed from the school entrance.
     */
    public ColoredPawn removeFromEntrance(PawnColor color) {
        
        ColoredPawn student = entranceStudents.stream().filter((x) -> x.color == color).findFirst().orElseThrow();
        entranceStudents.remove(student);
        return student;
    }
    
    /**
     * Places a colored pawn at the table of the corresponding color. Only pawns that represent students should be placed with this method
     * @param student the student to place
     * @throws RuntimeException if the table of the corresponding color already contains 9 or more students
     * @throws RuntimeException if the given student is already at the table of its color
     */
    public void addToTable(ColoredPawn student) {
        
        List<ColoredPawn> table = studentsTables.get(student.color);
        if(table.size() >= 10) throw new RuntimeException("Maximum number of students at the " + student.color + " table already reached");
        if(table.contains(student)) throw new RuntimeException("No duplicates allowed");
        table.add(student);
        
        if(table.size() % 3 == 0) player.addCoin();
    }
    
    /**
     * Removes the last placed student from the table of the given color
     * @param color the color of the table from which to remove the student
     * @throws RuntimeException if the table of the given color is empty
     */
    public ColoredPawn removeFromTable(PawnColor color) {
        
        List<ColoredPawn> table = studentsTables.get(color);
        if(table.isEmpty()) throw new RuntimeException("Cannot remove from an empty table");
        return table.remove(table.size() - 1);
    }
    
    public int countProfessors() {
        
        return professors.size();
    }
    
    /**
     * @param color the color of the professor to check for
     * @return whether the professor of the given color is in this school
     */
    public boolean containsProfessor(PawnColor color) {
        
        return professors.stream().anyMatch((x) -> x.color == color);
    }
    
    /**
     * Place a colored pawn into this school. Only pawns that represent professors should be place with this method
     * If the professor is present, do nothing
     * @param professor the professor to place
     */
    public void addProfessor(ColoredPawn professor) {
        
        if(professors.contains(professor)) throw new RuntimeException("Professor already present");
        professors.add(professor);
    }
    
    /**
     * Removes the professor of the given color from this school
     * @param color the color of the professor to remove
     * @return Optional of the removed professor
     */
    public ColoredPawn removeProfessor(PawnColor color) {
        
        ColoredPawn professor = professors.stream().filter((x) -> x.color == color).findAny().orElseThrow();
        professors.remove(professor);
        return professor;
    }
    
    public int getTowers() {
        
        return towers;
    }
    
    /**
     * Places a tower in this school
     * @throws RuntimeException if the number of towers in this school is already at its maximum
     */
    public void addTower() {
        
        if(towers >= maxTowers) throw new RuntimeException("Cannot add anymore towers");
        towers++;
    }
    
    /**
     * Removes a tower from this school
     * @throws RuntimeException if the number of towers in this school is 0
     */
    public void removeTower() {
        
        if(towers <= 0) throw new RuntimeException("There are no towers in this school");
        towers--;
    }
}
