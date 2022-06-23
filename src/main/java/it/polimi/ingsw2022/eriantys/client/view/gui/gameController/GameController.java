package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GameController extends SceneController implements Initializable {

    private final Map<PawnColor, Image> studentsImages;
    private final Map<PawnColor, Image> professorsImages;
    private final Image whiteTowerImage, blackTowerImage, greyTowerImage, motherNatureImage, coinImage;

    @FXML
    Group schools, islands;

    @FXML
    GridPane clouds, characters;

    public GameController(EriantysGUI gui) {

        super(gui);
        studentsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) studentsImages.put(color, ImageFactory.getStudentImage(color));

        professorsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) professorsImages.put(color, ImageFactory.getProfessorImage(color));

        whiteTowerImage = ImageFactory.getWhiteTowerImage();
        blackTowerImage = ImageFactory.getBlackTowerImage();
        greyTowerImage = ImageFactory.getGreyTowerImage();
        motherNatureImage = ImageFactory.getMotherNatureImage();
        coinImage = ImageFactory.getCoinImage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image redStudentImage = studentsImages.get(PawnColor.RED);
        Image greenStudentImage = studentsImages.get(PawnColor.GREEN);
        Image yellowStudentImage = studentsImages.get(PawnColor.YELLOW);
        Image blueStudentImage = studentsImages.get(PawnColor.BLUE);
        Image pinkStudentImage = studentsImages.get(PawnColor.PINK);

        Group school1 = (Group) schools.getChildren().get(0);
        GridPane entrance = (GridPane) school1.getChildren().get(1);
        entrance.add(new ImageView(redStudentImage), 0, 0);
    }
}
