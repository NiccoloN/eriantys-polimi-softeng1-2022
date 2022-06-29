package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SizedImageView extends ImageView {
    
    public SizedImageView(int size, Image image) {
        
        super(image);
        setSmooth(true);
        setPreserveRatio(true);
        setFitWidth(size);
    }
    
    public SizedImageView(int size) {
        
        super();
        setSmooth(true);
        setPreserveRatio(true);
        setFitWidth(size);
    }
}
