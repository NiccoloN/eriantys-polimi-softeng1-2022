package it.polimi.ingsw2022.eriantys.model.cards;

public class CounterCharacterCard extends CharacterCard{
    private int counter;

    public CounterCharacterCard(int index, Skill skill, int counter) {
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
