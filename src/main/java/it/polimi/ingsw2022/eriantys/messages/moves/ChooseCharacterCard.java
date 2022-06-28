package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBonus;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoTowers;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.util.NoSuchElementException;

/**
 * This class represents the choice of a Character by a player
 * @author Emanuele Musto
 */
public class ChooseCharacterCard extends Move {

    public final int characterCardIndex;

    public ChooseCharacterCard(int characterCardIndex) {

        this.characterCardIndex = characterCardIndex;
    }

    @Override
    public boolean isValid(Game game, MoveRequest request) {

        CharacterCard characterCard;

        try {

            characterCard = game.getCharacterOfIndex(characterCardIndex);
        }
        catch(NoSuchElementException e) {

            errorMessage = "Invalid character index";
            return false;
        }

        if(game.getCurrentPlayer().getCoins() < characterCard.getCost()) {

            errorMessage = "Not enough coins";
            return false;
        }

        if(game.getCurrentPlayer().hasPlayedCharacter()) {

            errorMessage = "You already used a character in this turn";
            return false;
        }

        if(characterCardIndex == 10 && game.getCurrentPlayer().getSchool().getAvailableTableColors().isEmpty()) {

            errorMessage = "Not enough students in your school's tables";
            return false;
        }

        return true;
    }

    @Override
    public void apply(Game game) {

        CharacterCard characterCard = game.getCharacterOfIndex(characterCardIndex);

        game.getCurrentPlayer().payCoins(characterCard.getCost());
        characterCard.incrementCost();
        game.getCurrentPlayer().setCharacterUsed(true);
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        CharacterCardsChange characterCardsChange = new CharacterCardsChange();
        PlayerChange playerChange = new PlayerChange(game.getCurrentPlayer());

        if(characterCardIndex == 2) {
            for (Player player : game.getPlayers()) update.addChange(new SchoolChange(player.getSchool())); }

        for(int i=0; i<game.getNumberOfCharacters(); i++) {
            characterCardsChange.addCharacterCard(game.getCharacter(i));
        }

        update.addChange(characterCardsChange);
        update.addChange(playerChange);

        return update;
    }
}
