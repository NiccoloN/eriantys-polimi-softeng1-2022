package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseCharacterCard;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseColor;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.requests.*;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBasic;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.IOException;
import java.util.*;

public class ExpertGameMode extends BasicGameMode {

    private boolean additionalMotherNatureSteps = false;

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
    }

    private void playCharacter(int cardIndex) throws IOException, InterruptedException {

        CharacterCard characterCard = game.getCharacterOfIndex(cardIndex);
        switch(cardIndex) {

            case 1:
                requestMove(new MoveStudentRequest(
                            characterCard.index,
                            characterCard.getStudentsColors(),
                            "Move a student from the character card to an island")
                        , game.getCurrentPlayer().username);
                break;
            case 3:
                requestMove(new ChooseIslandRequest(
                        cardIndex,
                        "Select the island on which influence will be calculated."),
                        game.getCurrentPlayer().username);
                break;
            case 4:
                additionalMotherNatureSteps = true;
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
                                                CharactersColorOrigin.FROM_CHARACTER,
                                    "Select a student from the character card, or press ESC to stop the effect."
                                        ),
                                game.getCurrentPlayer().username);

                        if(!game.getAbortMessageReceived()) {
                            requestMove(
                                    new ChooseColorRequest
                                            (
                                                    cardIndex,
                                                    game.getCurrentPlayer().getSchool().getAvailableEntranceColors(),
                                                    CharactersColorOrigin.FROM_ENTRANCE,
                                                    "Select now a student from your school entrance."
                                            ),
                                    game.getCurrentPlayer().username );

                            game.getCharacterOfIndex(cardIndex).addStudent(
                                    game.getCurrentPlayer().getSchool().removeFromTable(game.getExchange(CharactersColorOrigin.FROM_ENTRANCE))
                            );
                            game.getCurrentPlayer().getSchool().addToEntrance(
                                    game.getCharacterOfIndex(cardIndex).getStudent(game.getExchange(CharactersColorOrigin.FROM_CHARACTER))
                            );

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
                                                CharactersColorOrigin.FROM_TABLE,
                                                "Select a student from the student tables of your school, or press ESC to stop the effect."
                                        ),
                                game.getCurrentPlayer().username);

                        if (!game.getAbortMessageReceived()) {
                            requestMove(
                                    new ChooseColorRequest
                                            (
                                                    cardIndex,
                                                    game.getCurrentPlayer().getSchool().getAvailableEntranceColors(),
                                                    CharactersColorOrigin.FROM_ENTRANCE,
                                                    "Select now a student from your school entrance."
                                            ),
                                    game.getCurrentPlayer().username);

                            try {
                                game.getCurrentPlayer().getSchool().addToTable(game.getCurrentPlayer().getSchool().removeFromEntrance(
                                        game.getExchange(CharactersColorOrigin.FROM_ENTRANCE)));
                            } catch (RuntimeException e) {
                                server.sendToClient(new InvalidMoveMessage
                                        (null, null, "Already reached maximum students in the table"), game.getCurrentPlayer().username);
                                return;
                            }

                            game.getCurrentPlayer().getSchool().addToEntrance(game.getCurrentPlayer().getSchool().removeFromTable(
                                    game.getExchange(CharactersColorOrigin.FROM_TABLE)));

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
                                        CharactersColorOrigin.FROM_CHARACTER,
                                        "Select a student from the character card."
                                ),
                        game.getCurrentPlayer().username);

                game.getCurrentPlayer().getSchool().addToTable(game.getCharacterOfIndex(cardIndex).getStudent(
                        game.getExchange(CharactersColorOrigin.FROM_CHARACTER)));

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

                MoveStudent moveStudentUpdate = new MoveStudent(false, 0, null);
                server.sendToAllClients(new UpdateMessage(moveStudentUpdate.getUpdate(game)));
                break;
        }
    }

    @Override
    protected void requestStudents(Player player) throws IOException, InterruptedException {

        for (int studentMove = 0; studentMove < 3; studentMove++) {

            requestMove(new MoveStudentRequest(player.getSchool().getAvailableEntranceColors()), player.username);
            //TODO checkCoins();
        }
    }

    @Override
    public void requestMotherNature(Player player) throws IOException, InterruptedException {

        if(additionalMotherNatureSteps) {
            requestMove(new MoveMotherNatureRequest(player.getCurrentHelper().movement + 2), player.username);
        }
        else super.requestMotherNature(player);

        additionalMotherNatureSteps = false;
    }

    @Override
    public void managePerformedMoveMessage(PerformedMoveMessage performedMoveMessage) throws IOException, InterruptedException {

        if(performedMoveMessage.move instanceof ChooseCharacterCard) {

            MoveRequestMessage previousMessage = performedMoveMessage.getPreviousMessage();

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

                    requestMove(previousMessage.moveRequest, game.getCurrentPlayer().username);
                    previousMessage.acceptResponse();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        else super.managePerformedMoveMessage(performedMoveMessage);
    }
}
