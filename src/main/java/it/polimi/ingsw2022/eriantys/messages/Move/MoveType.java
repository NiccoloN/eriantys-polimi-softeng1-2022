package it.polimi.ingsw2022.eriantys.messages.Move;

import java.io.Serializable;

/**
 * This class represents every move that can take place in the game
 * @author Emanuele Musto
 */
public enum MoveType implements Serializable {
    CHOOSE_HELPER_CARD("Choose an helper card"),
    MOVE_STUDENT("Move one student from entrance to either an island or to the dining room"),
    MOVE_MOTHER_NATURE("Choose how many steps mother nature will move"),
    CHOOSE_CLOUD("Choose a cloud from which taking new students"),
    CHOOSE_CHARACTER_CARD("If wanted, it's possible to choose a character card");

    public final String sentence;

    MoveType(String sentence) {
        this.sentence = sentence;
    }
}
