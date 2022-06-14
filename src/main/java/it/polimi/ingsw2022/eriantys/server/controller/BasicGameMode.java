package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.*;
import it.polimi.ingsw2022.eriantys.messages.moves.Move;
import it.polimi.ingsw2022.eriantys.messages.requests.*;
import it.polimi.ingsw2022.eriantys.messages.toClient.GameEndedMessage;
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

public class BasicGameMode implements GameMode {

    protected final Game game;
    protected final EriantysServer server;

    public BasicGameMode(Game game) {

        this.game = game;
        server = EriantysServer.getInstance();
    }

    public Update[] createInitialUpdates() {

        List<Player> players = game.getPlayers();
        Update[] initUpdates = new Update[players.size()];

        IslandChange[] islandChanges = new IslandChange[game.getBoard().getNumberOfIslands()];
        for (int n = 0; n < islandChanges.length; n++) islandChanges[n] = new IslandChange(n, game.getBoard().getIsland(n));

        CloudChange[] cloudChanges = new CloudChange[players.size()];
        for (int n = 0; n < cloudChanges.length; n++) cloudChanges[n] = new CloudChange(n, game.getBoard().getCloud(n));

        SchoolChange[] schoolChanges = new SchoolChange[players.size()];
        for (int n = 0; n < players.size(); n++) schoolChanges[n] = new SchoolChange(players.get(n).getSchool());

        PlayerChange[] playerChanges = new PlayerChange[players.size()];
        for (int n = 0; n < players.size(); n++) playerChanges[n] = new PlayerChange(players.get(n));

        for (int n = 0; n < initUpdates.length; n++) {

            initUpdates[n] = new Update();
            for (IslandChange islandChange : islandChanges) initUpdates[n].addChange(islandChange);
            for (CloudChange cloudChange : cloudChanges) initUpdates[n].addChange(cloudChange);
            for (SchoolChange schoolChange : schoolChanges) initUpdates[n].addChange(schoolChange);
            for (PlayerChange playerChange : playerChanges) initUpdates[n].addChange(playerChange);
        }

        for (int n = 0; n < players.size(); n++) {

            Player player = players.get(n);

            HelperCardsChange helperCardsChange = new HelperCardsChange(player.username);
            for(int i = 0; i < player.getNumberOfHelpers(); i++) helperCardsChange.addHelperCard(player.getHelperCard(i));
            initUpdates[n].addChange(helperCardsChange);
        }

        return initUpdates;
    }

    @Override
    public void playGame() throws IOException, InterruptedException {

        while (!game.isGameEnding()) {

            if (!(game.getInfluenceCalculator() instanceof InfluenceCalculatorBasic))
                game.setInfluenceCalculator(new InfluenceCalculatorBasic());

            fillClouds();
            playHelpers();

            game.sortPlayersBasedOnHelperCard();

            playRound();
        }
        endGame();
    }

    protected void playRound() throws IOException, InterruptedException {

        for (Player player : game.getPlayers()) {
            playTurn(player);
        }
    }

    protected void playTurn(Player player) throws IOException, InterruptedException {

        game.setCurrentPlayer(player);

        requestStudents(player);

        requestMotherNature(player);

        checkIslandInfluence();

        requestMove(new ChooseCloudRequest(), player.username);
    }

    protected void requestStudents(Player player) throws IOException, InterruptedException {

        for (int studentMove = 0; studentMove < 3; studentMove++)
            requestMove(new MoveStudentRequest(player.getSchool().getAvailableEntranceColors()), player.username);
    }

    protected void requestMotherNature(Player player) throws IOException, InterruptedException {

        requestMove(new MoveMotherNatureRequest(player.getCurrentHelper().movement), player.username);
    }

    private void fillClouds() throws IOException {

        Update update = new Update();

        for (int cloudIndex = 0; cloudIndex < game.getPlayers().size(); cloudIndex++) {

            CloudTile cloud = game.getBoard().getCloud(cloudIndex);

            for (int i = 0; i < 3; i++) {
                if (game.getStudentsBag().isEmpty()) {
                    game.setGameEnding();
                    return;
                }
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
            if (game.getCurrentPlayer().getNumberOfHelpers() == 0) {
                game.setGameEnding();
            }
        }
    }

     protected void checkIslandInfluence() throws IOException {

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

        // If there's a team controlling the island ,and it's not the new team who will control the island, remove the old tower
        if (island.getTeam().isPresent()) {

            Player currentControllingTeamLeader = island.getTeam().get().getLeader();
            for (int i = 0; i < islandSize; i++) currentControllingTeamLeader.getSchool().addTower();

            SchoolChange oldSchoolChange = new SchoolChange(currentControllingTeamLeader.getSchool());
            update.addChange(oldSchoolChange);
        }

        for (int i = 0; i < islandSize; i++) {
            dominantTeamLeader.getSchool().removeTower();
            if (dominantTeamLeader.getSchool().getTowers() == 0) {
                game.setGameEnding();
                endGame();
                return;
            }
        }
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
        if (game.getBoard().getNumberOfIslands() <= 3) {
            game.setGameEnding();
            endGame();
        }

        server.sendToAllClients(new UpdateMessage(update));
    }

    protected void requestMove(MoveRequest request, String playerUsername) throws IOException, InterruptedException {

        MoveRequestMessage requestMessage = new MoveRequestMessage(request);
        server.sendToClient(requestMessage, playerUsername);
        requestMessage.waitForValidResponse();
    }

    public void managePerformedMoveMessage(PerformedMoveMessage performedMoveMessage) throws IOException, InterruptedException {

        synchronized (this) {
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

    private void endGame() throws IOException {
        Team winnerTeam = game.checkWinner();
        server.sendToAllClients(new GameEndedMessage(winnerTeam));
    }
}
