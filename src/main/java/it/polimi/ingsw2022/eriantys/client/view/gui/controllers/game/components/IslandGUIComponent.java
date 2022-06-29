package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.ImageFactory;
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
    
    public IslandGUIComponent(Group islandGroup, GameController gameController) {
        
        this.gameController = gameController;
        
        islandImageView = ((ImageView) islandGroup.getChildren().get(0));
        islandImageView.setImage(ImageFactory.islandsImages.get(((int) (Math.random() * 3) + 1)));
        
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
            catch(IOException e) {
                
                e.printStackTrace();
            }
        };
    }
    
    private void initializeStudentImageViews() {
        
        redStudentImage    = new SizedImageView(ImageFactory.STUDENT_SIZE, ImageFactory.studentsImages.get(PawnColor.RED));
        greenStudentImage  = new SizedImageView(ImageFactory.STUDENT_SIZE, ImageFactory.studentsImages.get(PawnColor.GREEN));
        yellowStudentImage = new SizedImageView(ImageFactory.STUDENT_SIZE, ImageFactory.studentsImages.get(PawnColor.YELLOW));
        blueStudentImage   = new SizedImageView(ImageFactory.STUDENT_SIZE, ImageFactory.studentsImages.get(PawnColor.BLUE));
        pinkStudentImage   = new SizedImageView(ImageFactory.STUDENT_SIZE, ImageFactory.studentsImages.get(PawnColor.PINK));
        
        redStudentLabel    = new StudentLabel(PawnColor.RED);
        greenStudentLabel  = new StudentLabel(PawnColor.GREEN);
        yellowStudentLabel = new StudentLabel(PawnColor.YELLOW);
        blueStudentLabel   = new StudentLabel(PawnColor.BLUE);
        pinkStudentLabel   = new StudentLabel(PawnColor.PINK);
    }
    
    private void initializeStudentAndLabel(ImageView image, Label label, PawnColor color, int positionX, int positionY) {
        
        island.add(image, positionX, positionY);
        
        label.setText("0");
        label.getStyleClass().add("students-label");
        if(color == PawnColor.YELLOW) label.setTranslateY(-55);
        else label.setTranslateY(-27);
        label.setTranslateX(3);
        
        island.add(label, positionX, positionY);
    }
    
    private void initializeTowers() {
        
        whiteTowerImage = new SizedImageView(ImageFactory.TOWER_SIZE, ImageFactory.whiteTowerImage);
        grayTowerImage  = new SizedImageView(ImageFactory.TOWER_SIZE, ImageFactory.greyTowerImage);
        blackTowerImage = new SizedImageView(ImageFactory.TOWER_SIZE, ImageFactory.blackTowerImage);
        
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
        
        motherNatureImage = new SizedImageView(ImageFactory.MOTHER_NATURE_SIZE, ImageFactory.motherNatureImage);
        motherNatureImage.setTranslateY(-2);
        island.add(motherNatureImage, 2, 2);
        motherNatureImage.setVisible(false);
    }
    
    private void initializeDenyTilesImages() {
        
        denyTilesImages = new ArrayList<>(4);
        
        ImageView denyTile1 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);
        ImageView denyTile2 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);
        ImageView denyTile3 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);
        ImageView denyTile4 = new SizedImageView(ImageFactory.DENY_TILE_SIZE, ImageFactory.denyTileImage);
        
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
        
        for(ImageView image : denyTilesImages) {
            
            image.setTranslateY(indexTraslateY);
            image.setVisible(false);
        }
    }
    
    public void manageInput(MouseEvent mouseEvent) throws IOException {
        
        MoveRequest request = requestMessage.moveRequest;
        
        if(mouseEvent.getButton() == MouseButton.PRIMARY) {
            
            if(characterIndex > 0) manageCharacters();
            
            else if(request instanceof MoveStudentRequest) {
                
                EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) request).toWhere, Integer.parseInt(indexLabel.getText()), chosenColor)));
                for(IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
                gameController.getDashboardGUIComponentOfPlayer(EriantysClient.getInstance().getUsername()).stopListeningToInput();
            }
            
            else if(request instanceof MoveMotherNatureRequest) {
                
                EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new MoveMotherNature(Integer.parseInt(indexLabel.getText()), ((MoveMotherNatureRequest) request).getMotherNatureMaxSteps())));
                for(IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
            }
        }
    }
    
    public void manageCharacters() throws IOException {
        
        if(chosenColor != null) {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) requestMessage.moveRequest).toWhere, Integer.parseInt(indexLabel.getText()), chosenColor, characterIndex)));
            for(IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
        }
        
        else {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseIsland(Integer.parseInt(indexLabel.getText()), characterIndex)));
            for(IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
        }
    }
    
    public void stopListeningToInput() {
        
        chosenColor = null;
        button.removeEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        islandImageView.setEffect(null);
    }
    
    public void listenToInput(MoveRequestMessage requestMessage) {
        
        listenToInput(requestMessage, null);
    }
    
    public void listenToInput(MoveRequestMessage requestMessage, PawnColor chosenColor) {
        
        this.requestMessage = requestMessage;
        this.chosenColor    = chosenColor;
        characterIndex      = 0;
        
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        
        islandImageView.setEffect(gameController.getBorderGlowEffect());
    }
    
    public void listenToInput(MoveRequestMessage requestMessage, int characterIndex) {
        
        listenToInput(requestMessage, null, characterIndex);
    }
    
    public void listenToInput(MoveRequestMessage requestMessage, PawnColor chosenColor, int characterIndex) {
        
        listenToInput(requestMessage, chosenColor);
        this.characterIndex = characterIndex;
    }
    
    public void setGuiIslandIndex(Integer guiIslandIndex) {
        
        indexLabel.setText(guiIslandIndex.toString());
        
        if(guiIslandIndex < 10) indexLabel.setTranslateX(6);
        else indexLabel.setTranslateX(2);
    }
    
    public void setStudents(PawnColor color, Integer value) {
        
        if(value < 0) throw new RuntimeException("Number of students can't be negative");
        
        switch(color) {
            case RED:
                redStudentImage.setVisible(value > 0);
                redStudentLabel.setText(value.toString());
                redStudentLabel.setVisible(value > 1);
                break;
            case GREEN:
                greenStudentImage.setVisible(value > 0);
                greenStudentLabel.setText(value.toString());
                greenStudentLabel.setVisible(value > 1);
                break;
            case YELLOW:
                yellowStudentImage.setVisible(value > 0);
                yellowStudentLabel.setText(value.toString());
                yellowStudentLabel.setVisible(value > 1);
                break;
            case BLUE:
                blueStudentImage.setVisible(value > 0);
                blueStudentLabel.setText(value.toString());
                blueStudentLabel.setVisible(value > 1);
                break;
            case PINK:
                pinkStudentImage.setVisible(value > 0);
                pinkStudentLabel.setText(value.toString());
                pinkStudentLabel.setVisible(value > 1);
                break;
            default:
                throw new RuntimeException("Chosen color does not exists");
        }
    }
    
    public void setMotherNature(boolean visible) {
        
        hasMotherNature = visible;
        motherNatureImage.setVisible(visible);
    }
    
    public boolean hasMotherNature() {
        
        return hasMotherNature;
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
            default:
                throw new RuntimeException("There are no towers of this color");
        }
    }
    
    public void setDenyTiles(int numberOfDenyTiles) {
        
        for(int i = 0; i < numberOfDenyTiles; i++) {
            denyTilesImages.get(i).setVisible(true);
        }
    }
    
    public int getCompoundIslandIndex() {
        
        return Integer.parseInt(indexLabel.getText());
    }
}
