package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.enums.Mage;

public class HelperCard extends Card {
    private int priority;
    private int movement;
    private Mage mage;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public Mage getMage() {
        return mage;
    }

    public void setMage(Mage mage) {
        this.mage = mage;
    }
}
