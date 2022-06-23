package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.DashboardGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.IslandGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.PlayerGUIComponent;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

public class GameController extends SceneController implements Initializable {

    public static Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {

        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    private final Mode gameMode;
    private PawnColor chosenColor;

    private final List<String> playersUsernames;
    private final List<IslandGUIComponent> islandGUIComponents;
    private final Map<String, DashboardGUIComponent> dashboardComponents;
    private final Map<String, PlayerGUIComponent> playerComponents;

    private final Map<PawnColor, Image> studentsImages;
    private final Map<PawnColor, Image> professorsImages;
    private final Image whiteTowerImage, blackTowerImage, greyTowerImage, motherNatureImage, coinImage;

    private IslandGUIComponent islandController;
    //private HelperCardController helperCardController;
    //private CharacterCardController characterCardController;
    //private CloudController cloudController;
    private PlayerGUIComponent playerGUIComponent;

    @FXML
    GridPane entrance1, tables1, professors1, towers1,
            entrance2, tables2, professors2, towers2,
            entrance3, tables3, professors3, towers3,
            entrance4, tables4, professors4, towers4;

    @FXML
    GridPane island11;

    public GameController(EriantysGUI gui, Mode gameMode, List<Player> players) {

        super(gui);

        studentsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) studentsImages.put(color, ImageFactory.loadStudentImage(color));

        professorsImages = new HashMap<>(5);
        for(PawnColor color : PawnColor.values()) professorsImages.put(color, ImageFactory.loadProfessorImage(color));

        whiteTowerImage = ImageFactory.loadWhiteTowerImage();
        blackTowerImage = ImageFactory.loadBlackTowerImage();
        greyTowerImage = ImageFactory.loadGreyTowerImage();
        motherNatureImage = ImageFactory.loadMotherNatureImage();
        coinImage = ImageFactory.loadCoinImage();

        playersUsernames = new ArrayList<>(2);
        islandGUIComponents = new ArrayList<>(12);
        dashboardComponents = new HashMap<>();
        playerComponents = new HashMap<>();

        this.gameMode = gameMode;
        //for(Player player : players) playersUsernames.add(player.getUsername());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeComponents();

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

    }

    public void initializeComponents() {


        //for(String player : playersUsernames) playerComponents.put(player, new PlayerGUIComponent());
        islandGUIComponents.add(new IslandGUIComponent(11, island11, null, this));

        //dashBoardGUIComponent = new DashboardGUIComponent();
        playerGUIComponent = new PlayerGUIComponent();

        //if(gameMode == Mode.EXPERT) characterCardController = new CharacterCardController();
    }

    public PlayerGUIComponent getPlayerGUIComponent(String username) {

        return playerComponents.get(username);
    }

    public PawnColor getChosenColor() {
        return chosenColor;
    }
}
