package it.polimi.ingsw2022.eriantys.model;

import java.util.ArrayList;

/**
 * This class represents a player of the game, and has methods to manage cards and coins.
 * @author Emanuele Musto
 */

public class Player {

    private int coins;
    private HelperCard currentHelper;
    private final List<HelperCard> playerHelperCards;
    private SchoolDashboard schooldashboard;
    public final Team team;
    public final Mage mage;
    public final boolean isTeamLeader;

    /**
     * This methods initializes the player giving him a team, a mage, one coins, and the isTeamLeader attribute.
     * @param team the team of the player
     * @param mage the mage chosen by the player
     */

    public Player(Team team, Mage mage){

        coins = 1;
        this.mage = mage;
        this.team = team;
        team.addPlayer(this);
        playerHelperCards = new ArrayList<>(10);
        if(team.getSize() == 0) isTeamLeader = true;
        else { isTeamLeader = false; }
    }

    /**
     * This method adds one HelperCard to the list of helper cards owned by the player,
     * ordered by the index of the card.
     * @param cardToAdd the HelperCard to add.
     * @throws RuntimeException when the HelperCard is already assigned to the player.
     */

    public void addHelperCard(HelperCard cardToAdd){

        if(playerHelperCards.contains(cardToAdd)){ throw new RuntimeException("HelperCard already assigned to player."); }
        playerHelperCards.add(cardToAdd.index, cardToAdd);
    }

    /**
     * This method moves a HelperCard in currentHelper, in order to represent the card chosen by the player.
     * @param index the index of the card, and his position on the list of HelperCards.
     * @throws IndexOutOfBoundsException when index is out of bound.
     */

    public void playHelper(int index){
        currentHelper = playerHelperCards.remove(index);
    }

    public HelperCard getCurrentHelper() {
        return currentHelper;
    }

    /**
     * @return the helper card with the wanted index.
     * @throws IndexOutOfBoundsException when index is out of bound.
     */

    public HelperCard getHelperCard(int index){
        return(playerHelperCards.get(index));
    }

    /**
     * @return the number of HelperCards left for the player.
     */
    public int getNumberOfHelpers(){
        return(playerHelperCards.size);
    }

    /**
     * This method adds a coin to the player.
     */
    public void addCoin(){
        coins++;
    }

    /**
     * This method updates the amount of coin when the player uses a CharacterCard.
     * @param fee the amount of coins needed to play a CharacterCard.
     * @throws RuntimeException when the player has not enough coins.
     */
    public void payCoins(int fee) {
        if ((coins - fee) < 0) { throw new RuntimeException("Player has not enough coins."); }
        coins = coins - fee;
    }
}