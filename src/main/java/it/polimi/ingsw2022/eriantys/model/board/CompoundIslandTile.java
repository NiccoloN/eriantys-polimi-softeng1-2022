package it.polimi.ingsw2022.eriantys.model.board;

import it.polimi.ingsw2022.eriantys.model.ColoredPawn;
import it.polimi.ingsw2022.eriantys.model.PawnColor;
import it.polimi.ingsw2022.eriantys.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Niccolò Nicolosi
 * This class represents an island. Every island is composed of different island tiles (at least one).
 * @see IslandTile
 * Colored pawns can be placed on an island. However, only pawns that represent students should be placed on it.
 * @see ColoredPawn
 * An island can be controlled by a team.
 * @see Team
 */
public class CompoundIslandTile {

    private final List<IslandTile> tiles;
    private Team team;
    private boolean denied;

    public CompoundIslandTile() {

        tiles = new ArrayList<>();
        tiles.add(new IslandTile());
    }

    /**
     * @return an array containing the individual tiles that form this island
     */
    public IslandTile[] getTiles() {

        IslandTile[] tiles = new IslandTile[this.tiles.size()];
        this.tiles.toArray(tiles);
        return tiles;
    }

    /**
     * Adds a tile to this island, making it bigger.
     * @param tile the tile to add
     */
    public void addTile(CompoundIslandTile tile) {

        tiles.addAll(tile.tiles);
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
     * Places a student onto this island
     * @param student the student to place
     */
    public void addStudent(ColoredPawn student) {

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
        
        for (IslandTile overcrowdedTile : tiles) {
            
            if (carry > 0 && overcrowdedTile.countStudents() == studentsPerTile + 1) carry--;
            else while (overcrowdedTile.countStudents() > studentsPerTile) {
                
                for (IslandTile needingTile : tiles)
                    if (needingTile.countStudents() < studentsPerTile) {

                        needingTile.addStudent(overcrowdedTile.removeStudent());
                        break;
                    }
            }
        }
    }

    /**
     * @return the team that currently controls this island
     */
    public Optional<Team> getTeam() {

        return Optional.of(team);
    }

    /**
     * Sets the given team as the controller of this island
     * @param team the new team that controls this island
     * @throws RuntimeException if this island is denied
     */
    public void setTeam(Team team) {

        if (denied) throw new RuntimeException("Cannot set a new controller team on a denied island");
        this.team = team;
    }

    public boolean isDenied() {

        return denied;
    }

    /**
     * Denies this island: a denied island cannot change its controller team
     */
    public void deny() {

        denied = true;
    }

    /**
     * Un-denies this island: after the call of this method, a new team can control this island
     */
    public void undeny() {

        denied = false;
    }

    /**
     * @return the number of towers on this island
     */
    public int countTowers() {

        return tiles.size();
    }
}
