package it.polimi.ingsw2022.eriantys.server.model.board;

import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.util.*;

/**
 * This class represents the game board: the set of islands, clouds and schools used to play the game
 * @author Niccolò Nicolosi
 * @see CompoundIslandTile
 * @see CloudTile
 * @see SchoolDashboard
 */
public class Board {

    private final List<CompoundIslandTile> islands;
    private final List<IslandTile> islandTiles;
    private final List<CloudTile> clouds;
    private final List<SchoolDashboard> schools;
    private int motherNatureIslandIndex;

    /**
     * Constructs a board for the given players
     * @param players the list of players in the game
     * @throws RuntimeException if the number of given players is not between 2 and 4
     */
    public Board(List<Player> players) {

        if (players.size() < 2) throw new RuntimeException("Not enough players");
        if (players.size() > 4) throw new RuntimeException("Too many players");

        islands = new ArrayList<>(12);
        for(int n = 0; n < 12; n ++) islands.add(new CompoundIslandTile(n));
        islandTiles = new ArrayList<>(12);
        for(int n = 0; n < 12; n++) islandTiles.add(islands.get(n).getTile(0));
        clouds = new ArrayList<>(players.size());
        for(int n = 0; n < players.size(); n++) clouds.add(new CloudTile());
        schools = new ArrayList<>(players.size());
        for(int n = 0; n < players.size(); n++) {

            Player player = players.get(n);

            int maxTowers = 0;

            switch(players.size()) {

                case 2:
                    maxTowers =  8;
                    break;
                case 3:
                    maxTowers = 6;
                    break;
                case 4:
                    maxTowers = player.isTeamLeader ? 8 : 0;
                    break;
            }

            SchoolDashboard school = new SchoolDashboard(player, maxTowers);
            player.setSchool(school);
            schools.add(school);
        }

        motherNatureIslandIndex = 0;
        islands.get(motherNatureIslandIndex).setMotherNature(true);
    }

    /**
     * Merges the given islands into a single bigger island. The given islands must be adjacent
     * @param index1 the index of the first island to merge
     * @param index2 the index of the second island to merge
     * @throws IndexOutOfBoundsException if either index1 or index2 is out of bounds
     * @throws RuntimeException if the given islands are the same island
     * @throws RuntimeException if the given islands are not adjacent
     * @throws RuntimeException if any island tile is part of both the islands
     */
    public void mergeIslands(int index1, int index2) {

        if (index1 == index2) throw new RuntimeException("Cannot merge an island with itself");
        if (Math.abs(index1 - index2) != 1 && Math.abs(index1 - index2) != islands.size() - 1) throw new RuntimeException("Cannot merge not adjacent islands");

        int resultingIndex = index1;
        int index1Previous = index1 - 1;
        if(index1Previous < 0) index1Previous = islands.size() - 1;
        if(index1Previous % islands.size() == index2) resultingIndex = index2;

        CompoundIslandTile island1 = getIsland(resultingIndex);
        CompoundIslandTile island2 = getIsland(resultingIndex == index1 ? index2 : index1);
        island1.mergeWithIsland(island2);
        islands.remove(island2);

        for(int n = 0; n < islands.size(); n++) islands.get(n).setIndex(n);

        motherNatureIslandIndex = island1.getIndex();
    }

    /**
     * Moves mother nature of the given steps. Every step corresponds to moving from an island to its adjacent
     * @param steps the number of steps to perform
     * @return the index of the island where mother nature is located after the move
     */
    public int moveMotherNature(int steps) {

        islands.get(motherNatureIslandIndex).setMotherNature(false);
        motherNatureIslandIndex += steps;
        motherNatureIslandIndex %= islands.size();
        islands.get(motherNatureIslandIndex).setMotherNature(true);
        return motherNatureIslandIndex;
    }

    /**
     * @return the island where mother nature is
     */
    public CompoundIslandTile getMotherNatureIsland() {

        return getIsland(motherNatureIslandIndex);
    }

    /**
     * @return the number of islands at the current state of the game
     */
    public int getNumberOfIslands() {

        return islands.size();
    }

    /**
     * @param index the index of the island (1 <= index <= number of islands)
     * @return the island of the given index
     * @throws IndexOutOfBoundsException if this board contains no island of the given index
     */
    public CompoundIslandTile getIsland(int index) {

        return islands.get(index);
    }

    public List<CompoundIslandTile> getIslands() {

        return new ArrayList<>(islands);
    }

    public List<IslandTile> getIslandTiles() {

        return new ArrayList<>(islandTiles);
    }

    /**
     * @param index the index of the cloud (1 <= index <= number of clouds)
     * @return the cloud of the given index
     * @throws IndexOutOfBoundsException if this board contains no cloud of the given index
     */
    public CloudTile getCloud(int index) {

        return clouds.get(index);
    }
}
