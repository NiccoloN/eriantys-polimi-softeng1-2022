package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.utilityNodes;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;

public class IndexedImageView extends SizedImageView {

    private int index;

    public IndexedImageView(int size) {

        super(size);
    }

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {

        this.index = index;
        setImage(ImageFactory.charactersImages.get(index));
    }
}
