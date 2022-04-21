package it.polimi.ingsw2022.eriantys.model.players;

import java.util.ArrayList;
import java.util.List;

/**
 * This enumeration represents the teams of the game. Every team should contain from 0 to 2 players
 * @author Emanuele Musto
 */
public enum Team {

    WHITE,
    BLACK,
    GRAY;

    private final List<Player> players;

    /**
     * Initializes the list of player with initial capacity equal to 2
     */
    Team() {

        players = new ArrayList<>(2);
    }

    /**
     * Adds a player to the team
     * @param player the player to add to the team
     * @throws RuntimeException if the team already contains 2 players
     * @throws RuntimeException if the player is already part of the team
     */
    public void addPlayer(Player player) {

        if (players.size() >= 2) throw new RuntimeException("Team already reached its max size: max 2 players per team");
        if (players.contains(player)) throw new RuntimeException("Player already part of this team");
        players.add(player);
    }

    /**
     * @return the number of players of the team
     */
    public int getSize() {

        return players.size();
    }

    /**
     * Resets this team to an empty team (only for test purposes)
     */
    void reset() {

        players.clear();
    }
}
