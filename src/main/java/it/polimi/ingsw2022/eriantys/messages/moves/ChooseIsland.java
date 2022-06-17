package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.util.Optional;

public class ChooseIsland extends Move{

    public final int compoundIslandIndex;
    public final int characterCardIndex;

    public ChooseIsland(int compoundIslandIndex, int characterCardIndex){

        this.compoundIslandIndex = compoundIslandIndex;
        this.characterCardIndex = characterCardIndex;
    }

    @Override
    public boolean isValid(Game game) {

        boolean validParameters = (compoundIslandIndex >= 0 && compoundIslandIndex <= game.getBoard().getNumberOfIslands()) &&
                (characterCardIndex >= 1 && characterCardIndex <= 12);

        switch(characterCardIndex) {

            case 3:
                errorMessage = "Invalid parameters (island index or character card index).";
                return validParameters;
            case 5:
                errorMessage = "Deny Tiles are not available or invalid parameters";
                int numberOfDenyTiles = game.getCharacterOfIndex(characterCardIndex).getDenyTilesNumber();
                return numberOfDenyTiles > 0 && validParameters;
            default:
                errorMessage = "Invalid move.";
                return false;
        }
    }

    @Override
    public void apply(Game game) {

        switch(characterCardIndex) {

            case 3:
                game.setCharacterIsland(compoundIslandIndex);
                break;
            case 5:
                game.getCharacterOfIndex(characterCardIndex).decrementDenyTiles();
                game.getBoard().getIsland(compoundIslandIndex).incrementNumberOfDenyCards();
                break;
            default: break;
        }
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();
        IslandChange islandChange;

        switch(characterCardIndex) {

            case 3:

                islandChange = new IslandChange(game.getBoard().getIslandTiles());
                update.addChange(islandChange);

                update.addChange(new SchoolChange(game.getCurrentPlayer().getSchool()));
                break;
            case 5:

                islandChange = new IslandChange(game.getBoard().getIslandTiles());
                update.addChange(islandChange);

                CharacterCardsChange characterCardsChange = new CharacterCardsChange();
                for(int i=0; i<game.getNumberOfCharacters(); i++) characterCardsChange.addCharacterCard(game.getCharacter(i));
                update.addChange(characterCardsChange);
                break;
            default: break;
        }

        return update;
    }
}
