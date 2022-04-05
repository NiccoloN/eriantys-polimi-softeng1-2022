package it.polimi.ingsw2022.eriantys.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Team, with the associated color, and contains the player (or players) of the team
 * @author Emanuele Musto
 */

public enum Team {

    WHITE,
    BLACK,
    GRAY;

    private final List<Player> players;


    /**
     * This method initializes the list of player with initial size equal to 2.
     */

    Team() {

        players = new ArrayList<>(2);
    }


    /**
     *This method adds a player to the team.
     * @param playerToAdd the player to add to the team.
     * @throws RuntimeException when the player is already part of the team.
     */

    public void addPlayer(Player playerToAdd) {

        if(players.contains(playerToAdd)) { throw new RuntimeException("Player already on this team."); }
        players.add(playerToAdd);
    }

    /**
     * @return the number of players of the team.
     */
    public int getSize() {

        return players.size();
    }
}
