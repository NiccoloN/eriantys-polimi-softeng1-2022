package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This class represents an island tile: the smallest piece that forms an island.
 * Island tiles can be aggregated to form a bigger island.
 *
 * @author Niccolò Nicolosi
 * @see CompoundIslandTile
 */
public class IslandTile implements Serializable {
    
    private final List<ColoredPawn> students;
    private boolean motherNature;
    private int numberOfDenyTiles;
    private boolean tower;
    private Team team;
    private int index;
    
    IslandTile() {
        
        students = new ArrayList<>();
        motherNature = false;
        numberOfDenyTiles = 0;
        tower = false;
        team = null;
        index = 0;
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
     * @return whether mother nature is currently on this specific tile of an island
     */
    public boolean hasMotherNature() {
        
        return motherNature;
    }
    
    /**
     * @return whether a tower has been placed on this tile
     */
    public boolean hasTower() {
        
        return tower;
    }
    
    public Optional<Team> getTeam() {
        
        if (team != null) return Optional.of(team);
        return Optional.empty();
    }
    
    public void setTeam(Team team) {
        
        this.team = team;
        tower = true;
    }
    
    public int getIndex() {
        
        return index;
    }
    
    public void setIndex(int index) {
        
        this.index = index;
    }
    
    public int getNumberOfDenyTiles() {
        
        return numberOfDenyTiles;
    }
    
    public void setNumberOfDenyTiles(int numberOfDenyTiles) {
        
        this.numberOfDenyTiles = numberOfDenyTiles;
    }
    
    /**
     * Places the given colored pawns onto this tile. Only pawns that represent students should be placed on an island tile
     *
     * @param students the list of students to place
     * @throws RuntimeException if any of the given students is already placed on this tile
     */
    void addStudents(Collection<ColoredPawn> students) {
        
        for (ColoredPawn student : students) addStudent(student);
    }
    
    /**
     * Places a colored pawn onto this tile. Only pawns that represent students should be placed on an island tile
     *
     * @param student the student to place
     * @throws RuntimeException if the given student is already placed on this tile
     */
    void addStudent(ColoredPawn student) {
        
        if (students.contains(student)) throw new RuntimeException("No duplicates allowed");
        students.add(student);
    }
    
    /**
     * Removes all the students from this tile
     *
     * @return a list containing the removed students
     */
    List<ColoredPawn> removeAllStudents() {
        
        List<ColoredPawn> removed = new ArrayList<>(students);
        students.clear();
        return removed;
    }
    
    /**
     * Sets whether mother nature is currently on this specific tile of an island
     *
     * @param motherNature true to place mother nature on this tile, false to remove it
     */
    void setMotherNature(boolean motherNature) {
        
        this.motherNature = motherNature;
    }
}
