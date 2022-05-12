package it.polimi.ingsw2022.eriantys.server.model.cards;

import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;

/**
 * Extension of CharacterCard that handles selection of a color.
 * @author Francesco Melegati Maccari
 */
public class ColorCharacterCard extends CharacterCard {
    private ColoredPawn pawnColor;

    /**
     * @param index index of the CharacterCard
     * @param skill skill associated with the Card
     */
    ColorCharacterCard(int index, Skill skill, String effect, int cost) {
        super(index, skill, effect, cost);
    }

    public ColoredPawn getPawnColor() {
        return pawnColor;
    }

    public void setPawnColor(ColoredPawn pawnColor) {
        this.pawnColor = pawnColor;
    }
}
