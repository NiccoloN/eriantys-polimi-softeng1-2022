package it.polimi.ingsw2022.eriantys.model.cards;

public class CounterCharacterCard extends CharacterCard{
    private int counter;

    public CounterCharacterCard(Skill skill, int counter) {
        super(skill);
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void decrementCounter() {
        if (counter > 0) {
            this.counter = this.counter - 1;
        }
    }
}
