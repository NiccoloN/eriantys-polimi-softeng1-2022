package it.polimi.ingsw2022.eriantys.model.cards;

import it.polimi.ingsw2022.eriantys.model.board.CloudTile;
import it.polimi.ingsw2022.eriantys.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.model.pawns.ColoredPawn;

/**
 * Extension of CharacterCard that handles selection of a color.
 * @author Francesco Melegati Maccari
 */
public class ColorCharacterCard extends CharacterCard{
    private ColoredPawn pawnColor;

    /**
     * @param index index of the CharacterCard
     * @param skill skill associated with the Card
     */
    public ColorCharacterCard(int index, Skill skill) {
        super(index, skill);
    }

    public ColoredPawn getPawnColor() {
        return pawnColor;
    }

    public void setPawnColor(ColoredPawn pawnColor) {
        this.pawnColor = pawnColor;
    }
}
