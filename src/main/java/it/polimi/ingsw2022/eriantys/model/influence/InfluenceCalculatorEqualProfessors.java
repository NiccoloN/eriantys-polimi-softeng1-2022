package it.polimi.ingsw2022.eriantys.model.influence;

import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.model.players.Player;
import it.polimi.ingsw2022.eriantys.model.players.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents the skill that gives temporary access to professors even if the number of students
 * in the school dashboard is the same as that of the actual professors' holder
 * @author Emanuele Musto
 */
public class InfluenceCalculatorEqualProfessors extends InfluenceCalculatorBasic{

    private final List<PawnColor> tempProfessors = new ArrayList<>();

    /**
     *
     * @param players the list of players in the game
     * @param island the compound island for which computing domination
     * @param currentPlayer the active player in this turn
     * @return the dominating team if present, an empty optional otherwise
     * @throws RuntimeException when the array of players is empty
     */
    @Override
    public Optional<Team> calculateInfluence(ArrayList<Player> players, CompoundIslandTile island, Player currentPlayer){
        if(players.isEmpty()) throw new RuntimeException("Not enough players for computing domination");
        manageProfessors(players, currentPlayer);
        fillMap(players, currentPlayer);

        for(Player player : players){
            towerInfluence(player, island);
            studentsInfluence(player, island, currentPlayer);
        }

        return(getDominator() );
    }

    /**
     * Adds the students' influence considering the temporary professors
     * @param player one of the players
     * @param island the given compound island
     * @param currentPlayer the player in this turn for whom the skill is active
     */
    @Override
    protected void studentsInfluence(Player player, CompoundIslandTile island, Player currentPlayer){

        for( PawnColor colors : PawnColor.values()){
            if(player.equals(currentPlayer)){
                if(player.getSchool().containsProfessor(colors) || tempProfessors.contains(colors)){
                    playersInfluence.put(player, playersInfluence.get(player) + island.countStudents(colors) );
                }
            }
            else{
                if(player.getSchool().containsProfessor(colors) && !tempProfessors.contains(colors)){
                    playersInfluence.put(player, playersInfluence.get(player) + island.countStudents(colors) );
                }
            }
        }
    }

    /**
     * Adds the temporary professors to the active player
     * @param players the list of players
     * @param currentPlayer the player that benefits the skill
     */
    private void manageProfessors(ArrayList<Player> players, Player currentPlayer){
        for(Player player : players){
            if(!player.equals(currentPlayer)){
                for(PawnColor professor : PawnColor.values() ){
                    if(player.getSchool().containsProfessor(professor) &&
                      (currentPlayer.getSchool().countStudents(professor) == player.getSchool().countStudents(professor))){
                        tempProfessors.add(professor);
                    }
                }
            }
        }
    }
}
