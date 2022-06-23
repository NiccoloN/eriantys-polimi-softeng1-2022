package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.utilityNodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SizedImageView extends ImageView {

    public SizedImageView(int size, Image image) {

        super(image);
        setPreserveRatio(true);
        setFitWidth(size);
    }

    public SizedImageView(int size) {

        super();
        setPreserveRatio(true);
        setFitWidth(size);
    }
}
