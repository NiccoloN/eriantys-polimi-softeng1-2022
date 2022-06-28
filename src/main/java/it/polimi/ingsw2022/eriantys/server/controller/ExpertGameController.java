package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.moves.*;
import it.polimi.ingsw2022.eriantys.messages.requests.*;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBasic;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBonus;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoTowers;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.util.*;

/**
 * This class represents the expert mode of the game, it includes coins and characters.
 * @author Emanuele Musto
 */
public class ExpertGameController extends BasicGameController {

    private MoveRequestMessage currentCharacterMoveRequestMessage;
    private int motherNatureAdditionalSteps;

    /**
     * Initializes the controller in expert mode, using the basic and adding the initialization of characters.
     * @param game the model and all the game's data.
     * @param initialize if true the game is initialized. It's false only when a game is loaded from a save.
     */
    public ExpertGameController(Game game, boolean initialize) {

        super(game);

        if(initialize) {

            currentCharacterMoveRequestMessage = null;
            motherNatureAdditionalSteps = 0;
            game.resetCharacterUses();

            for(int i=0; i<game.getNumberOfCharacters(); i++) {

                CharacterCard currentCharacter = game.getCharacter(i);

                switch(currentCharacter.index) {

                    case 1:
                    case 11:
                        for(int k=0; k<4; k++) currentCharacter.addStudent(game.getStudentsBag().extractRandomStudent());
                        break;
                    case 5:
                        for(int k=0; k<5; k++) currentCharacter.incrementDenyTiles();
                        break;
                    case 7:
                        for(int k=0; k<6; k++) currentCharacter.addStudent(game.getStudentsBag().extractRandomStudent());
                        break;
                    default: break;
                }
            }
        }
    }

    @Override
    public Update[] createInitialUpdates() {

        Update[] initialUpdates = super.createInitialUpdates();

        CharacterCardsChange characterCardsChange = new CharacterCardsChange();
        for(int n = 0; n < game.getNumberOfCharacters(); n++) characterCardsChange.addCharacterCard(game.getCharacter(n));
        for (Update update : initialUpdates) update.addChange(characterCardsChange);

        return(initialUpdates);
    }


    @Override
    public void playRound() throws IOException, InterruptedException {

        if (currentGamePhase == GamePhase.STARTING_ROUND) game.resetCharacterUses();
        super.playRound();
    }

    @Override
    public void playTurn(Player player, int playerIndex) throws IOException, InterruptedException {

        super.playTurn(player, playerIndex);

        if (currentGamePhase == GamePhase.MOVING_STUDENTS || currentGamePhase == GamePhase.STARTING_ROUND) {

            if(!(game.getInfluenceCalculator() instanceof InfluenceCalculatorBasic))
                game.setInfluenceCalculator(new InfluenceCalculatorBasic());

            motherNatureAdditionalSteps = 0;
            server.saveGame();
        }
    }

    @Override
    protected void requestMotherNature(Player player) throws IOException, InterruptedException {

        requestMove(new MoveMotherNatureRequest(player.getCurrentHelper().movement + motherNatureAdditionalSteps,
                !player.hasPlayedCharacter()), player.getUsername());
    }

    private void requestCharacterMove(MoveRequest request, String playerUsername) throws IOException, InterruptedException {

        if(!endGameNow) {

            currentCharacterMoveRequestMessage = new MoveRequestMessage(request);
            server.sendToClient(currentCharacterMoveRequestMessage, playerUsername);
            currentCharacterMoveRequestMessage.waitForValidResponse();
            currentCharacterMoveRequestMessage = null;
        }
    }

    /**
     * This method is called when the client sends a character card move. Based on the index, the right requests and
     * character effects will take place.
     * @param cardIndex the index of the character card used by the player.
     * @throws IOException when output stream throws an exception (while sending a message to client).
     * @throws InterruptedException when the thread waiting for a response by the client is interrupted.
     */
    private void playCharacter(int cardIndex) throws IOException, InterruptedException {

        CharacterCard characterCard = game.getCharacterOfIndex(cardIndex);
        switch(cardIndex) {

            case 1:
                requestCharacterMove(new MoveStudentRequest(
                                characterCard.index,
                                characterCard.getStudentsColors(),
                                List.of(ColoredPawnOriginDestination.ISLAND),
                                "Move a student from the character card to an island"),
                        game.getCurrentPlayer().getUsername());

                characterCard.addStudent(game.getStudentsBag().extractRandomStudent());
                break;
            case 2:
                for(PawnColor color : PawnColor.values()) game.checkAndUpdateProfessor(color, true);
                break;
            case 3:
                requestCharacterMove(new ChooseIslandRequest(
                        cardIndex,
                        "Select the island on which influence will be calculated"),
                        game.getCurrentPlayer().getUsername());

                Optional<Team> dominantTeam = game.getInfluenceCalculator().calculateInfluence
                        (game.getPlayers(),game.getBoard().getIsland(game.getCharacterIsland()), game.getCurrentPlayer());

                if(dominantTeam.isPresent()) updateTowers(dominantTeam.get(), game.getBoard().getIsland(game.getCharacterIsland()));
                break;
            case 4:
                MoveRequest currentRequest = currentMoveRequestMessage.moveRequest;
                if(currentRequest instanceof MoveMotherNatureRequest)
                    ((MoveMotherNatureRequest) currentRequest).setMotherNatureMaxSteps(((MoveMotherNatureRequest) currentRequest).getMotherNatureMaxSteps() + 2);
                else motherNatureAdditionalSteps = 2;
                break;
            case 5:
                requestCharacterMove(new ChooseIslandRequest(
                                cardIndex,
                                "Select the island on which putting the deny card"),
                        game.getCurrentPlayer().getUsername());
                break;
            case 6:
                game.setInfluenceCalculator(new InfluenceCalculatorNoTowers());
                break;
            case 7:
                for(int i = 0; i < 3; i++) {

                    if(!game.getAbortMessageReceived()) {
                        requestCharacterMove(new ChooseColorRequest
                                        (
                                                cardIndex,
                                                characterCard.getStudentsColors(),
                                                ColoredPawnOriginDestination.CHARACTER,
                                    "Select a student from the character card, or press ESC to stop the effect"
                                        ),
                                game.getCurrentPlayer().getUsername());

                        if(!game.getAbortMessageReceived()) {
                            requestCharacterMove(new ChooseColorRequest
                                            (
                                                    cardIndex,
                                                    game.getCurrentPlayer().getSchool().getAvailableEntranceColors(),
                                                    ColoredPawnOriginDestination.ENTRANCE,
                                                    "Select now a student from your school entrance"
                                            ),
                                    game.getCurrentPlayer().getUsername() );

                            game.getCharacterOfIndex(cardIndex).addStudent(
                                    game.getCurrentPlayer().getSchool().removeFromEntrance(game.getExchange(ColoredPawnOriginDestination.ENTRANCE))
                            );

                            ColoredPawn temp = game.getCharacterOfIndex(cardIndex).getStudent(game.getExchange(ColoredPawnOriginDestination.CHARACTER));
                            game.getCharacterOfIndex(cardIndex).removeStudent(temp);
                            game.getCurrentPlayer().getSchool().addToEntrance(temp);

                            ChooseColor makeUpdate = new ChooseColor(null, cardIndex, null);

                            server.sendToAllClients(new UpdateMessage(makeUpdate.getUpdate(game)));
                            game.resetExchanges();
                        }
                    }
                }

                game.setAbortMessageReceived(false);
                break;
            case 8:
                game.setInfluenceCalculator(new InfluenceCalculatorBonus());
                break;
            case 9:
                requestCharacterMove(new ChooseColorRequest
                        (
                                cardIndex,
                                List.of(PawnColor.values()),
                                null,
                                "Select a color that will not be considered during this turn's influence calculation"
                        ), game.getCurrentPlayer().getUsername());
                break;
            case 10:
                for(int i = 0; i < 2 ; i++) {

                    if (!game.getAbortMessageReceived()) {

                        requestCharacterMove(
                                new ChooseColorRequest
                                        (
                                                cardIndex,
                                                game.getCurrentPlayer().getSchool().getAvailableTableColors(),
                                                ColoredPawnOriginDestination.TABLE,
                                                "Select a student from the student tables of your school, or press ESC to stop the effect"
                                        ),
                                game.getCurrentPlayer().getUsername());

                        if (!game.getAbortMessageReceived()) {

                            MoveRequest colorRequest = new ChooseColorRequest(
                                    cardIndex,
                                    game.getCurrentPlayer().getSchool().getAvailableEntranceColors(),
                                    ColoredPawnOriginDestination.ENTRANCE,
                                    "Select now a student from your school entrance");

                            requestCharacterMove(colorRequest, game.getCurrentPlayer().getUsername());

                            try {

                                game.getCurrentPlayer().getSchool().addToTable(game.getCurrentPlayer().getSchool().removeFromEntrance(
                                        game.getExchange(ColoredPawnOriginDestination.ENTRANCE)));
                            }
                            catch (RuntimeException e) {

                                String playerUsername = game.getCurrentPlayer().getUsername();

                                server.sendToClient(new InvalidMoveMessage("Already reached maximum students in the table"), playerUsername);

                                requestCharacterMove(colorRequest, game.getCurrentPlayer().getUsername());
                                return;
                            }

                            game.getCurrentPlayer().getSchool().addToEntrance(game.getCurrentPlayer().getSchool().removeFromTable(
                                    game.getExchange(ColoredPawnOriginDestination.TABLE)));

                            for(PawnColor color: PawnColor.values()) game.checkAndUpdateProfessor(color, false);

                            ChooseColor chooseColorUpdate = new ChooseColor(null, cardIndex, null);

                            server.sendToAllClients(new UpdateMessage(chooseColorUpdate.getUpdate(game)));
                            game.resetExchanges();
                        }
                    }
                }

                game.setAbortMessageReceived(false);
                break;
            case 11:
                requestCharacterMove(
                        new ChooseColorRequest
                                (
                                        cardIndex,
                                        game.getCharacterOfIndex(cardIndex).getStudentsColors(),
                                        ColoredPawnOriginDestination.CHARACTER,
                                        "Select a student from the character card"
                                ),
                        game.getCurrentPlayer().getUsername());

                CharacterCard tempCharacter = game.getCharacterOfIndex(cardIndex);
                ColoredPawn temp = tempCharacter.getStudent(game.getExchange(ColoredPawnOriginDestination.CHARACTER));
                tempCharacter.removeStudent(temp);
                game.getCurrentPlayer().getSchool().addToTable(temp);

                for(PawnColor color: PawnColor.values()) game.checkAndUpdateProfessor(color, false);

                ChooseColor chooseColorUpdate = new ChooseColor(null, cardIndex, null);
                server.sendToAllClients(new UpdateMessage(chooseColorUpdate.getUpdate(game)));

                game.resetExchanges();

                characterCard.addStudent(game.getStudentsBag().extractRandomStudent());
                break;
            case 12:
                requestCharacterMove(
                        new ChooseColorRequest
                                (
                                        cardIndex,
                                        List.of(PawnColor.values()),
                                        null,
                                        "Select a color"
                                ),
                        game.getCurrentPlayer().getUsername());

                MoveStudent moveStudentUpdate = new MoveStudent(ColoredPawnOriginDestination.TABLE, null, 0, null);
                server.sendToAllClients(new UpdateMessage(moveStudentUpdate.getUpdate(game)));
                break;
        }
    }

    @Override
    protected void checkIslandInfluence() throws IOException {

        CompoundIslandTile motherNatureIsland = game.getBoard().getMotherNatureIsland();

        if(motherNatureIsland.getNumberOfDenyTiles() > 0) {

            motherNatureIsland.decrementNumberOfDenyTiles();
            game.getCharacterOfIndex(5).incrementDenyTiles();

            Update update = new Update();
            update.addChange(new IslandChange(game.getBoard().getIslands(), game.getBoard().getIslandTiles()));
            CharacterCardsChange characterCardsChange = new CharacterCardsChange();
            for(int i = 0; i < game.getNumberOfCharacters(); i++) characterCardsChange.addCharacterCard(game.getCharacter(i));
            server.sendToAllClients(new UpdateMessage(update));
        }
        else super.checkIslandInfluence();
    }

    @Override
    public void managePerformedMoveMessage(PerformedMoveMessage performedMoveMessage) throws IOException, InterruptedException {

        MoveRequestMessage previousMessage = performedMoveMessage.getPreviousMessage();

        if(performedMoveMessage.move instanceof ChooseCharacterCard && currentCharacterMoveRequestMessage == null) {

            ChooseCharacterCard move = (ChooseCharacterCard) performedMoveMessage.move;

            new Thread(() -> {
                try {

                    String playerUsername = game.getCurrentPlayer().getUsername();

                    if (move.isValid(game, currentMoveRequestMessage.moveRequest)) {

                        performedMoveMessage.move.apply(game);
                        currentMoveRequestMessage.moveRequest.setCanPlayCharacter(false);

                        playCharacter(move.characterCardIndex);

                        server.sendToAllClients(new UpdateMessage(move.getUpdate(game)));
                    }

                    else server.sendToClient(new InvalidMoveMessage(move.getErrorMessage()), playerUsername);

                    MoveRequestMessage newRequestMessage = new MoveRequestMessage(currentMoveRequestMessage.moveRequest);
                    server.sendToClient(newRequestMessage, playerUsername);
                    newRequestMessage.waitForValidResponse();

                    currentMoveRequestMessage.acceptResponse();
                }
                catch (IOException | InterruptedException e) {

                    e.printStackTrace();
                }
            }).start();
        }

        else if(performedMoveMessage.move instanceof Abort) {

            new Thread(() -> {

                if(performedMoveMessage.move.isValid(game, currentCharacterMoveRequestMessage.moveRequest)) performedMoveMessage.move.apply(game);
                previousMessage.acceptResponse();
            }).start();
        }

        else if(currentCharacterMoveRequestMessage != null) {

            Move move = performedMoveMessage.move;

            if (move.isValid(game, currentCharacterMoveRequestMessage.moveRequest)) {

                move.apply(game);
                server.sendToAllClients(new UpdateMessage(move.getUpdate(game)));
                performedMoveMessage.getPreviousMessage().acceptResponse();
            }
            else {

                String playerUsername = game.getCurrentPlayer().getUsername();

                server.sendToClient(new InvalidMoveMessage(move.getErrorMessage()), playerUsername);
                server.sendToClient(currentCharacterMoveRequestMessage, playerUsername);
            }
        }

        else super.managePerformedMoveMessage(performedMoveMessage);
    }
}
