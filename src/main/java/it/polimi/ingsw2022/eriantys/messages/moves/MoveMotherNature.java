package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveMotherNatureRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.Board;

/**
 * This class represents the movement of mother nature by specifying the destination island's index
 *
 * @author Emanuele Musto
 */
public class MoveMotherNature extends Move {
    
    private final int islandIndex;
    private final int motherNatureMaxSteps;
    private int oldIslandIndex;
    
    public MoveMotherNature(int islandIndex, int motherNatureMaxSteps) {
        
        this.islandIndex = islandIndex;
        this.motherNatureMaxSteps = motherNatureMaxSteps;
    }
    
    @Override
    public boolean isValid(Game game, MoveRequest request) {
        
        if (!(request instanceof MoveMotherNatureRequest)) {
            
            errorMessage = "Move not requested";
            return false;
        }
        
        oldIslandIndex = game.getBoard().getMotherNatureIsland().getIndex();
        if (oldIslandIndex == islandIndex) {
            
            errorMessage = "Mother nature must move of at least 1 step";
            return false;
        }
        
        int steps = 0;
        while ((oldIslandIndex + steps) % game.getBoard().getNumberOfIslands() != islandIndex) {
            
            steps++;
            if (steps > motherNatureMaxSteps) {
                
                errorMessage = "Cannot move mother nature of more than " + motherNatureMaxSteps + " steps";
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void apply(Game game) {
        
        oldIslandIndex = game.getBoard().getMotherNatureIsland().getIndex();
        int steps = islandIndex - oldIslandIndex;
        game.getBoard().moveMotherNature(steps);
    }
    
    @Override
    public Update getUpdate(Game game) {
        
        Board board = game.getBoard();
        Update update = new Update();
        IslandChange islandChange = new IslandChange(board.getIslands(), board.getIslandTiles());
        update.addChange(islandChange);
        
        return update;
    }
}
