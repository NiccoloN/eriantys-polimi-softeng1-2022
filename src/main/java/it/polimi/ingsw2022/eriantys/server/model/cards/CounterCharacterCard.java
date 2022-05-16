package it.polimi.ingsw2022.eriantys.server.model.cards;


/**
 * Extension of CharacterCard with a counter
 * @author Francesco Melegati Maccari
 */
public class CounterCharacterCard extends CharacterCard {
    private int counter;

    CounterCharacterCard(int index, Skill skill, String effect, int cost, int counter) {
        super(index, skill, effect, cost);
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void decrementCounter() {
        if (counter == 0) throw new RuntimeException("Counter has a negative value");
        this.counter--;

    }
}
