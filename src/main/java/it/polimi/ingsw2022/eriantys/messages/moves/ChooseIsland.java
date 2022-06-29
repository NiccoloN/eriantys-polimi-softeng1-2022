package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseIslandRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.Board;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;

import java.util.NoSuchElementException;

/**
 * This class represents the choice of an island by a player. It's mainly used for character cards effects.
 * @author Emanuele Musto
 */
public class ChooseIsland extends Move {
    
    public final int compoundIslandIndex;
    public final int characterCardIndex;
    
    public ChooseIsland(int compoundIslandIndex, int characterCardIndex) {
        
        this.compoundIslandIndex = compoundIslandIndex;
        this.characterCardIndex  = characterCardIndex;
    }
    
    @Override
    public boolean isValid(Game game, MoveRequest request) {
        
        if(!(request instanceof ChooseIslandRequest)) {
            
            errorMessage = "Move not requested";
            return false;
        }
        
        if(compoundIslandIndex < 0 || compoundIslandIndex >= game.getBoard().getNumberOfIslands()) {
            
            errorMessage = "Invalid island index";
            return false;
        }
        
        CharacterCard characterCard;
        
        try {
            
            characterCard = game.getCharacterOfIndex(characterCardIndex);
        }
        catch(NoSuchElementException e) {
            
            errorMessage = "Invalid character index";
            return false;
        }
        
        if(characterCardIndex == 5 && characterCard.getDenyTilesNumber() <= 0) {
            
            errorMessage = "Deny Tiles are not available or invalid parameters";
            return false;
        }
        
        return true;
    }
    
    @Override
    public void apply(Game game) {
        
        switch(characterCardIndex) {
            
            case 3:
                game.setCharacterIsland(compoundIslandIndex);
                break;
            case 5:
                game.getCharacterOfIndex(characterCardIndex).decrementDenyTiles();
                game.getBoard().getIsland(compoundIslandIndex).incrementNumberOfDenyTiles();
                break;
            default:
                break;
        }
    }
    
    @Override
    public Update getUpdate(Game game) {
        
        Update update = new Update();
        IslandChange islandChange;
        Board board = game.getBoard();
        
        switch(characterCardIndex) {
            
            case 3:
                
                islandChange = new IslandChange(board.getIslands(), board.getIslandTiles());
                update.addChange(islandChange);
                
                update.addChange(new SchoolChange(game.getCurrentPlayer().getSchool()));
                break;
            case 5:
                
                islandChange = new IslandChange(board.getIslands(), board.getIslandTiles());
                update.addChange(islandChange);
                
                CharacterCardsChange characterCardsChange = new CharacterCardsChange();
                for(int i = 0; i < game.getNumberOfCharacters(); i++) characterCardsChange.addCharacterCard(game.getCharacter(i));
                update.addChange(characterCardsChange);
                break;
            default:
                break;
        }
        
        return update;
    }
}
