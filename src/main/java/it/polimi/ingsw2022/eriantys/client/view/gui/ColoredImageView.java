package it.polimi.ingsw2022.eriantys.client.view.gui;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.image.ImageView;

public class ColoredImageView extends ImageView {

    PawnColor color;

    public ColoredImageView() {

        super();
        color = null;
    }

    public PawnColor getColor() {
        return color;
    }

    public void setColor(PawnColor color) {

        this.color = color;
        setImage(ImageFactory.studentsImages.get(color));
        setVisible(true);
    }

    public void clearColor() {

        this.color = null;
        setVisible(false);
    }

}
