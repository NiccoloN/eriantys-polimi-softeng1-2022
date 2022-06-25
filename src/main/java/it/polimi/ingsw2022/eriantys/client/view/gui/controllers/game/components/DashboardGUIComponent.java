package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.SizedImageView;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.ImageFactory;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.utilityNodes.ColoredPawnImageView;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.event.Event;
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
import java.util.List;


public class DashboardGUIComponent {

    private final ImageView dashboardImageView;
    private final GridPane entrancePane;
    private final GridPane tablePane;
    private final GridPane professorPane;
    private final GridPane towersPane;

    private EventHandler<MouseEvent> dashboardClicked;
    private MoveRequestMessage requestMessage;
    private PawnColor choosenColor;

    private final GameController controller;

    public DashboardGUIComponent(Group school, Team team, GameController controller) {
        dashboardImageView = (ImageView) school.getChildren().get(0);
        dashboardImageView.setImage(ImageFactory.schoolImage);

        entrancePane = (GridPane) school.getChildren().get(1);
        tablePane = (GridPane) school.getChildren().get(2);
        professorPane = (GridPane) school.getChildren().get(3);
        towersPane = (GridPane) school.getChildren().get(4);

        this.controller = controller;

        int colorRow = 0;
        for (PawnColor color : PawnColor.values()) {

            // Setting up professors pane
            ColoredPawnImageView professorsImageView = new ColoredPawnImageView(ImageFactory.PROFESSOR_SIZE);
            professorsImageView.setProfessorOfColor(color);
            professorsImageView.setVisible(false);
            professorPane.add(professorsImageView, 0, colorRow);

            // Setting up table pane
            int TABLE_COLS = 10;
            for (int col = 0; col < TABLE_COLS; col++) {
                ColoredPawnImageView tableImageView = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
                tableImageView.setStudentOfColor(color);
                tableImageView.setVisible(false);
                tablePane.add(tableImageView, col, colorRow);
            }
            colorRow++;
        }

        // Setting up entrance pane
        int ENTRANCE_ROWS = 5;
        int ENTRANCE_COLS = 2;
        for (int row = 0; row < ENTRANCE_ROWS; row ++) {
            for (int col = 0; col < ENTRANCE_COLS; col++) {
                if (row == 0 && col == 0) continue;
                ColoredPawnImageView coloredImageView = new ColoredPawnImageView(ImageFactory.STUDENT_SIZE);
                coloredImageView.setVisible(false);
                entrancePane.add(coloredImageView, col, row);
            }
        }

        // Setting up towers pane
        int TOWERS_ROWS = 4;
        int TOWERS_COLS = 2;
        for (int row = 0; row < TOWERS_ROWS; row ++) {
            for (int col = 0; col < TOWERS_COLS; col++) {
                Image towerImage;
                switch (team.getTeamName()) {
                    case "BLACK":
                        towerImage = ImageFactory.blackTowerImage;
                        break;
                    case "WHITE":
                        towerImage = ImageFactory.whiteTowerImage;
                        break;
                    case "CYAN":
                        towerImage = ImageFactory.greyTowerImage;
                        break;
                    default: throw new InvalidParameterException("Invalid team");
                }
                SizedImageView coloredImageView = new SizedImageView(ImageFactory.TOWER_SIZE, towerImage);
                coloredImageView.setVisible(false);
                towersPane.add(coloredImageView, col, row);
            }
        }

        dashboardClicked = mouseEvent -> {

            try {
                manageInput(mouseEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public void setEntranceStudents(List<PawnColor> students) {
        for (int n = 0; n < entrancePane.getChildren().size(); n++) {
            ColoredPawnImageView coloredImageView = (ColoredPawnImageView) entrancePane.getChildren().get(n);
            if (n < students.size()) {
                coloredImageView.setStudentOfColor(students.get(n));
            } else {
                coloredImageView.setVisible(false);
            }
        }
    }

    public void setTableStudents(int students, PawnColor color) {
        for (int i = 0; i < tablePane.getChildren().size(); i++) {
            ColoredPawnImageView coloredImageView = (ColoredPawnImageView) tablePane.getChildren().get(i);

            while (color != coloredImageView.getColor()) {
                i += 10;
                coloredImageView = (ColoredPawnImageView) tablePane.getChildren().get(i);
            }

            if (students == 0) break;


            if (!coloredImageView.isVisible()) {
                coloredImageView.setVisible(true);
                students--;
            }
        }

        if (students > 0) throw new InvalidParameterException("Too many students");
    }

    public void setProfessors(PawnColor color, boolean isPresent) {
        for (Node child : professorPane.getChildren()) {
            ColoredPawnImageView coloredImageView = (ColoredPawnImageView) child;
            if (coloredImageView.getColor() == color) {
                if (isPresent) coloredImageView.setVisible(true);
                else coloredImageView.setVisible(false);
                return;
            }
        }
    }

    public void setTowers(int towers) {
        for (Node child : towersPane.getChildren()) {
            SizedImageView sizedImageView = (SizedImageView) child;
            if (towers > 0) {
                sizedImageView.setVisible(true);
                towers--;
            } else {
                sizedImageView.setVisible(false);
            }
        }
    }

    public void listenToInput(MoveRequestMessage requestMessage, PawnColor choosenColor) {
        dashboardImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, dashboardClicked);
        this.requestMessage = requestMessage;
        this.choosenColor = choosenColor;

    }

    public void manageInput(MouseEvent mouseEvent) throws IOException {

        if(mouseEvent.getButton() == MouseButton.PRIMARY) {

            EriantysClient.getInstance().sendToServer(
                    new PerformedMoveMessage(
                            requestMessage,
                            new MoveStudent(
                                    ColoredPawnOriginDestination.TABLE,
                                    ((MoveStudentRequest) requestMessage.moveRequest).toWhere,
                                    -1, choosenColor
                                    )));
            stopListeningToInput();
            for (IslandGUIComponent island : controller.getIslandGUIComponents()) {
                island.stopListeningToInput();
            }
        }
    }

    public void stopListeningToInput() {
        dashboardImageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, dashboardClicked);
    }

}
