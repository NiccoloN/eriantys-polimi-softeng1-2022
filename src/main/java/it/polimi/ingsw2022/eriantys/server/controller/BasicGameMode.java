package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveMotherNature;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseHelperCardRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveMotherNatureRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.CloudTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.IOException;
import java.util.NoSuchElementException;

public class BasicGameMode implements GameMode {

    private final Game game;
    private final EriantysServer server;
    private PerformedMoveMessage performedMoveMessage;

    public BasicGameMode(Game game) {

        this.game = game;
        server = EriantysServer.getInstance();
    }

    @Override
    public void playGame() throws IOException, InterruptedException {

        while (!game.isGameEnding()) {

            fillCloudIslands();
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

                moveMotherNature(player.username);
            }
        }
    }

    private void fillCloudIslands() throws IOException {

        Update update = new Update();

        for (int cloudIndex = 0; cloudIndex < game.getPlayers().length; cloudIndex++) {

            CloudTile cloud = game.getBoard().getCloud(cloudIndex);

            for (int i = 0; i < 3; i++) {

                ColoredPawn student = game.getStudentsBag().extractRandomStudent();
                cloud.addStudent(student);
            }

            update.addChange(new CloudChange(cloudIndex, cloud));
        }

        server.sendToAllClients(new UpdateMessage(update));
    }

    private void chooseHelperCard(String playerUsername) throws IOException, InterruptedException {

        server.sendToClient(new MoveRequestMessage(new ChooseHelperCardRequest()), playerUsername);

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

        server.sendToClient(new MoveRequestMessage(new MoveStudentRequest()), playerUsername);

        synchronized (this) {

            while (performedMoveMessage == null) this.wait();

            if (!(performedMoveMessage.move instanceof MoveStudent)) {

                server.sendToClient(new InvalidMoveMessage(performedMoveMessage, performedMoveMessage.previousMessage,
                        "the user is required to move a student"), playerUsername);
            }

            try{ performedMoveMessage.move.apply(game, playerUsername); }
            catch(Exception e) {

                server.sendToClient(new InvalidMoveMessage(performedMoveMessage, performedMoveMessage.previousMessage,
                        "a student of the selected color is not available in your school dashboard"), playerUsername);
            }

            server.sendToAllClients(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)));
            performedMoveMessage = null;
            notifyAll();
        }
    }

    public void moveMotherNature(String playerUsername) throws IOException, InterruptedException {

        int motherNatureMaxSteps = game.getPlayer(playerUsername).getCurrentHelper().movement;
        server.sendToClient(new MoveRequestMessage(new MoveMotherNatureRequest(motherNatureMaxSteps)), playerUsername);

        synchronized (this) {

            while (performedMoveMessage == null) this.wait();

            if (!(performedMoveMessage.move instanceof MoveMotherNature)) {
                // TODO: send InvalidMoveMessage
            }

            performedMoveMessage.move.apply(game, playerUsername);
            //TODO controllo mossa

            server.sendToAllClients(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)));
            performedMoveMessage = null;
            notifyAll();
        }
    }


    public synchronized void setPerformedMoveMessage(PerformedMoveMessage performedMoveMessage) {

        this.performedMoveMessage = performedMoveMessage;
        notifyAll();
    }
}
