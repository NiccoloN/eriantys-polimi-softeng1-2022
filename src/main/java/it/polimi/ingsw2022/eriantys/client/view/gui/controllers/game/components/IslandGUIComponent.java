package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.Images;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.SizedImageView;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.StudentLabel;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseIsland;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveMotherNature;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveMotherNatureRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an island gui component. An island gui component is associated to a javafx Group that represents an island.
 * This component manages various graphic elements to represent the current state of the island and attaches listeners to them in order
 * to detect inputs and react accordingly
 * @author Emanuele Musto
 */
public class IslandGUIComponent {
    
    private final GameController gameController;
    private final int indexTraslateY = 41;
    private final GridPane island;
    private final Button button;
    private final ImageView islandImageView;
    private final Label indexLabel;
    private final EventHandler<MouseEvent> buttonClicked;
    private PawnColor chosenColor;
    private boolean hasMotherNature;
    private int characterIndex;
    private ImageView redStudentImage, greenStudentImage, yellowStudentImage, blueStudentImage, pinkStudentImage;
    private Label redStudentLabel, greenStudentLabel, yellowStudentLabel, blueStudentLabel, pinkStudentLabel;
    private ImageView whiteTowerImage, grayTowerImage, blackTowerImage;
    private ImageView motherNatureImage;
    private List<ImageView> denyTilesImages;
    private MoveRequestMessage requestMessage;
    
    /**
     * Constructs an island gui component
     * @param islandGroup    the javafx group associated to this component
     * @param gameController the game controller associated to this component
     */
    public IslandGUIComponent(Group islandGroup, GameController gameController) {
        
        this.gameController = gameController;
        
        islandImageView = ((ImageView) islandGroup.getChildren().get(0));
        islandImageView.setImage(Images.islandsImages.get(((int) (Math.random() * 3) + 1)));
        
        island = (GridPane) islandGroup.getChildren().get(1);
        button = (Button) islandGroup.getChildren().get(2);
        
        initializeStudentImageViews();
        
        initializeStudentAndLabel(redStudentImage, redStudentLabel, PawnColor.RED, 1, 0);
        initializeStudentAndLabel(greenStudentImage, greenStudentLabel, PawnColor.GREEN, 3, 0);
        initializeStudentAndLabel(yellowStudentImage, yellowStudentLabel, PawnColor.YELLOW, 2, 1);
        initializeStudentAndLabel(blueStudentImage, blueStudentLabel, PawnColor.BLUE, 1, 2);
        initializeStudentAndLabel(pinkStudentImage, pinkStudentLabel, PawnColor.PINK, 3, 2);
        
        indexLabel = new Label();
        indexLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        indexLabel.setTranslateY(indexTraslateY);
        island.add(indexLabel, 2, 2);
        
        initializeTowers();
        initializeMotherNature();
        
        initializeDenyTilesImages();
        
        buttonClicked = mouseEvent -> {
            
            try {
                
                manageInput(mouseEvent);
            }
            catch (IOException e) {
                
                e.printStackTrace();
            }
        };
    }
    
    /**
     * Makes this component stop listening to click inputs
     */
    public void stopListeningToInput() {
        
        chosenColor = null;
        button.removeEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        islandImageView.setEffect(null);
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage the message that requested to listen to inputs
     */
    public void listenToInput(MoveRequestMessage requestMessage) {
        
        listenToInput(requestMessage, null);
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage the message that requested to listen to inputs
     * @param chosenColor    the student color previously chosen
     */
    public void listenToInput(MoveRequestMessage requestMessage, PawnColor chosenColor) {
        
        this.requestMessage = requestMessage;
        this.chosenColor = chosenColor;
        characterIndex = 0;
        
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        
        islandImageView.setEffect(gameController.getBorderGlowEffect());
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage the message that requested to listen to inputs
     * @param characterIndex the index of the character card that requested to listen to inputs
     */
    public void listenToInput(MoveRequestMessage requestMessage, int characterIndex) {
        
        listenToInput(requestMessage, null, characterIndex);
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage the message that requested to listen to inputs
     * @param chosenColor    the student color previously chosen
     * @param characterIndex the index of the character card that requested to listen to inputs
     */
    public void listenToInput(MoveRequestMessage requestMessage, PawnColor chosenColor, int characterIndex) {
        
        listenToInput(requestMessage, chosenColor);
        this.characterIndex = characterIndex;
    }
    
    /**
     * Sets the students of the given color to visualize on this component
     * @param color    the color of the students to visualize
     * @param students the number of students to visualize
     */
    public void setStudents(PawnColor color, Integer students) {
        
        if (students < 0) throw new RuntimeException("Number of students can't be negative");
        
        switch (color) {
            case RED:
                redStudentImage.setVisible(students > 0);
                redStudentLabel.setText(students.toString());
                redStudentLabel.setVisible(students > 1);
                break;
            case GREEN:
                greenStudentImage.setVisible(students > 0);
                greenStudentLabel.setText(students.toString());
                greenStudentLabel.setVisible(students > 1);
                break;
            case YELLOW:
                yellowStudentImage.setVisible(students > 0);
                yellowStudentLabel.setText(students.toString());
                yellowStudentLabel.setVisible(students > 1);
                break;
            case BLUE:
                blueStudentImage.setVisible(students > 0);
                blueStudentLabel.setText(students.toString());
                blueStudentLabel.setVisible(students > 1);
                break;
            case PINK:
                pinkStudentImage.setVisible(students > 0);
                pinkStudentLabel.setText(students.toString());
                pinkStudentLabel.setVisible(students > 1);
                break;
            default:
                throw new RuntimeException("Invalid chosen color");
        }
    }
    
    /**
     * Sets if mother nature is visible on this component
     * @param visible the visibility of mother nature
     */
    public void setMotherNature(boolean visible) {
        
        hasMotherNature = visible;
        motherNatureImage.setVisible(visible);
    }
    
    /**
     * @return whether mother nature is visible on this component
     */
    public boolean hasMotherNature() {
        
        return hasMotherNature;
    }
    
    /**
     * Sets the visible tower on this component
     * @param team the team of the tower to visualize
     */
    public void setTower(Team team) {
        
        switch (team.getTeamName()) {
            
            case "WHITE":
                whiteTowerImage.setVisible(true);
                blackTowerImage.setVisible(false);
                grayTowerImage.setVisible(false);
                break;
            case "BLACK":
                whiteTowerImage.setVisible(false);
                blackTowerImage.setVisible(true);
                grayTowerImage.setVisible(false);
                break;
            case "CYAN":
                whiteTowerImage.setVisible(false);
                blackTowerImage.setVisible(false);
                grayTowerImage.setVisible(true);
                break;
            default:
                throw new RuntimeException("Invalid team");
        }
    }
    
    /**
     * Sets the number of deny tiles to visualize on this component
     * @param denyTiles the number of deny tiles to visualize
     */
    public void setDenyTiles(int denyTiles) {
        
        for (int i = 0; i < denyTiles; i++) {
            denyTilesImages.get(i).setVisible(true);
        }
    }
    
    /**
     * @return the compound island index visualized on this component
     */
    public int getCompoundIslandIndex() {
        
        return Integer.parseInt(indexLabel.getText());
    }
    
    /**
     * Sets the index of the compound island to visualize on this component
     * @param compoundIslandIndex the index to visualize
     */
    public void setCompoundIslandIndex(Integer compoundIslandIndex) {
        
        indexLabel.setText(compoundIslandIndex.toString());
        
        if (compoundIslandIndex < 10) indexLabel.setTranslateX(6);
        else indexLabel.setTranslateX(2);
    }
    
    private void manageInput(MouseEvent mouseEvent) throws IOException {
        
        MoveRequest request = requestMessage.moveRequest;
        
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            
            if (characterIndex > 0) manageCharacters();
            
            else if (request instanceof MoveStudentRequest) {
                
                EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) request).toWhere, Integer.parseInt(indexLabel.getText()), chosenColor)));
                for (IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
                gameController.getSchoolGUIComponentOfPlayer(EriantysClient.getInstance().getUsername()).stopListeningToInput();
            }
            else if (request instanceof MoveMotherNatureRequest) {
                
                EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new MoveMotherNature(Integer.parseInt(indexLabel.getText()), ((MoveMotherNatureRequest) request).getMotherNatureMaxSteps())));
                for (IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
            }
        }
    }
    
    private void manageCharacters() throws IOException {
        
        if (chosenColor != null) {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) requestMessage.moveRequest).toWhere, Integer.parseInt(indexLabel.getText()), chosenColor, characterIndex)));
            for (IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
        }
        else {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseIsland(Integer.parseInt(indexLabel.getText()), characterIndex)));
            for (IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
        }
    }
    
    private void initializeStudentImageViews() {
        
        redStudentImage = new SizedImageView(Images.STUDENT_SIZE, Images.studentsImages.get(PawnColor.RED));
        greenStudentImage = new SizedImageView(Images.STUDENT_SIZE, Images.studentsImages.get(PawnColor.GREEN));
        yellowStudentImage = new SizedImageView(Images.STUDENT_SIZE, Images.studentsImages.get(PawnColor.YELLOW));
        blueStudentImage = new SizedImageView(Images.STUDENT_SIZE, Images.studentsImages.get(PawnColor.BLUE));
        pinkStudentImage = new SizedImageView(Images.STUDENT_SIZE, Images.studentsImages.get(PawnColor.PINK));
        
        redStudentLabel = new StudentLabel(PawnColor.RED);
        greenStudentLabel = new StudentLabel(PawnColor.GREEN);
        yellowStudentLabel = new StudentLabel(PawnColor.YELLOW);
        blueStudentLabel = new StudentLabel(PawnColor.BLUE);
        pinkStudentLabel = new StudentLabel(PawnColor.PINK);
    }
    
    private void initializeStudentAndLabel(ImageView image, Label label, PawnColor color, int positionX, int positionY) {
        
        island.add(image, positionX, positionY);
        
        label.setText("0");
        label.getStyleClass().add("students-label");
        if (color == PawnColor.YELLOW) label.setTranslateY(-55);
        else label.setTranslateY(-27);
        label.setTranslateX(3);
        
        island.add(label, positionX, positionY);
    }
    
    private void initializeTowers() {
        
        whiteTowerImage = new SizedImageView(Images.TOWER_SIZE, Images.whiteTowerImage);
        grayTowerImage = new SizedImageView(Images.TOWER_SIZE, Images.greyTowerImage);
        blackTowerImage = new SizedImageView(Images.TOWER_SIZE, Images.blackTowerImage);
        
        whiteTowerImage.setTranslateY(-4);
        island.add(whiteTowerImage, 2, 0);
        whiteTowerImage.setVisible(false);
        
        grayTowerImage.setTranslateY(-4);
        island.add(grayTowerImage, 2, 0);
        grayTowerImage.setVisible(false);
        
        blackTowerImage.setTranslateY(-4);
        island.add(blackTowerImage, 2, 0);
        blackTowerImage.setVisible(false);
    }
    
    private void initializeMotherNature() {
        
        motherNatureImage = new SizedImageView(Images.MOTHER_NATURE_SIZE, Images.motherNatureImage);
        motherNatureImage.setTranslateY(-2);
        island.add(motherNatureImage, 2, 2);
        motherNatureImage.setVisible(false);
    }
    
    private void initializeDenyTilesImages() {
        
        denyTilesImages = new ArrayList<>(4);
        
        ImageView denyTile1 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
        ImageView denyTile2 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
        ImageView denyTile3 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
        ImageView denyTile4 = new SizedImageView(Images.DENY_TILE_SIZE, Images.denyTileImage);
        
        island.add(denyTile1, 0, 2);
        island.add(denyTile2, 1, 2);
        island.add(denyTile3, 3, 2);
        island.add(denyTile4, 4, 2);
        
        denyTilesImages.add(denyTile1);
        denyTilesImages.add(denyTile2);
        denyTilesImages.add(denyTile3);
        denyTilesImages.add(denyTile4);
        
        denyTile1.setTranslateX(3);
        denyTile2.setTranslateX(5);
        denyTile3.setTranslateX(-4);
        denyTile4.setTranslateX(-12);
        
        for (ImageView image : denyTilesImages) {
            
            image.setTranslateY(indexTraslateY);
            image.setVisible(false);
        }
    }
}
