package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

public class ColorCLIComponent extends BasicCLIComponent {

    public static final String DEFAULT_COLOR = RESET;

    private String color;
    public final PawnColor pawnColor;

    public ColorCLIComponent(PawnColor color) {

        super(3, 1);
        this.color = DEFAULT_COLOR;
        pawnColor = color;
        buildRows();
    }

    private void buildRows() {

        setRow(0, color + "|" + pawnColor.ansiBackgroundColor + " " + RESET + color + "|" + RESET);
    }

    @Override
    public void setColor(String color) {

        this.color = color;
        buildRows();
    }
}
