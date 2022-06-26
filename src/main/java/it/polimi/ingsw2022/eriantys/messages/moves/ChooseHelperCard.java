package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseHelperCardRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
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

        this.helperCardIndex = helperCardIndex;
    }

    @Override
    public boolean isValid(Game game, MoveRequest request) {

        if(!(request instanceof ChooseHelperCardRequest)) {

            errorMessage = "Move not requested";
            return false;
        }

        Player player = game.getCurrentPlayer();

        List<Integer> unplayableIndices = new ArrayList<>(3);
        for(Player other : game.getPlayers())
            if(other != player && other.getCurrentHelper() != null)
                unplayableIndices.add(other.getCurrentHelper().index);

        if(unplayableIndices.size() >= player.getNumberOfHelpers()) unplayableIndices.clear();

        if(unplayableIndices.contains(helperCardIndex)) {

            errorMessage = "Cannot play this helper";
            return false;
        }

        if(!player.hasHelper(helperCardIndex)) {

            errorMessage = "No card of the given index found in hand";
            return false;
        }

        return true;
    }

    @Override
    public void apply(Game game) {

        game.getCurrentPlayer().playHelperCard(helperCardIndex);
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        Player player = game.getCurrentPlayer();

        HelperCardsChange change = new HelperCardsChange(player.getUsername());
        change.addHelperCards(player.getHelperCards());
        change.setPlayedHelperCard(player.getPrevHelper());

        update.addChange(change);

        return update;
    }
}
