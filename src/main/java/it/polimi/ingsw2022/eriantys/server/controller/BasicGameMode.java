package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseCloudRequest;
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

            if (!(game.getInfluenceCalculator() instanceof InfluenceCalculatorBasic))
                game.setInfluenceCalculator(new InfluenceCalculatorBasic());

            fillCloudIslands();

            for (Player player : game.getPlayers()) {

                game.setCurrentPlayer(player);
                chooseHelperCard(player.username);
            }

            game.sortPlayersBasedOnHelperCard();

            for (Player player : game.getPlayers()) {

                game.setCurrentPlayer(player);
                for (int studentMove = 0; studentMove < 3; studentMove++)
                    chooseAndMoveStudent(player.username);

                moveMotherNature(player.username);
                checkIslandInfluence();
                chooseCloud(player.username);
            }
        }
    }

    private void fillCloudIslands() throws IOException {

        Update update = new Update();

        for (int cloudIndex = 0; cloudIndex < game.getPlayers().size(); cloudIndex++) {

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

            try{

                performedMoveMessage.move.apply(game, playerUsername);
                server.sendToClient(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)), playerUsername);
                //TODO check for last played cards
            }
            catch(NoSuchElementException e) {

                server.sendToClient(new InvalidMoveMessage(performedMoveMessage, performedMoveMessage.previousMessage,
                        "No card of the given index found in hand"), playerUsername);
            }

            performedMoveMessage = null;
            notifyAll();
        }
    }

    private void chooseAndMoveStudent(String playerUsername) throws IOException, InterruptedException {

        server.sendToClient(new MoveRequestMessage(new MoveStudentRequest()), playerUsername);

        synchronized (this) {

            while (performedMoveMessage == null) this.wait();

            try{

                performedMoveMessage.move.apply(game, playerUsername);
                server.sendToAllClients(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)));
            }
            catch(Exception e) {

                server.sendToClient(new InvalidMoveMessage(performedMoveMessage, performedMoveMessage.previousMessage,
                        "No student of the selected color is not available in school dashboard"), playerUsername);
            }
            performedMoveMessage = null;
            notifyAll();
        }
    }

    public void moveMotherNature(String playerUsername) throws IOException, InterruptedException {

        int motherNatureMaxSteps = game.getPlayer(playerUsername).getCurrentHelper().movement;
        server.sendToClient(new MoveRequestMessage(new MoveMotherNatureRequest(motherNatureMaxSteps)), playerUsername);

        synchronized (this) {

            while (performedMoveMessage == null) this.wait();

            performedMoveMessage.move.apply(game, playerUsername);
            server.sendToAllClients(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)));
            //TODO controllo mossa

            performedMoveMessage = null;
            notifyAll();
        }
    }

    private void checkIslandInfluence() throws IOException {

        CompoundIslandTile motherNatureIsland = game.getBoard().getMotherNatureIsland();

        Optional<Team> dominantTeam = game.getInfluenceCalculator().calculateInfluence(
                game.getPlayers(),
                motherNatureIsland,
                game.getCurrentPlayer());

        //If there is a dominant team, find its leader and check the towers
        if (dominantTeam.isPresent()) updateTowers(dominantTeam.get(), motherNatureIsland);
    }

    private void updateTowers(Team dominantTeam, CompoundIslandTile island) throws IOException {

        Update update = new Update();

        int islandSize = island.getNumberOfTiles();

        Player dominantTeamLeader = dominantTeam.getLeader();

        // If there's a team controlling the island and it's not the new team who will control the island, remove the old tower
        if (island.getTeam().isPresent()) {

            Player currentControllingTeamLeader = island.getTeam().get().getLeader();
            for (int i = 0; i < islandSize; i++) currentControllingTeamLeader.getSchool().addTower();

            SchoolChange oldSchoolChange = new SchoolChange(currentControllingTeamLeader.getSchool());
            update.addChange(oldSchoolChange);
        }

        for (int i = 0; i < islandSize; i++) dominantTeamLeader.getSchool().removeTower();
        SchoolChange newSchoolChange = new SchoolChange(dominantTeamLeader.getSchool());
        update.addChange(newSchoolChange);

        island.setTeam(dominantTeam);
        //TODO: merge islands
        IslandChange islandChange = new IslandChange(game.getBoard().getIslandIndex(island), island);
        update.addChange(islandChange);

        //TODO: check end of game

        server.sendToAllClients(new UpdateMessage(update));
    }

    private void chooseCloud(String playerUsername) throws IOException, InterruptedException {

        server.sendToClient(new MoveRequestMessage(new ChooseCloudRequest()), playerUsername);

        synchronized (this) {

            while (performedMoveMessage == null) this.wait();

            performedMoveMessage.move.apply(game, playerUsername);
            server.sendToAllClients(new UpdateMessage(performedMoveMessage.move.getUpdate(game, playerUsername)));
            //TODO controllo mossa

            performedMoveMessage = null;
            notifyAll();
        }
    }

    public synchronized void setPerformedMoveMessage(PerformedMoveMessage performedMoveMessage) {

        this.performedMoveMessage = performedMoveMessage;
        notifyAll();
    }
}
