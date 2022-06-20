package it.polimi.ingsw2022.eriantys.server.model.players;

import it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This enumeration represents the teams of the game. Every team should contain from 0 to 2 players
 * @author Emanuele Musto
 */
public class Team implements Serializable {

    public final String ansiColor;
    private final List<Player> players;
    private final String teamName;

    /**
     * Initializes the list of player with initial capacity equal to 2
     * @param ansiColor the ansi color of this team to use in the cli
     */
    public Team(String ansiColor, String teamName) {

        this.ansiColor = ansiColor;
        players = new ArrayList<>(2);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
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

    public Player getLeader() {

        for (Player player : players)
            if (player.isTeamLeader) return player;
        throw new RuntimeException("No team leader found");
    }

    /**
     * Get the list of players of the team
     * @return Array of players
     */
    public List<Player> getPlayers() {
        return players;
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
    public void reset() {
        players.clear();
    }
}
