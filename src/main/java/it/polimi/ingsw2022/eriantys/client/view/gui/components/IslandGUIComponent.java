package it.polimi.ingsw2022.eriantys.client.view.gui.components;

import it.polimi.ingsw2022.eriantys.client.view.gui.StudentLabel;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class IslandGUIComponent {
    private final int islandIndex;

    private final GameController gameController;
    private final GridPane island;
    private final Button click;

    private ImageView redStudentImage, greenStudentImage, yellowStudentImage, blueStudentImage, pinkStudentImage;
    private Label redStudentLabel, greenStudentLabel, yellowStudentLabel, blueStudentLabel, pinkStudentLabel;
    private final Label componentIndexLabel;

    private ImageView whiteTowerImage, grayTowerImage, blackTowerImage;
    private ImageView motherNatureImage;

    public IslandGUIComponent(Integer islandIndex, GridPane island, Button click, GameController gameController) {

        this.islandIndex = islandIndex;
        this.island = island;
        this.click = click;
        this.gameController = gameController;

        initializeStudentImageViews();

        initializeStudentAndLabel(redStudentImage, redStudentLabel, PawnColor.RED, 1, 0);
        initializeStudentAndLabel(greenStudentImage, greenStudentLabel, PawnColor.GREEN, 3, 0);
        initializeStudentAndLabel(yellowStudentImage, yellowStudentLabel, PawnColor.YELLOW, 2, 1);
        initializeStudentAndLabel(blueStudentImage, blueStudentLabel, PawnColor.BLUE, 1, 2);
        initializeStudentAndLabel(pinkStudentImage, pinkStudentLabel, PawnColor.PINK, 3, 2);

        componentIndexLabel = new Label();
        componentIndexLabel.setStyle("-fx-font-size: 16px");
        componentIndexLabel.setText(islandIndex.toString());
        componentIndexLabel.setTranslateX(2);
        componentIndexLabel.setTranslateY(36);
        island.add(componentIndexLabel, 2, 2);

        initializeTowers();
        initializeMotherNature();
    }

    public void initializeStudentImageViews() {

        redStudentImage = new ImageView(ImageFactory.loadStudentImage(PawnColor.RED));
        greenStudentImage = new ImageView(ImageFactory.loadStudentImage(PawnColor.GREEN));
        yellowStudentImage = new ImageView(ImageFactory.loadStudentImage(PawnColor.YELLOW));
        blueStudentImage = new ImageView(ImageFactory.loadStudentImage(PawnColor.BLUE));
        pinkStudentImage = new ImageView(ImageFactory.loadStudentImage(PawnColor.PINK));

        redStudentLabel = new StudentLabel(PawnColor.RED);
        greenStudentLabel = new StudentLabel(PawnColor.GREEN);
        yellowStudentLabel = new StudentLabel(PawnColor.YELLOW);
        blueStudentLabel = new StudentLabel(PawnColor.BLUE);
        pinkStudentLabel = new StudentLabel(PawnColor.PINK);
    }

    public void initializeStudentAndLabel(ImageView image, Label label, PawnColor color, int positionX, int positionY) {

        image.setId(color.toString());
        island.add(image, positionX, positionY);

        label.setText("0");
        label.setId(color + "Label");
        label.getStyleClass().add("students-label");
        if(color == PawnColor.YELLOW) label.setTranslateY(-53);
        else label.setTranslateY(-25);
        label.setTranslateX(3);

        island.add(label, positionX, positionY);
    }

    public void initializeTowers() {

        whiteTowerImage = new ImageView(ImageFactory.loadWhiteTowerImage());
        grayTowerImage = new ImageView(ImageFactory.loadGreyTowerImage());
        blackTowerImage = new ImageView(ImageFactory.loadBlackTowerImage());

        whiteTowerImage.setTranslateY(-7);
        island.add(whiteTowerImage, 2, 0);
        whiteTowerImage.setVisible(false);

        grayTowerImage.setTranslateY(-7);
        island.add(grayTowerImage, 2, 0);
        grayTowerImage.setVisible(false);

        blackTowerImage.setTranslateY(-7);
        island.add(blackTowerImage, 2, 0);
        blackTowerImage.setVisible(false);
    }

    public void initializeMotherNature() {

        motherNatureImage = new ImageView(ImageFactory.loadMotherNatureImage());
        motherNatureImage.setTranslateX(-3);
        motherNatureImage.setTranslateY(-7);
        island.add(motherNatureImage, 2, 2);
        motherNatureImage.setVisible(false);
    }


    public void listenToInput(MoveRequestMessage requestMessage) {

        click.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {

            if(mouseEvent.isPrimaryButtonDown()) {

                if(requestMessage.moveRequest instanceof MoveStudentRequest) {
                    //TODO: send message
                }
            }
        } );
    }

    public void setIslandIndex(Integer islandIndex) {

        componentIndexLabel.setText(islandIndex.toString());
    }

    public void setStudents(PawnColor color, Integer value) {

        if(value < 0) throw new RuntimeException("Number of students can't be negative");

        switch(color) {
            case RED:
                redStudentImage.setVisible(value != 0);
                redStudentLabel.setText(value.toString());
                break;
            case GREEN:
                greenStudentImage.setVisible(value != 0);
                greenStudentLabel.setText(value.toString());
                break;
            case YELLOW:
                yellowStudentImage.setVisible(value != 0);
                yellowStudentLabel.setText(value.toString());
                break;
            case BLUE:
                blueStudentImage.setVisible(value != 0);
                blueStudentLabel.setText(value.toString());
                break;
            case PINK:
                pinkStudentImage.setVisible(value != 0);
                pinkStudentLabel.setText(value.toString());
                break;
            default: throw new RuntimeException("Chosen color does not exists");
        }
    }

    public void setMotherNature(boolean visible) {

        motherNatureImage.setVisible(visible);
    }

    public void setTower(boolean visible, Team team) {

        switch(team.getTeamName()) {

            case "WHITE":
                whiteTowerImage.setVisible(visible);
                break;
            case "BLACK":
                blackTowerImage.setVisible(visible);
                break;
            case "CYAN":
                grayTowerImage.setVisible(visible);
                break;
            default: throw new RuntimeException("There are no towers of this color");
        }
    }

    public int getIslandIndex() { return islandIndex; }
}
