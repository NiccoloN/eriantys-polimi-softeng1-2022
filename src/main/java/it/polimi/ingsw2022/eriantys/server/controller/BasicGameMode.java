package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.Move.ChooseHelperCard;
import it.polimi.ingsw2022.eriantys.messages.Move.Move;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.Move.MoveType;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.toClient.changes.SchoolChange;
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
            while (performedMove == null) this.wait();
            if (!(performedMove instanceof ChooseHelperCard)) {
                // TODO: send InvalidMoveMessage
            }
            ChooseHelperCard chooseHelperCard = (ChooseHelperCard) performedMove;
            currentPlayer.playHelperCard(chooseHelperCard.helperCardIndex);
            List<HelperCard> updatedHelperCards = currentPlayer.getHelperCards();
            server.sendToClient(craftHelperCardUpdateMessage(updatedHelperCards), clientSocket);
            performedMove = null;
            notifyAll();
        }
    }

    private void chooseAndMoveStudent(String playerUsername, int playerIndex) throws IOException, InterruptedException {
        Socket clientSocket = server.getClientSocket(playerUsername);
        Player currentPlayer = game.getCurrentPlayer();
        server.sendToClient(new MoveRequestMessage(MoveType.MOVE_STUDENT), clientSocket);
        synchronized (this) {
            while (performedMove == null) this.wait();
            if (!(performedMove instanceof MoveStudent)) {
                // TODO: send InvalidMoveMessage
            }
            MoveStudent moveStudent = (MoveStudent) performedMove;
            ColoredPawn movedStudent = game.getBoard().getSchool(playerIndex).removeFromEntrance(moveStudent.studentColor);
            if (moveStudent.toDining) {
                game.getBoard().getSchool(playerIndex).addToTable(movedStudent);
                server.sendToClient(craftSchoolDashboardUpdate(playerIndex), clientSocket);
            } else if (moveStudent.toIsland) {
                game.getBoard().getIsland(moveStudent.islandIndex).addStudent(movedStudent);
                server.sendToClient(craftIslandUpdate(moveStudent.islandIndex), clientSocket);
            }
            performedMove = null;
            notifyAll();
        }
    }

    public synchronized void setPerformedMove(Move performedMove) {
        this.performedMove = performedMove;
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

    private UpdateMessage craftIslandUpdate(int islandIndex) {
        Update islandUpdate = new Update();
        IslandChange islandChange = new IslandChange(islandIndex, game.getBoard().getIsland(islandIndex));
        islandUpdate.addChange(islandChange);
        System.out.println("Crafted island update");
        return new UpdateMessage(islandUpdate);
    };

    private UpdateMessage craftSchoolDashboardUpdate(int playerIndex) {
        Update schoolUpdate = new Update();
        SchoolChange schoolChange = new SchoolChange(game.getBoard().getSchool(playerIndex));
        schoolUpdate.addChange(schoolChange);
        System.out.println("Crafted school dashboard update");
        return new UpdateMessage(schoolUpdate);
    };

}
