package it.polimi.ingsw2022.eriantys.model.influence;

import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.model.players.Player;
import it.polimi.ingsw2022.eriantys.model.players.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class calculates the dominance for a compound island
 * without considering skills from characters
 * @author Emanuele Musto
 */
public class InfluenceCalculatorBasic implements InfluenceCalculator{

    protected final Map<Player, Integer> playersInfluence;
    /**
     * Initializes the HashMap
     */
    public InfluenceCalculatorBasic(){
        playersInfluence = new HashMap<>();
    }

    /**
     * Calculates influence for all players on a given compound island
     * @param players the list of players in the game
     * @param island the compound island for which computing domination
     * @param currentPlayer the active player in this turn
     * @return the dominating team if present, an empty optional otherwise
     * @throws RuntimeException when the array of players is empty
     */
    @Override
    public Optional<Team> calculateInfluence(ArrayList<Player> players, CompoundIslandTile island, Player currentPlayer){

        if(players.isEmpty()) throw new RuntimeException("Not enough players for computing domination");
        fillMap(players, currentPlayer);

        for(Player player : players){
            towerInfluence(player, island);
            studentsInfluence(player, island, currentPlayer);
        }

        return(getDominator() );
    }


    /**
     * Adds the towers' influence if they are on the island
     * @param player one of the players
     * @param island the given compound island
     */
    protected void towerInfluence(Player player, CompoundIslandTile island) {

        if (island.getTeam().isPresent() && player.team.equals(island.getTeam().get())){
            playersInfluence.put(player, playersInfluence.get(player) + island.countTowers() );
        }
    }

    /**
     * Adds the students' influence
     * @param player one of the players
     * @param island the given compound island
     */
    protected void studentsInfluence(Player player, CompoundIslandTile island, Player currentPlayer){

        for( PawnColor colors : PawnColor.values()){
            if(player.getSchool().containsProfessor(colors)){
                playersInfluence.put(player, playersInfluence.get(player) + island.countStudents(colors) );
            }
        }
    }

    /**
     * Initializes the key as the players, and the influence as 0
     * @param players the list of players
     * @param currentPlayer the acting player in this turn
     */
    protected void fillMap(ArrayList<Player> players, Player currentPlayer){
        for(Player player : players){
            playersInfluence.put(player,0);
        }
    }

    /**
     * Calculates the player with the most influence
     * @return the Team that has the player with the most influence if there is not a tie. If there is a tie,
     * returns an empty Optional
     */
    protected Optional<Team> getDominator(){

        Integer max=0;
        Optional<Team> dominator = Optional.empty();
        boolean tie = false;

        for( Player player : playersInfluence.keySet() ){
            if(playersInfluence.get(player).equals(max)) tie = true;

            if(playersInfluence.get(player) > max){
                max = playersInfluence.get(player);
                dominator = Optional.of(player.team);
                tie = false;
            }
        }

        if(tie) return(Optional.empty());
        else return (dominator);
    }
}
