package it.polimi.ingsw2022.eriantys.server.model.cards;

import it.polimi.ingsw2022.eriantys.server.model.Game;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 * Skill associated to a CharacterCard and an effect that modifies the game and the associated character card.
 * @author Francesco Melegati Maccari
 */
public class Skill implements Serializable {
    private final BiConsumer<CharacterCard, Game> effect;
    public final CharacterCard characterCard;

    /**
     * @param characterCard the card associated to this Skill
     * @param effect effect to be applied when the card is played
     */
    Skill(CharacterCard characterCard, BiConsumer<CharacterCard, Game> effect) {
        this.characterCard = characterCard;
        this.effect = effect;
    }

    public void apply(Game game) {
        effect.accept(this.characterCard, game);
    }
}
