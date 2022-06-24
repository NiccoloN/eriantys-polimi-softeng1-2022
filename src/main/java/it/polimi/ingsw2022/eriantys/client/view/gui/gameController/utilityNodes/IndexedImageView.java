package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.utilityNodes;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import javafx.scene.image.ImageView;

public class IndexedImageView extends ImageView {

    private int index = 0;
    private final ImageView imageView;

    public IndexedImageView(ImageView imageView) {

        this.imageView = imageView;
    }

    public int getIndex() {

        return index;
    }

    public void setCharacterIndex(int index) {

        this.index = index;
        imageView.setImage(ImageFactory.charactersImages.get(index));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(ImageFactory.CHARACHTER_CARD_SIZE);
    }

    public ImageView getImageView() { return imageView; }
}
