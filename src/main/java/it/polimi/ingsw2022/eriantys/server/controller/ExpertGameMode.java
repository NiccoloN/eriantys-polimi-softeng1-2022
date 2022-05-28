package it.polimi.ingsw2022.eriantys.server.controller;

import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;

import java.io.IOException;

public class ExpertGameMode extends BasicGameMode {

    public ExpertGameMode(Game game) {

        super(game);

    }

    private void playCharacter(CharacterCard characterCard) throws IOException, InterruptedException {

        switch(characterCard.index) {

            case 1:
                requestMove(new MoveStudentRequest(characterCard.index, characterCard.getStudentsColors()), game.getCurrentPlayer().username);
                //TODO aggiungere nuovo studente alla carta
                break;
        }
    }
}
