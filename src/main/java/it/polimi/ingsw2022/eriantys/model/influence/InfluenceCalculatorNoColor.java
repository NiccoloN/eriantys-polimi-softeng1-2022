package it.polimi.ingsw2022.eriantys.model.influence;

import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.model.players.Player;


/**
 * This class calculates the dominance without considering one color
 * @author Emanuele Musto
 */
public class InfluenceCalculatorNoColor extends InfluenceCalculatorBasic{


    PawnColor ignoredColor;

    /**
     * Initializes color as the color to ignore
     * @param color the color to ignore
     */
    public InfluenceCalculatorNoColor(PawnColor color){
        super();
        ignoredColor = color;
    }

    /**
     * Calculates the influence without considering the chosen color
     * @param player one of the players
     * @param island the given compound island
     */

    @Override
    protected void studentsInfluence(Player player, CompoundIslandTile island, Player currentPlayer){

        for( PawnColor colors : PawnColor.values()){
            if( (!ignoredColor.equals(colors)) && (player.getSchool().containsProfessor(colors)) ){
                playersInfluence.put(player, playersInfluence.get(player) + island.countStudents(colors) );
            }
        }
    }
}
