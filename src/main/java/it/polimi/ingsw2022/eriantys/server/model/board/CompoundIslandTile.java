package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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
    private int index;

    /**
     * Constructs an island made of a single island tile
     */
    public CompoundIslandTile(int index) {

        tiles = new ArrayList<>();
        tiles.add(new IslandTile());
        team = null;
        setIndex(index);
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

        int denyTiles = 0;
        ArrayList<ColoredPawn> students = new ArrayList<>();
        for(IslandTile tile : tiles) {

            students.addAll(tile.removeAllStudents());
            denyTiles += tile.getNumberOfDenyTiles();
            tile.setNumberOfDenyTiles(0);
        }
        for(IslandTile tile : island.tiles) {

            if (tiles.contains(tile)) throw new RuntimeException("No duplicates allowed");
            students.addAll(tile.removeAllStudents());
            denyTiles += tile.getNumberOfDenyTiles();
            tile.setNumberOfDenyTiles(0);
            tiles.add(tile);
        }
        tiles.get(0).setNumberOfDenyTiles(denyTiles);
        tiles.get(0).addStudents(students);
    }

    /**
     * @return the number of students placed on this island
     */
    public int countStudents() {

        return tiles.get(0).countStudents();
    }

    /**
     * @param color the color of students to count
     * @return the number of students of the given color placed on this island
     */
    public int countStudents(PawnColor color) {

        return tiles.get(0).countStudents(color);
    }

    /**
     * Places a colored pawn onto this island. Only pawns that represent students should be placed on an island
     * @param student the student to place
     * @throws RuntimeException if the given student is already on this island
     */
    public void addStudent(ColoredPawn student) {

        IslandTile tile = tiles.get(0);
        if (tile.containsStudent(student)) throw new RuntimeException("No duplicates allowed");
        tile.addStudent(student);
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
      
        if (getNumberOfDenyTiles() > 0) throw new RuntimeException("Cannot set a new controller team on a denied island");
        if (team == null) throw new InvalidParameterException("Team cannot be null");

        this.team = team;
        for (IslandTile tile : tiles) tile.setTeam(team);
    }

    /**
     * @return whether mother nature is currently on this island
     */
    public boolean hasMotherNature() {

        return tiles.stream().anyMatch(IslandTile::hasMotherNature);
    }

    /**
     * Sets whether mother nature is currently on this island and places/removes it on/from the first tile of this island
     * @param motherNature true to place mother nature on this island, false to remove it
     */
    void setMotherNature(boolean motherNature) {

        if(motherNature) tiles.get(0).setMotherNature(true);
        else for(IslandTile tile : tiles) tile.setMotherNature(false);
    }

    public int getNumberOfDenyTiles() {

        return tiles.get(0).getNumberOfDenyTiles();
    }

    /**
     * Adds deny tile to the island. A denied island cannot change its controller team.
     * @throws RuntimeException if someone tries to put more than four deny tiles on the island.
     */
    public void incrementNumberOfDenyTiles() {

        IslandTile tile = tiles.get(0);
        if(tile.getNumberOfDenyTiles() >= 4) throw new RuntimeException("Cannot increment deny tiles");
        tile.setNumberOfDenyTiles(tile.getNumberOfDenyTiles() + 1);
    }

    /**
     * Removes deny tile to the island.
     * @throws RuntimeException if someone tries to remove deny tile when there are none on the island.
     */
    public void decrementNumberOfDenyTiles() {

        IslandTile tile = tiles.get(0);
        if(tile.getNumberOfDenyTiles() <= 0) throw new RuntimeException("Cannot decrement deny tiles");
        tile.setNumberOfDenyTiles(tile.getNumberOfDenyTiles() - 1);
    }

    /**
     * @return the number of towers on this island
     */
    public int countTowers() {

        return tiles.size() == 1 ? (tiles.get(0).hasTower() ? 1 : 0) : tiles.size();
    }

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {

        this.index = index;
        for(IslandTile tile : tiles) tile.setIndex(index);
    }
}
