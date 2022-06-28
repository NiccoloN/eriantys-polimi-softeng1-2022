package it.polimi.ingsw2022.eriantys.server.model.players;

import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class represents a player of the game. Every player is part of a team
 * @author Emanuele Musto
 * @see Team
 */

public class Player implements Serializable {

    /**
     * The mage associated to the player
     */
    public final Mage mage;

    /**
     * The team of the player
     */
    public final Team team;

    /**
     * True if the player is the leader of his team
     */
    public final boolean isTeamLeader;

    private String username;
    private int coins;
    private HelperCard currentHelper;
    private HelperCard prevHelper;
    private final List<HelperCard> helperCards;
    private SchoolDashboard school;

    private boolean characterUsed = false;

    /**
     * Initializes the player adding it to a team and associating it to a mage
     * @param team the team of the player
     * @param mage the mage associated with this player
     */
    public Player(String username, Team team, Mage mage) {

        this.username = username;

        this.team = team;
        team.addPlayer(this);
        isTeamLeader = team.getSize() == 1;

        this.mage = mage;

        helperCards = new ArrayList<>(10);
        coins = 99;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Adds a helper card to the hand of the player
     * @param card the card to add
     * @throws RuntimeException if the player already has a card of the same index in his hand
     */
    public void addHelperCard(HelperCard card) {

        if (helperCards.stream().anyMatch((x) -> x.index == card.index))
            throw new RuntimeException("Helper card of the same index already assigned to player");
        helperCards.add(card);
    }

    /**
     * Plays a helper card. The played card will be removed from the hand of the player and stored in currentHelper
     * @param index the index of the card to play
     * @throws NoSuchElementException if player's hand does not contain a card of the given index
     * @return played card
     */
    public HelperCard playHelperCard(int index) {

        currentHelper = helperCards.stream().filter((x) -> x.index == index).findAny().orElseThrow();
        helperCards.remove(currentHelper);
        prevHelper = currentHelper;
        return currentHelper;
    }

    public HelperCard getHelperCard(int index) {

        return helperCards.get(index);
    }

    public List<HelperCard> getHelperCards() {
        return new ArrayList<>(helperCards);
    }

    /**
     * @param index the index of the character card.
     * @return true if the player has the character card with the given index, false otherwise.
     */
    public boolean hasHelper(int index) {

        return helperCards.stream().anyMatch((x) -> x.index == index);
    }

    /**
     * Resets the current helper, used at the beginning of a new round.
     */
    public void resetCurrentHelper() {

        currentHelper = null;
    }

    /**
     * @return the number of HelperCards left in player's hand
     */
    public int getNumberOfHelpers() {

        return helperCards.size();
    }

    public HelperCard getCurrentHelper() {

        return currentHelper;
    }

    public HelperCard getPrevHelper() {

        return prevHelper;
    }

    public int getCoins() {

        return coins;
    }

    /**
     * Adds a coin to the player.
     */
    public void addCoin() {

        coins++;
    }

    /**
     * Updates the amount of coins when the player pays a given fee
     * @param fee the amount of coins to pay
     * @throws RuntimeException if the player has not enough coins
     */
    public void payCoins(int fee) {

        if ((coins - fee) < 0) throw new RuntimeException("Player has not enough coins");
        coins = coins - fee;
    }

    /**
     * @return the school associated to the player
     */
    public SchoolDashboard getSchool() {

        return school;
    }

    /**
     * Sets the school associated to the player
     * @param school the school to associate to the player
     * @throws RuntimeException if the player is already associated to a school
     */
    public void setSchool(SchoolDashboard school) {

        if (this.school != null) throw new RuntimeException("Player already associated to a school");
        this.school = school;
    }

    /**
     * @return true if the player has already used a character card during this round.
     */
    public boolean hasPlayedCharacter() {
        return characterUsed;
    }

    public void setCharacterUsed(boolean characterUsed) {
        this.characterUsed = characterUsed;
    }

    /**
     * Compare priority of turn of this player with another player, based on the index of the current helper card.
     * @param otherPlayer the other player to compare to.
     * @return -1 if priority is lower, +1 if it's higher.
     */
    public int comparePriorityTo(Player otherPlayer) {

        return this.getCurrentHelper().priority < otherPlayer.getCurrentHelper().priority ? -1 : 1;
    }
}
