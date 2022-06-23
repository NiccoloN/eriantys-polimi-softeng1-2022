package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.image.Image;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageFactory {

    private static final int DENY_TILE_SIZE = 20;
    private static final int STUDENT_SIZE = 19;
    private static final int PROFESSOR_SIZE = 23;
    private static final int TOWER_SIZE = 35;
    private static final int MOTHER_NATURE_SIZE = 35;

    public static final Map<PawnColor, Image> studentsImages;
    public static final Map<PawnColor, Image> professorsImages;
    public static final Image whiteTowerImage, blackTowerImage, greyTowerImage, motherNatureImage, coinImage, denyTileImage;

    static {

        studentsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) studentsImages.put(color, ImageFactory.loadStudentImage(color));

        professorsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) professorsImages.put(color, ImageFactory.loadProfessorImage(color));

        whiteTowerImage = ImageFactory.loadWhiteTowerImage();
        blackTowerImage = ImageFactory.loadBlackTowerImage();
        greyTowerImage = ImageFactory.loadGreyTowerImage();
        motherNatureImage = ImageFactory.loadMotherNatureImage();
        coinImage = ImageFactory.loadCoinImage();
        denyTileImage = loadDenyTileImage();
    }

    private static Image loadStudentImage(PawnColor color) {

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

    private static Image loadProfessorImage(PawnColor color) {

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

    private static Image loadWhiteTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/white_tower.png")).toString();
        return new Image(path, TOWER_SIZE, TOWER_SIZE, true, true);
    }

    private static Image loadBlackTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/black_tower.png")).toString();
        return new Image(path, TOWER_SIZE, TOWER_SIZE, true, true);
    }

    private static Image loadGreyTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/grey_tower.png")).toString();
        return new Image(path, TOWER_SIZE, TOWER_SIZE, true, true);
    }

    private static Image loadMotherNatureImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/mother_nature.png")).toString();
        return new Image(path, MOTHER_NATURE_SIZE, MOTHER_NATURE_SIZE, true, true);
    }

    private static Image loadCoinImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Characters/coin.png")).toString();
        return new Image(path, STUDENT_SIZE, STUDENT_SIZE, true, true);
    }

    private static Image loadDenyTileImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Characters/deny_island_icon.png")).toString();
        return new Image(path, DENY_TILE_SIZE, DENY_TILE_SIZE, true, true);
    }
}
