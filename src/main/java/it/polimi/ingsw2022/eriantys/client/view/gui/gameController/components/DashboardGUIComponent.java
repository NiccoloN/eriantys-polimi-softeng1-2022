package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.utilityNodes.ColoredPawnImageView;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.security.InvalidParameterException;
import java.util.HashMap;


public class DashboardGUIComponent {

    private final GridPane entrancePane;
    private final GridPane tablePane;
    private final GridPane professorPane;
    private final GridPane towersPane;

    public DashboardGUIComponent(Group school) {
        this.entrancePane = (GridPane) school.getChildren().get(1);
        this.tablePane = (GridPane) school.getChildren().get(2);
        this.professorPane = (GridPane) school.getChildren().get(3);
        this.towersPane = (GridPane) school.getChildren().get(4);

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
                ColoredPawnImageView coloredImageView = new ColoredPawnImageView(ImageFactory.TOWER_SIZE);
                coloredImageView.setVisible(false);
                towersPane.add(coloredImageView, col, row);
            }
        }
    }

    public void setEntranceStudents(int students, PawnColor color) {
        for (Node child : entrancePane.getChildren()) {
            ColoredPawnImageView coloredImageView = (ColoredPawnImageView) child;
            if (students == 0) {
                coloredImageView.setVisible(false);
            } else if (!coloredImageView.isVisible()) {
                coloredImageView.setStudentOfColor(color);
                students--;
            }
        }

        if (students > 0) throw new InvalidParameterException("Too many students");
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
            ColoredPawnImageView coloredImageView = (ColoredPawnImageView) child;
            if (towers > 0) {
                coloredImageView.setVisible(true);
                towers--;
            } else {
                coloredImageView.setVisible(false);
            }
        }
    }
}
