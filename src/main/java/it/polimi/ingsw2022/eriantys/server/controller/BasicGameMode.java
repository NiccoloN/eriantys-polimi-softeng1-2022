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
import it.polimi.ingsw2022.eriantys.server.model.board.Board;
import it.polimi.ingsw2022.eriantys.server.model.board.CloudTile;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBasic;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class BasicGameMode implements GameMode, Serializable {

    protected final Game game;
    protected final EriantysServer server;
    protected GamePhase currentGamePhase;
    private boolean endGameNow;

    public BasicGameMode(Game game) {

        this.game = game;
        server = EriantysServer.getInstance();
        currentGamePhase = GamePhase.STARTING_ROUND;
        endGameNow = false;
    }

    public Update[] createInitialUpdates() {

        Board board = game.getBoard();
        List<Player> players = game.getPlayersStartOrder();
        Update[] initUpdates = new Update[players.size()];

        IslandChange islandChange = new IslandChange(board.getIslands(), board.getIslandTiles());

        CloudChange[] cloudChanges = new CloudChange[players.size()];
        for (int n = 0; n < cloudChanges.length; n++) cloudChanges[n] = new CloudChange(n, board.getCloud(n));

        SchoolChange[] schoolChanges = new SchoolChange[players.size()];
        for (int n = 0; n < players.size(); n++) schoolChanges[n] = new SchoolChange(players.get(n).getSchool());

        PlayerChange[] playerChanges = new PlayerChange[players.size()];
        for (int n = 0; n < players.size(); n++) playerChanges[n] = new PlayerChange(players.get(n));

        for (int n = 0; n < initUpdates.length; n++) {

            initUpdates[n] = new Update();
            initUpdates[n].addChange(islandChange);
            for (CloudChange cloudChange : cloudChanges) initUpdates[n].addChange(cloudChange);
            for (SchoolChange schoolChange : schoolChanges) initUpdates[n].addChange(schoolChange);
            for (PlayerChange playerChange : playerChanges) initUpdates[n].addChange(playerChange);
        }

        for(Player player : players) {

            HelperCardsChange helperCardsChange = new HelperCardsChange(player.getUsername());
            for(int i = 0; i < player.getNumberOfHelpers(); i++) helperCardsChange.addHelperCard(player.getHelperCard(i));
            helperCardsChange.setPlayedHelperCard(player.getPrevHelper());

            for(Update initUpdate : initUpdates) initUpdate.addChange(helperCardsChange);
        }

        return initUpdates;
    }

    @Override
    public void playGame() throws IOException, InterruptedException {

        while (!game.isGameEnding() && !endGameNow) playRound();
        endGame();
    }

    protected void playRound() throws IOException, InterruptedException {

        if (currentGamePhase == GamePhase.STARTING_ROUND) {

            if (!(game.getInfluenceCalculator() instanceof InfluenceCalculatorBasic))
                game.setInfluenceCalculator(new InfluenceCalculatorBasic());

            fillClouds();

            currentGamePhase = GamePhase.PLAYING_HELPERS;
            game.setCurrentPlayer(game.getPlayers().get(0));
            server.saveGame();
        }

        if (currentGamePhase == GamePhase.PLAYING_HELPERS) {

            playHelpers();
            game.sortPlayersBasedOnHelperCard();

            server.sendToAllClients(new MoveRequestMessage(new WaitRequest()));

            currentGamePhase = GamePhase.MOVING_STUDENTS;
            game.setCurrentPlayer(game.getPlayers().get(0));
            server.saveGame();
        }

        for (int n = 0; n < game.getPlayers().size(); n++) {

            Player player = game.getPlayers().get(n);
            if (game.getCurrentPlayer() == player) playTurn(player, n);
        }
    }

    private void fillClouds() throws IOException {

        Update update = new Update();

        for (int cloudIndex = 0; cloudIndex < game.getPlayers().size(); cloudIndex++) {

            CloudTile cloud = game.getBoard().getCloud(cloudIndex);

            for (int i = 0; i < 3; i++) {

                if (game.getStudentsBag().isEmpty()) {

                    game.setGameEnding();
                    break;
                }

                ColoredPawn student = game.getStudentsBag().extractRandomStudent();
                cloud.addStudent(student);
            }

            update.addChange(new CloudChange(cloudIndex, cloud));
        }

        server.sendToAllClients(new UpdateMessage(update));
    }

    private void playHelpers() throws IOException, InterruptedException {

        for(Player player : game.getPlayers()) player.resetCurrentHelper();
        server.sendToAllClients(new MoveRequestMessage(new WaitRequest()));

        List<Integer> unplayableIndices = new ArrayList<>(3);
        for (int n = 0; n < game.getPlayers().size(); n++) {

            Player player = game.getPlayers().get(n);

            if (game.getCurrentPlayer() == player) {

                unplayableIndices.clear();
                for(Player other : game.getPlayers())
                    if(other != player && other.getCurrentHelper() != null)
                        unplayableIndices.add(other.getCurrentHelper().index);

                if(unplayableIndices.size() >= player.getNumberOfHelpers()) unplayableIndices.clear();
                requestMove(new ChooseHelperCardRequest(unplayableIndices), player.getUsername());
                server.sendToClient(new MoveRequestMessage(new WaitRequest()), player.getUsername());

                if (game.getCurrentPlayer().getNumberOfHelpers() == 0) game.setGameEnding();

                game.setCurrentPlayer(n + 1 < game.getPlayers().size() ? game.getPlayers().get(n + 1) : null);
                server.saveGame();
            }
        }
    }

    protected void playTurn(Player player, int playerIndex) throws IOException, InterruptedException {

        if (currentGamePhase == GamePhase.MOVING_STUDENTS) {

            for(int studentMove = 0; studentMove < 3; studentMove++) requestStudent(player);
            currentGamePhase = GamePhase.MOVING_MOTHER_NATURE;
            server.saveGame();
        }

        if (currentGamePhase == GamePhase.MOVING_MOTHER_NATURE) {

            requestMotherNature(player);
            checkIslandInfluence();
            currentGamePhase = GamePhase.CHOOSING_CLOUD;
            server.saveGame();
        }

        if(currentGamePhase == GamePhase.CHOOSING_CLOUD && !game.isGameEnding()) {

            requestMove(new ChooseCloudRequest(), player.getUsername());

            if (playerIndex + 1 < game.getPlayers().size()) {

                currentGamePhase = GamePhase.MOVING_STUDENTS;
                game.setCurrentPlayer(game.getPlayers().get(playerIndex + 1));
                server.saveGame();
            }
            else {

                currentGamePhase = GamePhase.STARTING_ROUND;
                game.setCurrentPlayer(null);
                server.saveGame();
            }
        }

        if(playerIndex < game.getPlayers().size() - 1)
            server.sendToClient(new MoveRequestMessage(new WaitRequest()), player.getUsername());
    }

    protected void requestStudent(Player player) throws IOException, InterruptedException {

        requestMove(new MoveStudentRequest
                        (player.getSchool().getAvailableEntranceColors(), List.of(ColoredPawnOriginDestination.ISLAND, ColoredPawnOriginDestination.TABLE)),
                player.getUsername());
    }

    protected void requestMotherNature(Player player) throws IOException, InterruptedException {

        requestMove(new MoveMotherNatureRequest(player.getCurrentHelper().movement), player.getUsername());
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

    protected void updateTowers(Team dominantTeam, CompoundIslandTile island) throws IOException {

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
            if (dominantTeamLeader.getSchool().getTowers() == 0) break;
        }
        SchoolChange newSchoolChange = new SchoolChange(dominantTeamLeader.getSchool());
        update.addChange(newSchoolChange);

        island.setTeam(dominantTeam);
        IslandChange islandChange = checkForMerge(island);
        update.addChange(islandChange);

        server.sendToAllClients(new UpdateMessage(update));

        if (dominantTeamLeader.getSchool().getTowers() == 0) {

            game.setGameEnding();
            endGameNow = true;
            endGame();
            return;
        }

        if (game.getBoard().getNumberOfIslands() <= 3) {

            game.setGameEnding();
            endGameNow = true;
            endGame();
        }
    }

    private IslandChange checkForMerge(CompoundIslandTile island) {

        Board board = game.getBoard();
        int islandIndex = island.getIndex();

        int nextIslandIndex = (islandIndex + 1) % board.getNumberOfIslands();
        CompoundIslandTile nextIsland = board.getIsland(nextIslandIndex);
        if (nextIsland.getTeam().equals(island.getTeam())) board.mergeIslands(islandIndex, nextIslandIndex);

        islandIndex = island.getIndex();
        int previousIslandIndex = islandIndex - 1;
        if(previousIslandIndex < 0) previousIslandIndex = board.getNumberOfIslands() - 1;
        CompoundIslandTile previousIsland = board.getIsland(previousIslandIndex);
        if (previousIsland.getTeam().equals(island.getTeam())) board.mergeIslands(islandIndex, previousIslandIndex);

        return new IslandChange(board.getIslands(), board.getIslandTiles());
    }

    protected void requestMove(MoveRequest request, String playerUsername) throws IOException, InterruptedException {

        if(!endGameNow) {

            MoveRequestMessage requestMessage = new MoveRequestMessage(request);
            server.sendToClient(requestMessage, playerUsername);
            requestMessage.waitForValidResponse();
        }
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
                        game.getCurrentPlayer().getUsername());
            }
        }
    }

    private void endGame() throws IOException {

        Team winnerTeam = game.checkWinner();
        server.sendToAllClients(new GameEndedMessage(winnerTeam));
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    public void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    public boolean getEndGameNow() {
        return endGameNow;
    }

    public void setEndGameNow(boolean endGameNow) {
        this.endGameNow = endGameNow;
    }
}
