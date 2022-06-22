package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.image.Image;

import java.security.InvalidParameterException;
import java.util.Objects;

public class ImageFactory {

    private static final int STUDENT_SIZE = 19;
    private static final int PROFESSOR_SIZE = 23;
    private static final int TOWER_SIZE = 35;
    private static final int MOTHER_NATURE_SIZE = 35;

    public static Image getStudentImage(PawnColor color) {

        String imageName;
        switch(color) {

            case RED:
                imageName = "redStudent3D.png";
                break;
            case GREEN:
                imageName = "greenStudent3D.png";
                break;
            case YELLOW:
                imageName = "yellowStudent3D.png";
                break;
            case BLUE:
                imageName = "blueStudent3D.png";
                break;
            case PINK:
                imageName = "pinkStudent3D.png";
                break;
            default:
                throw new InvalidParameterException("Invalid pawn color");
        }

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/3D/" + imageName)).toString();
        return new Image(path, STUDENT_SIZE, STUDENT_SIZE, true, true);
    }

    public static Image getProfessorImage(PawnColor color) {

        String imageName;
        switch(color) {

            case RED:
                imageName = "redProf3D.png";
                break;
            case GREEN:
                imageName = "greenProf3D.png";
                break;
            case YELLOW:
                imageName = "yellowProf3D.png";
                break;
            case BLUE:
                imageName = "blueProf3D.png";
                break;
            case PINK:
                imageName = "pinkProf3D.png";
                break;
            default:
                throw new InvalidParameterException("Invalid pawn color");
        }

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/3D/" + imageName)).toString();
        return new Image(path, PROFESSOR_SIZE, PROFESSOR_SIZE, true, true);
    }

    public static Image getWhiteTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/white_tower.png")).toString();
        return new Image(path, TOWER_SIZE, TOWER_SIZE, true, true);
    }

    public static Image getBlackTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/black_tower.png")).toString();
        return new Image(path, TOWER_SIZE, TOWER_SIZE, true, true);
    }

    public static Image getGreyTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/grey_tower.png")).toString();
        return new Image(path, TOWER_SIZE, TOWER_SIZE, true, true);
    }

    public static Image getMotherNatureImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/mother_nature.png")).toString();
        return new Image(path, MOTHER_NATURE_SIZE, MOTHER_NATURE_SIZE, true, true);
    }

    public static Image getCoinImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Characters/coin.png")).toString();
        return new Image(path, STUDENT_SIZE, STUDENT_SIZE, true, true);
    }
}
