package it.polimi.ingsw2022.eriantys.server.model.cards;

/**
 * Special card with an associated Skill
 * @author Francesco Melegati Maccari
 */
public class CharacterCard extends Card {
    public final Skill skill;
    public final String effect;
    private int cost;

    CharacterCard(int index, Skill skill, String effect, int cost) {
        super(index);
        this.skill = skill;
        this.effect = effect;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void incrementCost() {
        this.cost = this.cost + 1;
    }
}
