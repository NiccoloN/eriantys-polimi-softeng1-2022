package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.Move.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class BasicGameMode implements GameMode {

    private Game game;
    private EriantysServer server = EriantysServer.getInstance();
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
            int playerIndex = 0;
            for (Player player : game.getPlayers()) {
                game.setCurrentPlayer(player);
                for (int studentMove = 0; studentMove < 3; studentMove++) {
                    chooseAndMoveStudent(player.username, playerIndex);
                }
                playerIndex++;
            }
        }
    }

    private void chooseHelperCard(String playerUsername) throws IOException, InterruptedException {
        Socket clientSocket =  server.getClientSocket(playerUsername);
        Player currentPlayer = game.getPlayer(playerUsername);
        server.sendToClient(new MoveRequestMessage(MoveType.CHOOSE_HELPER_CARD), clientSocket);
        synchronized (this) {
            while (performedMoveMessage == null) this.wait();
            if (!(performedMoveMessage.move instanceof ChooseHelperCard)) {
                // TODO: send InvalidMoveMessage
            }
            ChooseHelperCard chooseHelperCard = (ChooseHelperCard) performedMoveMessage.move;
            currentPlayer.playHelperCard(chooseHelperCard.helperCardIndex);
            List<HelperCard> updatedHelperCards = currentPlayer.getHelperCards();
            server.sendToClient(craftHelperCardUpdateMessage(updatedHelperCards), clientSocket);
            performedMoveMessage = null;
            notifyAll();
        }
    }

    private void chooseAndMoveStudent(String playerUsername, int playerIndex) throws IOException, InterruptedException {
        Socket clientSocket = server.getClientSocket(playerUsername);
        Player currentPlayer = game.getCurrentPlayer();
        server.sendToClient(new MoveRequestMessage(MoveType.MOVE_STUDENT), clientSocket);
        synchronized (this) {
            while (performedMoveMessage == null) this.wait();
            if (!(performedMoveMessage.move instanceof MoveStudent)) {
                server.sendToClient(new InvalidMoveMessage(
                        performedMoveMessage,
                        performedMoveMessage.previousMessage,
                        "Error, required move is a " + MoveType.MOVE_STUDENT), clientSocket);
            }
            MoveStudent moveStudent = (MoveStudent) performedMoveMessage.move;
            ColoredPawn movedStudent = null;
            try{movedStudent = currentPlayer.getSchool().removeFromEntrance(moveStudent.studentColor); }
            catch(Exception e){
                server.sendToClient(new InvalidMoveMessage(
                        performedMoveMessage,
                        performedMoveMessage.previousMessage,
                        "Student is not available in school dashboard"), clientSocket);
            }
            Update update = new Update();
            if (moveStudent.toDining) currentPlayer.getSchool().addToTable(movedStudent);
            SchoolChange schoolChange = new SchoolChange(currentPlayer.getSchool());
            update.addChange(schoolChange);
            if (moveStudent.toIsland) {
                game.getBoard().getIsland(moveStudent.islandIndex).addStudent(movedStudent);
                IslandChange islandChange = new IslandChange(moveStudent.islandIndex, game.getBoard().getIsland(moveStudent.islandIndex));
                update.addChange(islandChange);
            }
            server.sendToClient(new UpdateMessage(update), clientSocket);
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

    // Creating updates
    private UpdateMessage craftHelperCardUpdateMessage(List<HelperCard> helperCards) {
        Update helperCardUpdate = new Update();
        HelperCardsChange helperCardsChange = new HelperCardsChange();
        helperCardsChange.addHelperCards(helperCards);
        helperCardUpdate.addChange(helperCardsChange);
        System.out.println("Crafted helper card update");
        return new UpdateMessage(helperCardUpdate);
    }


}
