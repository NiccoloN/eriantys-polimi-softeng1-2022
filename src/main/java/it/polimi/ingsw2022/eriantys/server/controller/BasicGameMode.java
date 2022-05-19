package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.moves.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;

public class BasicGameMode implements GameMode {

    private final Game game;
    private final EriantysServer server = EriantysServer.getInstance();
    private PerformedMoveMessage performedMoveMessage;

    public BasicGameMode(Game game) {
        this.game = game;
    }

    @Override
    public void playGame() throws IOException, InterruptedException {

        while (!game.isGameEnding()) {

            //fillCloudIslands();
            for (Player player : game.getPlayers()) {

                game.setCurrentPlayer(player); // Maybe it's useless
                chooseHelperCard(player.username);
            }

            game.reorderPlayersBasedOnHelperCard();

            for (Player player : game.getPlayers()) {

                game.setCurrentPlayer(player);

                for (int studentMove = 0; studentMove < 3; studentMove++) {

                    chooseAndMoveStudent(player.username);
                }
            }
        }
    }

    private void chooseHelperCard(String playerUsername) throws IOException, InterruptedException {

        server.sendToClient(new MoveRequestMessage(MoveType.CHOOSE_HELPER_CARD), playerUsername);
        synchronized (this) {
            while (performedMoveMessage == null) this.wait();
            if (!(performedMoveMessage.move instanceof ChooseHelperCard)) {
                // TODO: send InvalidMoveMessage
            }

            try{ performedMoveMessage.move.apply(game, playerUsername); }
            catch(NoSuchElementException e){
                //TODO : send InvalidMoveMessage
            }

            server.sendToAllClients(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)));
            performedMoveMessage = null;
            notifyAll();
        }
    }

    private void chooseAndMoveStudent(String playerUsername) throws IOException, InterruptedException {

        server.sendToClient(new MoveRequestMessage(MoveType.MOVE_STUDENT), playerUsername);

        synchronized (this) {

            while (performedMoveMessage == null) this.wait();

            if (!(performedMoveMessage.move instanceof MoveStudent)) {
                server.sendToClient(new InvalidMoveMessage(
                        performedMoveMessage,
                        performedMoveMessage.previousMessage,
                        "Error, required move is a " + MoveType.MOVE_STUDENT), playerUsername);
            }

            try{ performedMoveMessage.move.apply(game, playerUsername); }
            catch(Exception e) {

                e.printStackTrace();
                server.sendToClient(new InvalidMoveMessage(
                        performedMoveMessage,
                        performedMoveMessage.previousMessage,
                        "Student is not available in school dashboard"), playerUsername);
            }

            server.sendToAllClients(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)));
            performedMoveMessage = null;
            notifyAll();
        }
    }

    public synchronized void setPerformedMoveMessage(PerformedMoveMessage performedMoveMessage) {

        this.performedMoveMessage = performedMoveMessage;
        notifyAll();
    }

    // Updating model
    private void fillCloudIslands() {

        for (int cloudIndex = 0; cloudIndex < game.getPlayers().length; cloudIndex++) {

            for (int i = 0; i < 3; i++) {

                ColoredPawn student = game.getStudentsBag().extractRandomStudent();
                game.getBoard().getCloud(cloudIndex).addStudent(student);
            }
        }
    }
}
