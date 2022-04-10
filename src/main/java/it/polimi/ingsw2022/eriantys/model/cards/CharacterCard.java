package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.cards.Skill;


/**
 * Special card with an associated Skill
 * @author Francesco Melegati Maccari
 */
public class CharacterCard extends Card {
    public final Skill skill;

    CharacterCard(int index, Skill skill) {
        super(index);
        this.skill = skill;
    }
}
