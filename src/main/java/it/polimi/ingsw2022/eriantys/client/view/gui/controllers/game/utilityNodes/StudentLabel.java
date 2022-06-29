package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes;

import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.control.Label;

public class StudentLabel extends Label {
    
    private final PawnColor color;
    
    public StudentLabel(PawnColor color) {
        
        super();
        this.color = color;
        getStyleClass().add("students-label");
        setStyle("-fx-text-fill: " + color.name().toLowerCase());
    }
    
    public PawnColor getColor() {
        
        return color;
    }
}
