package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * This class represents a color cli component
 * @author Niccolò Nicolosi
 */
public class ColorCLIComponent extends BasicCLIComponent {
    
    public static final int WIDTH = 3, HEIGHT = 1;
    public static final String DEFAULT_COLOR = RESET;
    public final PawnColor pawnColor;
    private String color;
    
    /**
     * Constructs a color cli component of the given color
     * @param color the color that this component represents
     */
    public ColorCLIComponent(PawnColor color) {
        
        super(WIDTH, HEIGHT);
        this.color = DEFAULT_COLOR;
        pawnColor = color;
        buildRows();
    }
    
    @Override
    public void setColor(String color) {
        
        this.color = color;
        buildRows();
    }
    
    private void buildRows() {
        
        setRow(0, color + "|" + pawnColor.ansiBackgroundColor + " " + RESET + color + "|" + RESET);
    }
}
