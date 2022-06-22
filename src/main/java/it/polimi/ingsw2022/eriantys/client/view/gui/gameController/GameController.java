package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private final Image whiteTowerImage, blackTowerImage, greyTowerImage, motherNatureImage;

    @FXML
    GridPane entrance1, tables1, professors1, towers1, 
            entrance2, tables2, professors2, towers2,
            entrance3, tables3, professors3, towers3,
            entrance4, tables4, professors4, towers4;

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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image image = studentsImages.get(PawnColor.RED);

        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 2; j++)
                entrance1.add(new ImageView(image), j, i);

        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 10; j++)
                tables1.add(new ImageView(image), j, i);

        Image image1 = professorsImages.get(PawnColor.RED);
        for(int n = 0; n < 5; n++) professors1.add(new ImageView(image1), 0, n);

        Image image2 = ImageFactory.getWhiteTowerImage();
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 2; j++)
                towers1.add(new ImageView(image2), j, i);
    }
}
