package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBonus;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoColor;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoTowers;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

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
    public boolean isValid(Game game) {

        return characterCardIndex >= 1 && characterCardIndex <= 12 &&
                game.getCurrentPlayer().getCoins() >= game.getCharacterOfIndex(characterCardIndex).getCost() &&
                !game.getCurrentPlayer().isCharacterUsed();
    }

    @Override
    public void apply(Game game) {

        CharacterCard characterCard = game.getCharacterOfIndex(characterCardIndex);

        game.getCurrentPlayer().payCoins(characterCard.getCost());
        characterCard.incrementCost();
        game.getCurrentPlayer().setCharacterUsed(true);

        switch(characterCardIndex) {

            case 1:
            case 11:
                characterCard.addStudent(game.getStudentsBag().extractRandomStudent());
                break;
            case 2:
                for(PawnColor color : PawnColor.values()) game.checkAndUpdateProfessor(color, true);
                break;
            case 6:
                game.setInfluenceCalculator(new InfluenceCalculatorNoTowers());
                break;
            case 8:
                game.setInfluenceCalculator(new InfluenceCalculatorBonus());
                break;
            default: break;
        }
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        CharacterCardsChange characterCardsChange = new CharacterCardsChange();
        PlayerChange playerChange = new PlayerChange(game.getCurrentPlayer());

        if(characterCardIndex == 2) {
            for (Player player : game.getPlayers()) update.addChange(new SchoolChange(player.getSchool())); }

        for(int i=0; i<game.getNumberOfCharacters(); i++){
            characterCardsChange.addCharacterCard(game.getCharacter(i));
        }

        update.addChange(characterCardsChange);
        update.addChange(playerChange);

        return update;
    }
}
