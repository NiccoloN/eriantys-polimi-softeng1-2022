package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes;

import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.control.Label;

/**
 * This class represents a javafx Label associated with a specific PawnColor
 * @author Emanuele Musto
 * @see PawnColor
 */
public class StudentLabel extends Label {
    
    private final PawnColor color;
    
    /**
     * Constructs a student label of the given color
     * @param color the color of this label
     */
    public StudentLabel(PawnColor color) {
        
        super();
        this.color = color;
        getStyleClass().add("students-label");
        setStyle("-fx-text-fill: " + color.name().toLowerCase());
    }
    
    /**
     * @return the color associated to this label
     */
    public PawnColor getColor() {
        
        return color;
    }
}
