package it.polimi.ingsw2022.eriantys.server.model.cards;


/**
 * Extension of CharacterCard with a counter
 * @author Francesco Melegati Maccari
 */
public class CounterCharacterCard extends CharacterCard{
    private int counter;

    CounterCharacterCard(int index, Skill skill, int counter) {
        super(index, skill);
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
