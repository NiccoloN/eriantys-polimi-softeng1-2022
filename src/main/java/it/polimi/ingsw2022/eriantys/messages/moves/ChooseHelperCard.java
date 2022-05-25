package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * This class represents the choice of a helper card by a player
 * @author Emanuele Musto
 */
public class ChooseHelperCard implements Move, Serializable {

    private final int helperCardIndex;

    public ChooseHelperCard(int helperCardIndex) {

        this.helperCardIndex = helperCardIndex;
    }

    @Override
    public String apply(Game game) {

        try {

            game.getCurrentPlayer().playHelperCard(helperCardIndex);
        }
        catch (NoSuchElementException e) {

            return "No card of the given index found in hand";
        }

        return null;
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();
        HelperCardsChange change = new HelperCardsChange();

        Player player = game.getCurrentPlayer();
        change.addHelperCards(player.getHelperCards());
        change.setPlayedHelperCard(player.getCurrentHelper(), player.username);

        update.addChange(change);
        System.out.println("Crafted helper card update");

        return update;
    }
}
