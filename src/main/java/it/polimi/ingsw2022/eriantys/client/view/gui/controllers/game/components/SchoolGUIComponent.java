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
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;


public class SchoolGUIComponent {

    private final Group schoolGroup;
    private final GridPane entrancePane;
    private final GridPane tablePane;
    private final GridPane professorPane;
    private final GridPane towersPane;

    private final EventHandler<MouseEvent> dashboardClicked;
    private MoveRequestMessage requestMessage;
    private PawnColor chosenColor;

    private final GameController gameController;

    public SchoolGUIComponent(Group school, Team team, GameController gameController) {

        schoolGroup = school;
        entrancePane = (GridPane) school.getChildren().get(1);
        tablePane = (GridPane) school.getChildren().get(2);
        professorPane = (GridPane) school.getChildren().get(3);
        towersPane = (GridPane) school.getChildren().get(4);

        this.gameController = gameController;

        ((ImageView) school.getChildren().get(0)).setImage(ImageFactory.schoolImage);

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

            ColoredPawnImageView student = (ColoredPawnImageView) entrancePane.getChildren().get(n);

            if (n < students.size()) student.setStudentOfColor(students.get(n));
            else student.setVisible(false);
        }
    }

    public void setTableStudents(int students, PawnColor color) {

        if (students < 0 || students > 10) throw new InvalidParameterException("Students must be >= 0 and <= 10");

        for (int i = 0; i < tablePane.getChildren().size(); i++) {

            ColoredPawnImageView student = (ColoredPawnImageView) tablePane.getChildren().get(i);

            while (color != student.getColor()) {

                i += 10;
                student = (ColoredPawnImageView) tablePane.getChildren().get(i);
            }

            if (students == 0) break;

            if (!student.isVisible()) student.setVisible(true);
            students--;
        }
    }

    public void setProfessors(PawnColor color, boolean isPresent) {

        for (Node child : professorPane.getChildren()) {

            ColoredPawnImageView professor = (ColoredPawnImageView) child;

            if (professor.getColor() == color) {

                professor.setVisible(isPresent);
                return;
            }
        }
    }

    public void setTowers(int towers) {

        for (Node tower : towersPane.getChildren()) {

            if (towers > 0) {

                tower.setVisible(true);
                towers--;
            }
            else tower.setVisible(false);
        }
    }

    public void listenToInput(MoveRequestMessage requestMessage, PawnColor choosenColor) {

        this.requestMessage = requestMessage;
        this.chosenColor    = choosenColor;
        schoolGroup.addEventHandler(MouseEvent.MOUSE_CLICKED, dashboardClicked);

        schoolGroup.setEffect(gameController.getBorderGlowEffect());
    }

    public void manageInput(MouseEvent mouseEvent) throws IOException {

        if(mouseEvent.getButton() == MouseButton.PRIMARY) {

            EriantysClient.getInstance().sendToServer(
                    new PerformedMoveMessage(
                            requestMessage,
                            new MoveStudent(
                                    ColoredPawnOriginDestination.TABLE,
                                    ((MoveStudentRequest) requestMessage.moveRequest).toWhere,
                                    -1, chosenColor
                                    )));

            stopListeningToInput();
            for (IslandGUIComponent island : gameController.getIslandGUIComponents()) island.stopListeningToInput();
        }
    }

    public void stopListeningToInput() {

        schoolGroup.removeEventHandler(MouseEvent.MOUSE_CLICKED, dashboardClicked);
        schoolGroup.setEffect(null);
    }
}
