package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.Images;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.ColoredPawnImageView;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.SizedImageView;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a school gui component. A school gui component is associated to a javafx Group that represents a school
 * and to the team of the school it represents. This component manages various graphic elements to represent the current state
 * of the school and attaches listeners to them in order to detect inputs and react accordingly
 * @author Francesco Melegati Maccari
 * @see Team
 */
public class SchoolGUIComponent {
    
    private final Group schoolGroup;
    private final GridPane entrancePane;
    private final GridPane tablePane;
    private final GridPane professorPane;
    private final GridPane towersPane;
    
    private final Map<PawnColor, Integer> tableStudents;
    
    private final EventHandler<MouseEvent> dashboardClicked;
    private final GameController gameController;
    private MoveRequestMessage requestMessage;
    private PawnColor chosenColor;
    
    /**
     * Constructs a school gui component
     * @param schoolGroup    the javafx group associated to this component
     * @param team           the team associated to this component
     * @param gameController the game controller associated to this component
     */
    public SchoolGUIComponent(Group schoolGroup, Team team, GameController gameController) {
        
        this.gameController = gameController;
        this.schoolGroup = schoolGroup;
        
        entrancePane = (GridPane) schoolGroup.getChildren().get(1);
        tablePane = (GridPane) schoolGroup.getChildren().get(2);
        professorPane = (GridPane) schoolGroup.getChildren().get(3);
        towersPane = (GridPane) schoolGroup.getChildren().get(4);
        
        ((ImageView) schoolGroup.getChildren().get(0)).setImage(Images.schoolImage);
        
        PawnColor[] pawnColors = PawnColor.values();
        
        tableStudents = new HashMap<>(5);
        for (PawnColor color : pawnColors) tableStudents.put(color, 0);
        
        int colorRow = 0;
        for (PawnColor color : pawnColors) {
            
            // Setting up professors pane
            ColoredPawnImageView professorsImageView = new ColoredPawnImageView(Images.PROFESSOR_SIZE);
            professorsImageView.setProfessorOfColor(color);
            professorsImageView.setVisible(false);
            professorPane.add(professorsImageView, 0, colorRow);
            
            // Setting up table pane
            int TABLE_COLS = 10;
            for (int col = 0; col < TABLE_COLS; col++) {
                
                SizedImageView coinImageView = null;
                if (EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT && col % 3 == 2)
                    coinImageView = new SizedImageView(Images.COIN_SIZE, Images.coinImage);
                
                ColoredPawnImageView studentImageView = new ColoredPawnImageView(Images.STUDENT_SIZE);
                studentImageView.setStudentOfColor(color);
                studentImageView.setVisible(false);
                
                Group tableSeatGroup;
                if (coinImageView != null) tableSeatGroup = new Group(coinImageView, studentImageView);
                else tableSeatGroup = new Group(studentImageView);
                
                tablePane.add(tableSeatGroup, col, colorRow);
            }
            colorRow++;
        }
        
        // Setting up entrance pane
        int ENTRANCE_ROWS = 5;
        int ENTRANCE_COLS = 2;
        for (int row = 0; row < ENTRANCE_ROWS; row++) {
            
            for (int col = 0; col < ENTRANCE_COLS; col++) {
                
                if (row == 0 && col == 0) continue;
                ColoredPawnImageView studentImageView = new ColoredPawnImageView(Images.STUDENT_SIZE);
                studentImageView.setVisible(false);
                entrancePane.add(studentImageView, col, row);
            }
        }
        
        // Setting up towers pane
        int TOWERS_ROWS = 4;
        int TOWERS_COLS = 2;
        for (int row = 0; row < TOWERS_ROWS; row++) {
            
            for (int col = 0; col < TOWERS_COLS; col++) {
                
                Image towerImage;
                switch (team.getTeamName()) {
                    case "BLACK":
                        towerImage = Images.blackTowerImage;
                        break;
                    case "WHITE":
                        towerImage = Images.whiteTowerImage;
                        break;
                    case "CYAN":
                        towerImage = Images.greyTowerImage;
                        break;
                    default:
                        throw new InvalidParameterException("Invalid team");
                }
                
                SizedImageView coloredImageView = new SizedImageView(Images.TOWER_SIZE, towerImage);
                coloredImageView.setVisible(false);
                towersPane.add(coloredImageView, col, row);
            }
        }
        
        dashboardClicked = mouseEvent -> {
            
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
        
        schoolGroup.removeEventHandler(MouseEvent.MOUSE_CLICKED, dashboardClicked);
        schoolGroup.setEffect(null);
    }
    
    /**
     * Sets the students to visualize at the entrance of this school component
     * @param students the list of student to visualize
     */
    public void setEntranceStudents(List<PawnColor> students) {
        
        for (int n = 0; n < entrancePane.getChildren().size(); n++) {
            
            ColoredPawnImageView student = (ColoredPawnImageView) entrancePane.getChildren().get(n);
            
            if (n < students.size()) student.setStudentOfColor(students.get(n));
            else student.setVisible(false);
        }
    }
    
    /**
     * Sets the students to visualize at the table of the given color in this school component
     * @param students the number of students to visualize
     * @param color    the color of the students to visualize
     * @throws InvalidParameterException if students < 0 or > 10
     */
    public void setTableStudents(int students, PawnColor color) {
        
        if (students < 0 || students > 10) throw new InvalidParameterException("Students must be >= 0 and <= 10");
        
        int currentStudents = tableStudents.get(color);
        
        if (currentStudents < students) {
            
            for (int i = 0; i < tablePane.getChildren().size() && currentStudents != students; i++) {
                
                Group tableSeatGroup = (Group) tablePane.getChildren().get(i);
                ColoredPawnImageView student = (ColoredPawnImageView) tableSeatGroup.getChildren().get(tableSeatGroup.getChildren().size() > 1 ? 1 : 0);
                
                while (color != student.getColor()) {
                    
                    i += 10;
                    tableSeatGroup = (Group) tablePane.getChildren().get(i);
                    student = (ColoredPawnImageView) tableSeatGroup.getChildren().get(tableSeatGroup.getChildren().size() > 1 ? 1 : 0);
                }
                
                if (!student.isVisible()) {
                    
                    student.setVisible(true);
                    currentStudents++;
                }
            }
        }
        
        else if (currentStudents > students) {
            
            for (int i = tablePane.getChildren().size() - 1; i > 0 && currentStudents != students; i--) {
                
                Group tableSeatGroup = (Group) tablePane.getChildren().get(i);
                ColoredPawnImageView student = (ColoredPawnImageView) tableSeatGroup.getChildren().get(tableSeatGroup.getChildren().size() > 1 ? 1 : 0);
                
                while (color != student.getColor()) {
                    
                    i -= 10;
                    tableSeatGroup = (Group) tablePane.getChildren().get(i);
                    student = (ColoredPawnImageView) tableSeatGroup.getChildren().get(tableSeatGroup.getChildren().size() > 1 ? 1 : 0);
                }
                
                if (student.isVisible()) {
                    
                    student.setVisible(false);
                    currentStudents--;
                }
            }
        }
        
        tableStudents.put(color, currentStudents);
    }
    
    /**
     * Sets if the professor of the given color is visible on this component
     * @param color   the color of the professor
     * @param visible the visibility of the professor
     */
    public void setProfessor(PawnColor color, boolean visible) {
        
        for (Node child : professorPane.getChildren()) {
            
            ColoredPawnImageView professor = (ColoredPawnImageView) child;
            
            if (professor.getColor() == color) {
                
                professor.setVisible(visible);
                return;
            }
        }
    }
    
    /**
     * Sets the number of towers to visualize on this component
     * @param towers the number of towers to visualize
     */
    public void setTowers(int towers) {
        
        for (Node tower : towersPane.getChildren()) {
            
            if (towers > 0) {
                
                tower.setVisible(true);
                towers--;
            }
            else tower.setVisible(false);
        }
    }
    
    /**
     * Makes this component start listening to click inputs
     * @param requestMessage the message that requested to listen to inputs
     * @param chosenColor    the student color previously chosen
     */
    public void listenToInput(MoveRequestMessage requestMessage, PawnColor chosenColor) {
        
        this.requestMessage = requestMessage;
        this.chosenColor = chosenColor;
        schoolGroup.addEventHandler(MouseEvent.MOUSE_CLICKED, dashboardClicked);
        
        schoolGroup.setEffect(gameController.getBorderGlowEffect());
    }
    
    private void manageInput(MouseEvent mouseEvent) throws IOException {
        
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new MoveStudent(ColoredPawnOriginDestination.TABLE, ((MoveStudentRequest) requestMessage.moveRequest).toWhere, -1, chosenColor)));
            
            stopListeningToInput();
            for (IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
        }
    }
}
