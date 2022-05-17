package it.polimi.ingsw2022.eriantys.server.model.board;

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
        for (int n = 0; n < 12; n ++) islands.add(new CompoundIslandTile());
        clouds = new ArrayList<>(players.size());
        for (int n = 0; n < players.size(); n++) clouds.add(new CloudTile());
        schools = new ArrayList<>(players.size());
        for (int n = 0; n < players.size(); n++) {

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
        islands.get(0).setMotherNature(true);
    }

    /**
     * Merges the given islands into a single bigger island. The given islands must be adjacent
     * @param index1 the index of the first island to merge
     * @param index2 the index of the second island to merge
     * @throws RuntimeException if the given islands are the same island
     * @throws RuntimeException if the given islands are not adjacent
     * @throws RuntimeException if any island tile is part of both the islands
     */
    public void mergeIslands(int index1, int index2) {
        if (index1 > islands.size() - 1 || index2 > islands.size() - 1) throw new RuntimeException("Index are out of bounds");
        if (index1 == index2) throw new RuntimeException("Cannot merge an island with itself");
        if (Math.abs(index1 - index2) != 1 && Math.abs(index1 - index2) != islands.size() - 1) throw new RuntimeException("Cannot merge not adjacent islands");
        CompoundIslandTile island1 = getIsland(Math.min(index1, index2));
        CompoundIslandTile island2 = getIsland(Math.max(index1, index2));
        island1.mergeWithIsland(island2);
        islands.remove(island2);
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

    public CompoundIslandTile getIsland(int index) {

        return islands.get(index);
    }

    public CloudTile getCloud(int index) {

        return clouds.get(index);
    }
}
