package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.server.EriantysServer;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorBonus;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoTowers;

import java.io.Serializable;

/**
 * This class represents the choice of a Character by a player
 * @author Emanuele Musto
 */
public class ChooseCharacterCard extends Move {

    public final int characterCardIndex;

    public ChooseCharacterCard(int characterCardIndex) {

        this.characterCardIndex = characterCardIndex;
    }

    @Override
    public boolean isValid(Game game) {

        return game.getCurrentPlayer().getCoins() >= game.getCharacterOfIndex(characterCardIndex).getCost() &&
                game.getCharacterUses(game.getCurrentPlayer().username) == 0;
    }

    @Override
    public void apply(Game game) {

        CharacterCard characterCard = null;
        if(characterCardIndex >= 1 && characterCardIndex <= 12) characterCard = game.getCharacterOfIndex(characterCardIndex);

        switch(characterCardIndex) {

            case 1:
                characterCard.addStudent(game.getStudentsBag().extractRandomStudent());
                characterCard.incrementCost();
                game.incrementCharacterUses(game.getCurrentPlayer().username);
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
            default: break;
        }
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        CharacterCardsChange change = new CharacterCardsChange();

        for(int i=0; i<game.getNumberOfCharacters(); i++){
            change.addCharacterCard(game.getCharacter(i));
        }

        update.addChange(change);

        return update;
    }
}
