package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.cards.Skill;

public class CharacterCard extends Card {
    public final Skill skill;

    public CharacterCard(int index, Skill skill) {
        super(index);
        this.skill = skill;
    }
}
