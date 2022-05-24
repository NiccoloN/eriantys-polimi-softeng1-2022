package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This class represents an island. Every island is composed of different island tiles (at least one).
 * @author Niccolò Nicolosi
 * @see IslandTile
 */
public class CompoundIslandTile implements Serializable {

    private final List<IslandTile> tiles;
    private Team team;
    private boolean motherNature;
    private boolean denied;

    /**
     * Constructs an island made of a single island tile
     */
    public CompoundIslandTile() {

        tiles = new ArrayList<>();
        tiles.add(new IslandTile());
        team = null;
        motherNature = false;
        denied = false;
    }

    /**
     * @return the number of island tiles this island is made of
     */
    public int getNumberOfTiles() {

        return tiles.size();
    }

    public IslandTile getTile(int index) {

        return tiles.get(index);
    }

    /**
     * Merges this island with the given one, making it bigger.
     * @param island the island to merge with
     * @throws RuntimeException if the given island contains any tile that is already part of this island
     */
    public void mergeWithIsland(CompoundIslandTile island) {
        for (IslandTile tile : island.tiles) {
            if (tiles.contains(tile)) throw new RuntimeException("No duplicates allowed");
        }
        tiles.addAll(island.tiles);
    }

    /**
     * @return the number of students placed on this island
     */
    public int countStudents() {

        int count = 0;
        for (IslandTile tile : tiles) count += tile.countStudents();
        return count;
    }

    /**
     * @param color the color of students to count
     * @return the number of students of the given color placed on this island
     */
    public int countStudents(PawnColor color) {

        int count = 0;
        for (IslandTile tile : tiles) count += tile.countStudents(color);
        return count;
    }

    /**
     * Places a colored pawn onto this island. Only pawns that represent students should be placed on an island
     * @param student the student to place
     * @throws RuntimeException if the given student is already on this island
     */
    public void addStudent(ColoredPawn student) {

        for (IslandTile tile : tiles) if (tile.containsStudent(student)) throw new RuntimeException("No duplicates allowed");
        tiles.get(tiles.size() - 1).addStudent(student);
        distributeStudents();
    }

    /**
     * Distributes the students placed on this island equally among all of its tiles
     */
    private void distributeStudents() {
        
        int studentsNum = countStudents();
        int studentsPerTile = studentsNum / tiles.size();
        int carry = studentsNum - studentsPerTile;

        int n = 0;
        
        for (IslandTile overcrowdedTile : tiles) {
            
            if (carry > 0 && overcrowdedTile.countStudents() == studentsPerTile + 1) carry--;
            else while (overcrowdedTile.countStudents() > studentsPerTile) {
                
                while (n < tiles.size()) {

                    IslandTile needingTile = tiles.get(n);
                    if (needingTile.countStudents() < studentsPerTile) {

                        needingTile.addStudent(overcrowdedTile.removeStudent());
                        break;
                    }
                    n++;
                }
            }
        }
    }

    /**
     * @return the team that currently controls this island
     */
    public Optional<Team> getTeam() {

        return team != null ? Optional.of(team) : Optional.empty();
    }

    /**
     * Sets the given team as the controller of this island
     * @param team the new team that controls this island
     * @throws RuntimeException if this island is denied
     * @throws InvalidParameterException if team is null
     */
    public void setTeam(Team team) {
        if (denied) throw new RuntimeException("Cannot set a new controller team on a denied island");
        if (team == null) throw new InvalidParameterException("Team cannot be null");
        this.team = team;
        for (IslandTile tile : tiles) {
            tile.addTower();
        }
    }

    /**
     * @return whether mother nature is currently on this island
     */
    public boolean hasMotherNature() {

        return motherNature;
    }

    /**
     * Sets whether mother nature is currently on this island and places/removes it on/from the first tile of this island
     * @param motherNature true to place mother nature on this island, false to remove it
     */
    void setMotherNature(boolean motherNature) {

        this.motherNature = motherNature;
        tiles.get(0).setMotherNature(motherNature);
    }

    public boolean isDenied() {

        return denied;
    }

    /**
     * Sets whether this island is denied. A denied island cannot change its controller team
     * @param denied true to deny this island, false to make it not-denied
     */
    public void setDenied(boolean denied) {

        this.denied = denied;
    }

    /**
     * @return the number of towers on this island
     */
    public int countTowers() {

        return tiles.size() == 1 ? (tiles.get(0).hasTower() ? 1 : 0) : tiles.size();
    }
}
