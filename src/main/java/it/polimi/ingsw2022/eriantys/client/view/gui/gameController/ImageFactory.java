package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.image.Image;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageFactory {

    public static final int STUDENT_SIZE = 19;
    public static final int PROFESSOR_SIZE = 23;
    public static final int MOTHER_NATURE_SIZE = 18;
    public static final int TOWER_SIZE = 18;
    public static final int COIN_SIZE = 19;
    public static final int DENY_TILE_SIZE = 20;
    public static final int CARD_SIZE = 70;
    public static final int SCHOOL_SIZE = 362;
    public static final int ISLAND_SIZE = 150;
    public static final int CLOUD_SIZE = 80;

    public static final Map<PawnColor, Image> studentsImages;
    public static final Map<PawnColor, Image> professorsImages;
    public static final Map<Integer, Image> helpersImages;
    public static final Map<Integer, Image> charactersImages;
    public static final Map<Integer, Image> islandsImages;
    public static final Map<Integer, Image> magesImages;
    public static final Image whiteTowerImage, blackTowerImage, greyTowerImage,
            motherNatureImage, coinImage, denyTileImage, schoolImage, cloudImage;

    private static final double MAX_SCALE = 2;

    static {

        studentsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) studentsImages.put(color, ImageFactory.loadStudentImage(color));

        professorsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) professorsImages.put(color, ImageFactory.loadProfessorImage(color));

        helpersImages = new HashMap<>(10);
        for(int n = 1; n <= 10; n++) helpersImages.put(n, ImageFactory.loadHelperImage(n));

        charactersImages = new HashMap<>(12);
        for(int i = 1; i <= 12 ; i++) charactersImages.put(i, ImageFactory.loadCharacterImage(i));

        islandsImages = new HashMap<>(3);
        for(int n = 1; n <= 3; n++) islandsImages.put(n, ImageFactory.loadIslandImage(n));

        magesImages = new HashMap<>(4);
        for(int n = 1; n <= 4; n++) magesImages.put(n, ImageFactory.loadMageImage(n));

        whiteTowerImage = ImageFactory.loadWhiteTowerImage();
        blackTowerImage = ImageFactory.loadBlackTowerImage();
        greyTowerImage = ImageFactory.loadGreyTowerImage();
        motherNatureImage = ImageFactory.loadMotherNatureImage();
        coinImage = ImageFactory.loadCoinImage();
        denyTileImage = loadDenyTileImage();
        schoolImage = loadSchoolImage();
        cloudImage = loadCloudImage();
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
        return new Image(path, STUDENT_SIZE * MAX_SCALE, STUDENT_SIZE * MAX_SCALE, true, true);
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
        return new Image(path, PROFESSOR_SIZE * MAX_SCALE, PROFESSOR_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadWhiteTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/white_tower.png")).toString();
        return new Image(path, TOWER_SIZE * MAX_SCALE, TOWER_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadBlackTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/black_tower.png")).toString();
        return new Image(path, TOWER_SIZE * MAX_SCALE, TOWER_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadGreyTowerImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/grey_tower.png")).toString();
        return new Image(path, TOWER_SIZE * MAX_SCALE, TOWER_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadMotherNatureImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Pawns/mother_nature.png")).toString();
        return new Image(path, MOTHER_NATURE_SIZE * MAX_SCALE, MOTHER_NATURE_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadCoinImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Characters/coin.png")).toString();
        return new Image(path, COIN_SIZE * MAX_SCALE, COIN_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadDenyTileImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Characters/deny_island_icon.png")).toString();
        return new Image(path, DENY_TILE_SIZE * MAX_SCALE, DENY_TILE_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadHelperImage(int index) {

        String imageName = "Helper (" + index + ").png";
        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Helpers/" + imageName)).toString();
        return new Image(path, CARD_SIZE * MAX_SCALE, CARD_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadCharacterImage(int index) {

        String imageName = "character (" + index + ").jpg";
        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Characters/" + imageName)).toString();
        return new Image(path, CARD_SIZE * MAX_SCALE, CARD_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadSchoolImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Board/school.png")).toString();
        return new Image(path, SCHOOL_SIZE * MAX_SCALE, SCHOOL_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadIslandImage(int index) {

        String imageName = "island (" + index + ").png";
        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Board/" + imageName)).toString();
        return new Image(path, ISLAND_SIZE * MAX_SCALE, ISLAND_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadCloudImage() {

        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Board/cloud.png")).toString();
        return new Image(path, CLOUD_SIZE * MAX_SCALE, CLOUD_SIZE * MAX_SCALE, true, true);
    }

    private static Image loadMageImage(int index) {

        String imageName = "mage (" + index + ").png";
        String path = Objects.requireNonNull(ImageFactory.class.getResource("/Images/Game/Mages/" + imageName)).toString();
        return new Image(path, CARD_SIZE * MAX_SCALE, CARD_SIZE * MAX_SCALE, true, true);
    }
}
