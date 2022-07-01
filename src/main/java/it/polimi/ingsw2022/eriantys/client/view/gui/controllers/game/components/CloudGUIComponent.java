package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.Images;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.ColoredPawnImageView;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseCloud;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a cloud gui component. A cloud gui component is associated to a javafx Group that represents a cloud
 * and to the index of the cloud it represents. This component manages various graphic elements to represent the current state
 * of the cloud and attaches listeners to them in order to detect inputs and react accordingly
 * @author Emanuele Musto
 */
public class CloudGUIComponent {
    
    private final GameController gameController;
    private final int cloudIndex;
    private final Group cloudGroup;
    private final GridPane cloudGrid;
    private final Button button;
    private final EventHandler<MouseEvent> buttonClicked;
    private List<ColoredPawnImageView> students;
    private boolean empty;
    private MoveRequestMessage requestMessage;
    
    /**
     * Constructs a cloud gui component
     * @param cloudGroup     the javafx group associated to this component
     * @param cloudIndex     the index of the cloud associated to this component
     * @param gameController the game controller associated to this component
     */
    public CloudGUIComponent(Group cloudGroup, int cloudIndex, GameController gameController) {
        
        this.gameController = gameController;
        this.cloudIndex = cloudIndex;
        empty = true;
        
        ((ImageView) cloudGroup.getChildren().get(0)).setImage(Images.cloudImage);
        
        this.cloudGroup = cloudGroup;
        cloudGrid = (GridPane) cloudGroup.getChildren().get(1);
        button = (Button) cloudGroup.getChildren().get(2);
        
        initializeStudentImageViews();
        
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
        
        button.removeEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        cloudGroup.setEffect(null);
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage the message that requested to listen to inputs
     */
    public void listenToInput(MoveRequestMessage requestMessage) {
        
        this.requestMessage = requestMessage;
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, buttonClicked);
        cloudGroup.setEffect(gameController.getBorderGlowEffect());
    }
    
    /**
     * Sets the students of a given color to visualize onto this could component
     * @param color    the color of the students to visualize
     * @param students the number of students to visualize
     */
    public void setStudents(PawnColor color, int students) {
        
        for (int i = 0; i < students; i++) {
            
            for (ColoredPawnImageView image : this.students) {
                
                if (image.getColor() == null) {
                    
                    image.setStudentOfColor(color);
                    empty = false;
                    break;
                }
            }
        }
    }
    
    /**
     * Clears the cloud component from every visualized student
     */
    public void clearStudents() {
        
        for (ColoredPawnImageView image : students) image.clearColor();
        empty = true;
    }
    
    /**
     * @return whether this cloud component doesn't visualize any student
     */
    public boolean isEmpty() {
        
        return empty;
    }
    
    private void manageInput(MouseEvent mouseEvent) throws IOException {
        
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseCloud(cloudIndex)));
            stopListeningToInput();
        }
    }
    
    private void initializeStudentImageViews() {
        
        students = new ArrayList<>(3);
        
        ColoredPawnImageView firstStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
        ColoredPawnImageView secondStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
        ColoredPawnImageView thirdStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
        
        firstStudentImage.setVisible(false);
        secondStudentImage.setVisible(false);
        thirdStudentImage.setVisible(false);
        
        students.add(firstStudentImage);
        students.add(secondStudentImage);
        students.add(thirdStudentImage);
        
        switch (EriantysClient.getInstance().getGameSettings().numberOfPlayers) {
            
            case 2:
            case 4:
                cloudGrid.add(firstStudentImage, 1, 0);
                cloudGrid.add(secondStudentImage, 0, 2);
                cloudGrid.add(thirdStudentImage, 2, 2);
                
                firstStudentImage.setTranslateY(2);
                
                secondStudentImage.setTranslateX(2);
                secondStudentImage.setTranslateY(-10);
                
                thirdStudentImage.setTranslateX(-2);
                thirdStudentImage.setTranslateY(-10);
                break;
            case 3:
                ColoredPawnImageView fourthStudentImage = new ColoredPawnImageView(Images.STUDENT_SIZE);
                students.add(fourthStudentImage);
                
                fourthStudentImage.setVisible(false);
                
                cloudGrid.add(firstStudentImage, 0, 0);
                cloudGrid.add(secondStudentImage, 2, 0);
                cloudGrid.add(thirdStudentImage, 0, 2);
                cloudGrid.add(fourthStudentImage, 2, 2);
                
                firstStudentImage.setTranslateX(7);
                firstStudentImage.setTranslateY(7);
                
                secondStudentImage.setTranslateX(-7);
                secondStudentImage.setTranslateY(7);
                
                thirdStudentImage.setTranslateX(7);
                thirdStudentImage.setTranslateY(-7);
                
                fourthStudentImage.setTranslateX(-7);
                fourthStudentImage.setTranslateY(-7);
                break;
            default:
                throw new RuntimeException("Number of player needs to be between 2 and 4");
        }
    }
}
