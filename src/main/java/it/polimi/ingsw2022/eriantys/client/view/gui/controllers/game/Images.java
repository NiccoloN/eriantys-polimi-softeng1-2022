package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game;

import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class provides access to various preloaded images
 * @author Niccolò Nicolosi
 */
public class Images {
    
    public static final int STUDENT_SIZE = 19;
    public static final int PROFESSOR_SIZE = 23;
    public static final int COLOR_SIZE = 60;
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
    public static final Map<PawnColor, Image> colorsImages;
    public static final Map<Integer, Image> helpersImages;
    public static final Map<Integer, Image> charactersImages;
    public static final Map<Integer, Image> islandsImages;
    public static final Map<Integer, Image> magesImages;
    public static final Image whiteTowerImage, blackTowerImage, greyTowerImage, motherNatureImage, coinImage, denyTileImage, schoolImage, cloudImage;
    
    private static final double MAX_SCALE = 2;
    
    static {
        
        studentsImages = new HashMap<>(5);
        for (PawnColor color : PawnColor.values()) studentsImages.put(color, Images.loadStudentImage(color));
        
        professorsImages = new HashMap<>(5);
        for (PawnColor color : PawnColor.values()) professorsImages.put(color, Images.loadProfessorImage(color));
        
        colorsImages = new HashMap<>(5);
        for (PawnColor color : PawnColor.values()) colorsImages.put(color, Images.loadColorImage(color));
        
        helpersImages = new HashMap<>(10);
        for (int n = 1; n <= 10; n++) helpersImages.put(n, Images.loadHelperImage(n));
        
        charactersImages = new HashMap<>(12);
        for (int i = 1; i <= 12; i++) charactersImages.put(i, Images.loadCharacterImage(i));
        
        islandsImages = new HashMap<>(3);
        for (int n = 1; n <= 3; n++) islandsImages.put(n, Images.loadIslandImage(n));
        
        magesImages = new HashMap<>(4);
        for (int n = 1; n <= 4; n++) magesImages.put(n, Images.loadMageImage(n));
        
        whiteTowerImage = Images.loadWhiteTowerImage();
        blackTowerImage = Images.loadBlackTowerImage();
        greyTowerImage = Images.loadGreyTowerImage();
        motherNatureImage = Images.loadMotherNatureImage();
        coinImage = Images.loadCoinImage();
        denyTileImage = loadDenyTileImage();
        schoolImage = loadSchoolImage();
        cloudImage = loadCloudImage();
    }
    
    private static Image loadStudentImage(PawnColor color) {
        
        String imageName = color.name().toLowerCase() + "Student3D.png";
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Pawns/3D/" + imageName)).toString();
        return new Image(path, STUDENT_SIZE * MAX_SCALE, STUDENT_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadProfessorImage(PawnColor color) {
        
        String imageName = color.name().toLowerCase() + "Prof3D.png";
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Pawns/3D/" + imageName)).toString();
        return new Image(path, PROFESSOR_SIZE * MAX_SCALE, PROFESSOR_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadColorImage(PawnColor color) {
        
        String imageName = "student_" + color.name().toLowerCase() + ".png";
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Pawns/2D/" + imageName)).toString();
        return new Image(path, COLOR_SIZE * MAX_SCALE, COLOR_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadWhiteTowerImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Pawns/white_tower.png")).toString();
        return new Image(path, TOWER_SIZE * MAX_SCALE, TOWER_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadBlackTowerImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Pawns/black_tower.png")).toString();
        return new Image(path, TOWER_SIZE * MAX_SCALE, TOWER_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadGreyTowerImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Pawns/grey_tower.png")).toString();
        return new Image(path, TOWER_SIZE * MAX_SCALE, TOWER_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadMotherNatureImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Pawns/mother_nature.png")).toString();
        return new Image(path, MOTHER_NATURE_SIZE * MAX_SCALE, MOTHER_NATURE_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadCoinImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Characters/coin.png")).toString();
        return new Image(path, COIN_SIZE * MAX_SCALE, COIN_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadDenyTileImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Characters/deny_island_icon.png")).toString();
        return new Image(path, DENY_TILE_SIZE * MAX_SCALE, DENY_TILE_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadHelperImage(int index) {
        
        String imageName = "Helper (" + index + ").png";
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Helpers/" + imageName)).toString();
        return new Image(path, CARD_SIZE * MAX_SCALE, CARD_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadCharacterImage(int index) {
        
        String imageName = "character (" + index + ").jpg";
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Characters/" + imageName)).toString();
        return new Image(path, CARD_SIZE * MAX_SCALE, CARD_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadSchoolImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Board/school.png")).toString();
        return new Image(path, SCHOOL_SIZE * MAX_SCALE, SCHOOL_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadIslandImage(int index) {
        
        String imageName = "island (" + index + ").png";
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Board/" + imageName)).toString();
        return new Image(path, ISLAND_SIZE * MAX_SCALE, ISLAND_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadCloudImage() {
        
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Board/cloud.png")).toString();
        return new Image(path, CLOUD_SIZE * MAX_SCALE, CLOUD_SIZE * MAX_SCALE, true, true);
    }
    
    private static Image loadMageImage(int index) {
        
        String imageName = "mage (" + index + ").png";
        String path = Objects.requireNonNull(Images.class.getResource("/Images/Game/Mages/" + imageName)).toString();
        return new Image(path, CARD_SIZE * MAX_SCALE, CARD_SIZE * MAX_SCALE, true, true);
    }
}
