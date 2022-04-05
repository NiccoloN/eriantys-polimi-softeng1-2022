package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.Game;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Skill {
    public final BiConsumer<CharacterCard, Game> effect;
    public final CharacterCard characterCard;
    private int cost;

    public Skill(CharacterCard characterCard, BiConsumer<CharacterCard, Game> effect, int cost) {
        this.characterCard = characterCard;
        this.effect = effect;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void incrementCost() {
        this.cost = this.cost + 1;
    }

    public void apply(Game game) {
        effect.accept(this.characterCard, game);
    }
}