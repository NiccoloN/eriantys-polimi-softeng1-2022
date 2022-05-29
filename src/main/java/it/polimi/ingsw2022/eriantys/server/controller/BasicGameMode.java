package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.moves.Move;
import it.polimi.ingsw2022.eriantys.messages.requests.*;
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
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.util.*;

public class BasicGameMode implements GameMode {

    private final Game game;
    private final EriantysServer server;

    public BasicGameMode(Game game) {

        this.game = game;
        server = EriantysServer.getInstance();
    }

    @Override
    public void playGame() throws IOException, InterruptedException {

        while (!game.isGameEnding()) {

            if (!(game.getInfluenceCalculator() instanceof InfluenceCalculatorBasic))
                game.setInfluenceCalculator(new InfluenceCalculatorBasic());

            fillClouds();
            playHelpers();

            game.sortPlayersBasedOnHelperCard();

            for (Player player : game.getPlayers()) {

                game.setCurrentPlayer(player);
                for (int studentMove = 0; studentMove < 3; studentMove++) {

                    ArrayList<PawnColor> availableColors = new ArrayList<>();
                    for(PawnColor color : PawnColor.values())
                        if(player.getSchool().countEntranceStudents(color) > 0) availableColors.add(color);

                    requestMove(new MoveStudentRequest(availableColors), player.username);
                }

                requestMove(new MoveMotherNatureRequest(player.getCurrentHelper().movement), player.username);

                checkIslandInfluence();

                requestMove(new ChooseCloudRequest(), player.username);
            }
        }
    }

    private void fillClouds() throws IOException {

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

    private void playHelpers() throws IOException, InterruptedException {

        for (Player player : game.getPlayers()) player.resetCurrentHelper();

        List<Integer> unplayableIndices = new ArrayList<>(3);
        for (Player player : game.getPlayers()) {

            game.setCurrentPlayer(player);

            unplayableIndices.clear();
            for(Player other : game.getPlayers())
                if(other != player && other.getCurrentHelper() != null)
                    unplayableIndices.add(other.getCurrentHelper().index);

            if(unplayableIndices.size() >= player.getNumberOfHelpers()) unplayableIndices.clear();
            requestMove(new ChooseHelperCardRequest(unplayableIndices), player.username);
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
        int mergeStatus = game.checkAndMergeIslands(island);
        if (mergeStatus != 0) {
            for (int islandIndex = 0; islandIndex < game.getBoard().getNumberOfIslands(); islandIndex++) {
                update.addChange(
                        new IslandChange(islandIndex, game.getBoard().getIsland(islandIndex))
                );
            }
        } else {
            update.addChange(new IslandChange(game.getBoard().getIslandIndex(island), island));
        }
        //TODO: check end of game

        server.sendToAllClients(new UpdateMessage(update));
    }

    private void requestMove(MoveRequest request, String playerUsername) throws IOException, InterruptedException {

        MoveRequestMessage requestMessage = new MoveRequestMessage(request);
        server.sendToClient(requestMessage, playerUsername);
        requestMessage.waitForValidResponse();
    }

    public synchronized void managePerformedMoveMessage(PerformedMoveMessage performedMoveMessage) throws IOException {

        Move move = performedMoveMessage.move;

        if (move.isValid(game)) {

            move.apply(game);
            server.sendToAllClients(new UpdateMessage(move.getUpdate(game)));
            performedMoveMessage.getPreviousMessage().acceptResponse();
        }
        else {

            server.sendToClient(
                    new InvalidMoveMessage(performedMoveMessage, performedMoveMessage.getPreviousMessage(), move.getErrorMessage()),
                    game.getCurrentPlayer().username);
        }
    }
}
