package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the choice of a helper card by a player
 * @author Emanuele Musto
 */
public class ChooseHelperCard extends Move {

    private final int helperCardIndex;

    public ChooseHelperCard(int helperCardIndex) {

        super(MoveType.CHOOSE_HELPER_CARD);
        this.helperCardIndex = helperCardIndex;
    }

    @Override
    public void apply(Game game, String playerUsername) {

        game.getPlayer(playerUsername).playHelperCard(helperCardIndex);
    }

    @Override
    public Update getUpdate(Game game, String playerUsername) {

        List<HelperCard> updatedHelperCards = game.getPlayer(playerUsername).getHelperCards();
        Update helperCardUpdate = new Update();
        HelperCardsChange helperCardsChange = new HelperCardsChange();
        helperCardsChange.addHelperCards(updatedHelperCards);
        helperCardUpdate.addChange(helperCardsChange);
        System.out.println("Crafted helper card update");

        return helperCardUpdate;
    }
}
