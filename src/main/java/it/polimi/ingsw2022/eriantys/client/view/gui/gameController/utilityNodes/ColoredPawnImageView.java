package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.utilityNodes;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

public class ColoredPawnImageView extends SizedImageView {

    PawnColor color;

    public ColoredPawnImageView(int size) {

        super(size);
        color = null;
    }

    public PawnColor getColor() {
        return color;
    }

    public void setStudentOfColor(PawnColor color) {

        this.color = color;
        setImage(ImageFactory.studentsImages.get(color));
        setVisible(true);
    }

    public void setProfessorOfColor(PawnColor color) {

        this.color = color;
        setImage(ImageFactory.professorsImages.get(color));
        setVisible(true);
    }

    public void clearColor() {

        this.color = null;
        setVisible(false);
    }

}
