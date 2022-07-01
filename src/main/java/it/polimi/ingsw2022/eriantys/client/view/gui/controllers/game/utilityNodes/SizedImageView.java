package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents a javafx ImageView of a fixed size
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class SizedImageView extends ImageView {
    
    /**
     * Constructs an image view of the given size and with the given image
     * @param size  the size of the view
     * @param image the image of the view
     */
    public SizedImageView(int size, Image image) {
        
        super(image);
        setSmooth(true);
        setPreserveRatio(true);
        setFitWidth(size);
    }
    
    /**
     * Constructs an image view of the given size with no image
     * @param size the size of the view
     */
    SizedImageView(int size) {
        
        super();
        setSmooth(true);
        setPreserveRatio(true);
        setFitWidth(size);
    }
}
