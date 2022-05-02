package it.polimi.ingsw2022.eriantys.server.model.influence;

import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;


/**
 * This class is an influence calculator that calculates the dominance
 * without considering the contribution of students of a specific color
 * @author Emanuele Musto
 */
public class InfluenceCalculatorNoColor extends InfluenceCalculatorBasic{

    private final PawnColor ignoredColor;

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
            if( (ignoredColor != colors) && (player.getSchool().containsProfessor(colors)) ){
                playersInfluence.put(player, playersInfluence.get(player) + island.countStudents(colors) );
            }
        }
    }
}
