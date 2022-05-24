package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
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
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.IslandTile;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBasic;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.util.*;
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

            // fillCloudIslands();
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
                checkIslandInfluence();
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

    private void checkIslandInfluence() throws IOException {
        CompoundIslandTile motherNatureIsland = game.getBoard().getMotherNatureIsland();

        Optional<Team> dominantTeam = new InfluenceCalculatorBasic().calculateInfluence(
                new ArrayList<>(Arrays.asList(game.getPlayers())),
                motherNatureIsland,
                game.getCurrentPlayer());

        // If there is a dominant team, find its leader and check the towers
        if (dominantTeam.isPresent()) {
            updateTower(dominantTeam.get(), motherNatureIsland);
        }
    }

    private void updateTower(Team dominantTeam, CompoundIslandTile island) throws IOException {
        int islandSize = island.getNumberOfTiles();

        Player dominantTeamLeader = dominantTeam.getLeader();
        Player currentControllingTeamLeader = null;

        // If there's a team controlling the island and it's not the new team who will control the island, remove the old tower
        if (island.getTeam().isPresent()) {
            currentControllingTeamLeader = island.getTeam().get().getLeader();
            for (int i = 0; i < islandSize; i++) {
                currentControllingTeamLeader.getSchool().addTower();
            }
        }
        for (int i = 0; i < islandSize; i++) {
            dominantTeamLeader.getSchool().removeTower();
            // TODO: check end of game
        }
        island.setTeam(dominantTeam);
        //TODO: merge islands

        UpdateMessage influenceUpdate = craftInfluenceUpdate(
                island,
                currentControllingTeamLeader,
                dominantTeamLeader);

        server.sendToAllClients(influenceUpdate);
    }

    private UpdateMessage craftInfluenceUpdate(CompoundIslandTile island, Player currentTeamLeader, Player dominantTeamLeader) {
        Update update = new Update();

        IslandChange islandChange = new IslandChange(game.getBoard().getIslandIndex(island), island);
        update.addChange(islandChange);

        if (currentTeamLeader != null) {
            SchoolChange oldSchoolChange = new SchoolChange(currentTeamLeader.getSchool());
            update.addChange(oldSchoolChange);
        }

        SchoolChange newSchoolChange = new SchoolChange(dominantTeamLeader.getSchool());
        update.addChange(newSchoolChange);

        return new UpdateMessage(update);
    }


    public synchronized void setPerformedMoveMessage(PerformedMoveMessage performedMoveMessage) {

        this.performedMoveMessage = performedMoveMessage;
        notifyAll();
    }
}
