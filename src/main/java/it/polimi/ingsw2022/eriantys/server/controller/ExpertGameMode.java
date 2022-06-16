package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
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
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.io.IOException;
import java.util.*;

public class ExpertGameMode extends BasicGameMode {

    public ExpertGameMode(Game game) {

        super(game);

        game.resetCharacterUses();

        for(int i=0; i<game.getNumberOfCharacters(); i++){

            CharacterCard currentCharacter = game.getCharacter(i);

            switch(currentCharacter.index) {

                case 1:
                case 11:
                    for(int k=0; k<4; k++) currentCharacter.addStudent(game.getStudentsBag().extractRandomStudent());
                    break;
                case 5:
                    for(int k=0; k<4; k++) currentCharacter.incrementDenyTiles();
                    break;
                case 7:
                    for(int k=0; k<6; k++) currentCharacter.addStudent(game.getStudentsBag().extractRandomStudent());
                    break;
                default: break;
            }
        }
    }

    @Override
    public Update[] createInitialUpdates() {

        Update[] initialUpdate = super.createInitialUpdates();

        CharacterCardsChange characterCardsChange = new CharacterCardsChange();
        for(int n = 0; n < game.getNumberOfCharacters(); n++) characterCardsChange.addCharacterCard(game.getCharacter(n));

        for (Update update : initialUpdate) {

            update.addChange(characterCardsChange);
        }

        return(initialUpdate);
    }

    @Override
    public void playRound() throws IOException, InterruptedException {

        game.resetCharacterUses();
        super.playRound();
    }

    @Override
    public void playTurn(Player player) throws IOException, InterruptedException {

        super.playTurn(player);
        game.setInfluenceCalculator(new InfluenceCalculatorBasic());
        MoveMotherNatureRequest.setAdditionalSteps(false);
    }

    private void playCharacter(int cardIndex) throws IOException, InterruptedException {

        CharacterCard characterCard = game.getCharacterOfIndex(cardIndex);
        switch(cardIndex) {

            case 1:
                requestMove(new MoveStudentRequest(
                                characterCard.index,
                                characterCard.getStudentsColors(),
                                List.of(ColoredPawnOriginDestination.ISLAND),
                                "Move a student from the character card to an island")
                        , game.getCurrentPlayer().username);
                break;
            case 3:
                requestMove(new ChooseIslandRequest(
                        cardIndex,
                        "Select the island on which influence will be calculated."),
                        game.getCurrentPlayer().username);

                Optional<Team> dominantTeam = game.getInfluenceCalculator().calculateInfluence
                        (game.getPlayers(),game.getBoard().getIsland(game.getCharacterIsland()), game.getCurrentPlayer());
                if(dominantTeam.isPresent()) updateTowers(dominantTeam.get(), game.getBoard().getIsland(game.getCharacterIsland()));

                break;
            case 4:
                MoveMotherNatureRequest.setAdditionalSteps(true);
                break;
            case 5:
                requestMove(new ChooseIslandRequest(
                                cardIndex,
                                "Select the island on which putting the deny card."),
                        game.getCurrentPlayer().username);
                break;
            case 7:
                for(int i=0; i<3 ; i++) {

                    if(!game.getAbortMessageReceived()) {
                        requestMove(
                                new ChooseColorRequest
                                        (
                                                cardIndex,
                                                characterCard.getStudentsColors(),
                                                ColoredPawnOriginDestination.CHARACTER,
                                    "Select a student from the character card, or press ESC to stop the effect."
                                        ),
                                game.getCurrentPlayer().username);

                        if(!game.getAbortMessageReceived()) {
                            requestMove(
                                    new ChooseColorRequest
                                            (
                                                    cardIndex,
                                                    game.getCurrentPlayer().getSchool().getAvailableEntranceColors(),
                                                    ColoredPawnOriginDestination.ENTRANCE,
                                                    "Select now a student from your school entrance."
                                            ),
                                    game.getCurrentPlayer().username );

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
            case 9:
                requestMove(new ChooseColorRequest
                        (
                                cardIndex,
                                List.of(PawnColor.values()),
                                null,
                                "Select a color that will not be considered during this turn's influence calculation."
                        ), game.getCurrentPlayer().username);
                break;
            case 10:
                for(int i=0; i<2 ; i++) {

                    if (!game.getAbortMessageReceived()) {

                        requestMove(
                                new ChooseColorRequest
                                        (
                                                cardIndex,
                                                game.getCurrentPlayer().getSchool().getAvailableTableColors(),
                                                ColoredPawnOriginDestination.TABLE,
                                                "Select a student from the student tables of your school, or press ESC to stop the effect."
                                        ),
                                game.getCurrentPlayer().username);

                        if (!game.getAbortMessageReceived()) {
                            requestMove(
                                    new ChooseColorRequest
                                            (
                                                    cardIndex,
                                                    game.getCurrentPlayer().getSchool().getAvailableEntranceColors(),
                                                    ColoredPawnOriginDestination.ENTRANCE,
                                                    "Select now a student from your school entrance."
                                            ),
                                    game.getCurrentPlayer().username);

                            try {
                                game.getCurrentPlayer().getSchool().addToTable(game.getCurrentPlayer().getSchool().removeFromEntrance(
                                        game.getExchange(ColoredPawnOriginDestination.ENTRANCE)));
                            } catch (RuntimeException e) {
                                server.sendToClient(new InvalidMoveMessage
                                        (null, null, "Already reached maximum students in the table"), game.getCurrentPlayer().username);
                                return;
                            }

                            game.getCurrentPlayer().getSchool().addToEntrance(game.getCurrentPlayer().getSchool().removeFromTable(
                                    game.getExchange(ColoredPawnOriginDestination.TABLE)));

                            ChooseColor chooseColorUpdate = new ChooseColor(null, cardIndex, null);

                            server.sendToAllClients(new UpdateMessage(chooseColorUpdate.getUpdate(game)));
                            game.resetExchanges();
                        }
                    }
                }

                game.setAbortMessageReceived(false);
                break;
            case 11:
                requestMove(
                        new ChooseColorRequest
                                (
                                        cardIndex,
                                        game.getCharacterOfIndex(cardIndex).getStudentsColors(),
                                        ColoredPawnOriginDestination.CHARACTER,
                                        "Select a student from the character card."
                                ),
                        game.getCurrentPlayer().username);

                game.getCurrentPlayer().getSchool().addToTable(game.getCharacterOfIndex(cardIndex).getStudent(
                        game.getExchange(ColoredPawnOriginDestination.CHARACTER)));

                ChooseColor chooseColorUpdate = new ChooseColor(null, cardIndex, null);
                server.sendToAllClients(new UpdateMessage(chooseColorUpdate.getUpdate(game)));

                game.resetExchanges();
                break;
            case 12:
                requestMove(
                        new ChooseColorRequest
                                (
                                        cardIndex,
                                        List.of(PawnColor.values()),
                                        null,
                                        "Select a color."
                                ),
                        game.getCurrentPlayer().username);

                MoveStudent moveStudentUpdate = new MoveStudent(ColoredPawnOriginDestination.TABLE, null, 0, null);
                server.sendToAllClients(new UpdateMessage(moveStudentUpdate.getUpdate(game)));
                break;
        }
    }

    @Override
    protected void requestStudents(Player player) throws IOException, InterruptedException {

        for (int studentMove = 0; studentMove < 3; studentMove++) {

            requestMove(new MoveStudentRequest
                    (
                            player.getSchool().getAvailableEntranceColors(),
                            List.of(ColoredPawnOriginDestination.ISLAND, ColoredPawnOriginDestination.TABLE)),
                    player.username);
        }
    }

    @Override
    protected void checkIslandInfluence() throws IOException {

        CompoundIslandTile motherNatureIsland = game.getBoard().getMotherNatureIsland();

        if(motherNatureIsland.getNumberOfDenyCards() > 0) {

            motherNatureIsland.decrementNumberOfDenyCards();
            game.getCharacterOfIndex(5).incrementDenyTiles();
        }
        else super.checkIslandInfluence();
    }

    @Override
    public void managePerformedMoveMessage(PerformedMoveMessage performedMoveMessage) throws IOException, InterruptedException {

        MoveRequestMessage previousMessage = performedMoveMessage.getPreviousMessage();

        if(performedMoveMessage.move instanceof ChooseCharacterCard) {

            ChooseCharacterCard move = (ChooseCharacterCard) performedMoveMessage.move;

            new Thread(() -> {
                try {
                    if (move.isValid(game)) {

                        playCharacter(move.characterCardIndex);
                        performedMoveMessage.move.apply(game);
                        server.sendToAllClients(new UpdateMessage(move.getUpdate(game)));
                    }
                    else {
                        server.sendToClient(
                                new InvalidMoveMessage(performedMoveMessage, performedMoveMessage.getPreviousMessage(), "The chosen character card does not exist."),
                                game.getCurrentPlayer().username);
                    }

                    if(previousMessage.moveRequest instanceof MoveMotherNatureRequest) {
                        requestMotherNature(game.getCurrentPlayer());
                    }
                    else requestMove(previousMessage.moveRequest, game.getCurrentPlayer().username);
                    previousMessage.acceptResponse();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        else if(performedMoveMessage.move instanceof Abort) {
            new Thread( () -> {
                if(performedMoveMessage.move.isValid(game)) {

                    performedMoveMessage.move.apply(game);
                }
                previousMessage.acceptResponse();
            }).start();
        }
        else super.managePerformedMoveMessage(performedMoveMessage);
    }
}
