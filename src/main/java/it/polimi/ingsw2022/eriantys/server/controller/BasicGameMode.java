package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.Move.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class BasicGameMode implements GameMode {

    private Game game;
    private EriantysServer server = EriantysServer.getInstance();
    private Move performedMove;

    public BasicGameMode(Game game) {
        this.game = game;
    }

    @Override
    public void playGame() throws IOException, InterruptedException {
        while (!game.isGameEnding()) {
            fillCloudIslands();
            for (Player player : game.getPlayers()) {
                game.setCurrentPlayer(player);
                chooseHelperCard(player.username);
                // save helper cards
            }
        }
    }

    private void chooseHelperCard(String playerUsername) throws IOException, InterruptedException {
        server.sendToClient(new MoveRequestMessage(MoveType.CHOOSE_HELPER_CARD), server.getClients().get(playerUsername));
        synchronized (this) {
            while (performedMove == null) this.wait();
            if (!(performedMove instanceof ChooseHelperCard)) {
                // TODO: send InvalidMoveMessage
            }
            ChooseHelperCard chooseHelperCard = (ChooseHelperCard) performedMove;
            List<HelperCard> updatedHelperCards = game.getPlayer(playerUsername).getHelperCards();
            server.sendToClient(craftHelperCardUpdateMessage(updatedHelperCards), server.getClients().get(playerUsername));
            performedMove = null;
            notifyAll();
        }
    }

    public synchronized void setPerformedMove(Move performedMove) {
        this.performedMove = performedMove;
        notifyAll();
    }

    // Updating module
    private void fillCloudIslands() {
        for (int cloudIndex = 0; cloudIndex < game.getPlayers().length; cloudIndex++) {
            for (int i = 0; i < 3; i++) {
                ColoredPawn student = game.getStudentsBag().extractRandomStudent();
                game.getBoard().getCloud(cloudIndex).addStudent(student);
            }
        }
    }


    // Creating updates
    private UpdateMessage craftHelperCardUpdateMessage(List<HelperCard> helperCards) {
        Update helperCardUpdate = new Update();
        HelperCardsChange helperCardsChange = new HelperCardsChange();
        helperCardsChange.addHelperCards(helperCards);
        helperCardUpdate.addChange(helperCardsChange);
        System.out.println("Crafted update msg for client");
        return new UpdateMessage(helperCardUpdate);
    }
}
