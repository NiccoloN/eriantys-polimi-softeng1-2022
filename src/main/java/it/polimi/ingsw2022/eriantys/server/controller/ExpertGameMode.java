package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseCharacterCard;
import it.polimi.ingsw2022.eriantys.messages.moves.Move;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.InvalidMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toClient.UpdateMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBonus;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoColor;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoTowers;

import java.io.IOException;

public class ExpertGameMode extends BasicGameMode {

    public ExpertGameMode(Game game) {

        super(game);

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

    private void playCharacter(PerformedMoveMessage characterMoveMessage) throws IOException, InterruptedException {

        ChooseCharacterCard move = (ChooseCharacterCard) characterMoveMessage.move;
        int cardIndex = move.characterCardIndex;
        CharacterCard characterCard = null;

        if(cardIndex >= 1 && cardIndex <= 12) characterCard = game.getCharacterOfIndex(move.characterCardIndex);

        switch(cardIndex) {

            case 1:
                requestMove(new MoveStudentRequest(
                            characterCard.index,
                            characterCard.getStudentsColors(),
                            "Move a student from the character card to an island")
                        , game.getCurrentPlayer().username);
                characterCard.addStudent(game.getStudentsBag().extractRandomStudent());
                characterCard.incrementCost();
                break;
            case 2:
                //TODO dare professori al current player anche se ha lo stesso numero di studenti, sia subito che
                // quando muove nuovi studenti in dining room (Si può fare settando un boolean in game e successivamente
                // chiamando la funzione che fa l'update dei professori che deve tenere conto di questo boolean)
                break;
            case 3:
                //TODO mandare richiesta di selezione di un'isola (MoveRequest da creare) e ricevere una SelectIsland
                // (Move da creare) che conterrà l'indice dell'isola scelta. Successivamente chiamare il calcolo
                // dell'influence sull'isola scelta
                break;
            case 4:
                //TODO aggiungere 2 ai max step mandati nella request MoveMotherNature in questo turno
                break;
            case 5:
                //TODO mandare richiesta di selezione di un'isola (MoveRequest da creare) e rievere una SelectIsland
                // (Move da creare) che conterrà l'indice dell'isola scelta. Successivamente settare come denied
                // l'isola scelta
                break;
            case 6:
                game.setInfluenceCalculator(new InfluenceCalculatorNoTowers());
                break;
            case 7:
                //TODO (per 3 volte) mandare 2 richieste di selezione colore (Request da creare).
                // Successivamente a ogni 2 richieste scambiare uno studente del primo colore selezionato che si trova
                // sulla carta con uno del secondo colore selezionato che si trova nell'entrance. Se si riceve
                // un messaggio di abort si smette di richiedere
                break;
            case 8:
                game.setInfluenceCalculator(new InfluenceCalculatorBonus());
                break;
            case 9:
                //TODO mandare una richiesta di selezione colore (Request da creare).
                // game.setInfluenceCalculator(new InfluenceCalculatorNoColor( colore selezionato ));
                break;
            case 10:
                //TODO (per 2 volte) mandare 2 richieste di selezione colore (Request da creare).
                // Successivamente a ogni 2 richieste scambiare uno studente del primo colore selezionato che si trova
                // nell'entrance con uno del secondo colore selezionato che si trova nella dining room. Se si riceve
                // un messaggio di abort si smette di richiedere
                break;
            case 11:
                //TODO mandare richiesta di MoveStudent dalla carta alla dining room (request da modificare)
                //requestMove(new MoveStudentRequest(characterCard.index, characterCard.getStudentsColors()), game.getCurrentPlayer().username);
                characterCard.addStudent(game.getStudentsBag().extractRandomStudent());
                //TODO mandare change del character (da modificare)
                break;
            case 12:
                //TODO mandare una richiesta di selezione colore (Request da creare). Muovere 3 studenti del colore
                // selezionato dalla dining room di ogni studente alla bag
                break;
            default:
                server.sendToClient(new InvalidMoveMessage
                        (characterMoveMessage, characterMoveMessage.getPreviousMessage(), "The chosen character card does not exist."),
                        game.getCurrentPlayer().username);
        }
    }

    @Override
    public synchronized void managePerformedMoveMessage(PerformedMoveMessage performedMoveMessage) throws IOException, InterruptedException {

        if(performedMoveMessage.move instanceof ChooseCharacterCard){

            MoveRequestMessage previousMessage = performedMoveMessage.getPreviousMessage();
            ChooseCharacterCard move = (ChooseCharacterCard) performedMoveMessage.move;

            if (move.isValid(game)) {

                previousMessage.acceptResponse();
                playCharacter(performedMoveMessage);
                server.sendToAllClients(new UpdateMessage(move.getUpdate(game)));
            }

            server.sendToClient(previousMessage, game.getCurrentPlayer().username);
        }
        else super.managePerformedMoveMessage(performedMoveMessage);
    }
}
