package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    GridPane entrance1, tables1, professors1, towers1, 
            entrance2, tables2, professors2, towers2,
            entrance3, tables3, professors3, towers3,
            entrance4, tables4, professors4, towers4;

    @FXML
    GridPane island11;

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

        Image image = ImageFactory.studentsImages.get(PawnColor.RED);
        Image redStudentImage = studentsImages.get(PawnColor.RED);
        Image greenStudentImage = studentsImages.get(PawnColor.GREEN);
        Image yellowStudentImage = studentsImages.get(PawnColor.YELLOW);
        Image blueStudentImage = studentsImages.get(PawnColor.BLUE);
        Image pinkStudentImage = studentsImages.get(PawnColor.PINK);

        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 2; j++)
                if(i != 0 || j != 0) entrance1.add(new ImageView(redStudentImage), j, i);

        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 10; j++)
                tables1.add(new ImageView(coinImage), j, i);

        Image image1 = ImageFactory.professorsImages.get(PawnColor.RED);
        for(int n = 0; n < 5; n++) professors1.add(new ImageView(image1), 0, n);
        Image redProfImage = professorsImages.get(PawnColor.RED);
        for(int n = 0; n < 5; n++) professors1.add(new ImageView(redProfImage), 0, n);

        Image image2 = ImageFactory.whiteTowerImage;
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 2; j++)
                towers1.add(new ImageView(whiteTowerImage), j, i);

        island11.add(new ImageView(redStudentImage), 1, 0);

        Label label = new Label("5");
        label.getStyleClass().add("students-label");
        label.setTranslateX(3);
        label.setTranslateY(-30);

        island11.add(label, 1, 0);

        ImageView tower = new ImageView(whiteTowerImage);
        tower.setTranslateY(-7);
        island11.add(tower, 2, 0);

        island11.add(new ImageView(greenStudentImage), 3, 0);
        island11.add(new ImageView(yellowStudentImage), 2, 1);
        island11.add(new ImageView(blueStudentImage), 1, 2);

        ImageView motherNature = new ImageView(motherNatureImage);
        motherNature.setTranslateX(-3);
        motherNature.setTranslateY(-7);
        island11.add(motherNature, 2, 2);

        island11.add(new ImageView(pinkStudentImage), 3, 2);
    }
}
