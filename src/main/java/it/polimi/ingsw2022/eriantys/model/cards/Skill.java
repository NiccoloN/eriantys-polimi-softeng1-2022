package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.Game;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Skill {
    private BiConsumer<CharacterCard, Game> effect;
    private int cost;

    public Skill(Consumer<Game> effect) {
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void apply(Game game) {

    }

    public void incrementCost() {
        this.cost = this.cost + 1;
    }
}
