package it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.HashMap;


public class DashboardGUIComponent {

    private final HashMap<PawnColor, Integer> COLOR_ROW_INDEX;

    private final int ENTRANCE_COLS = 2;
    private final int ENTRANCE_ROWS = 5;
    private final int TABLE_COLS = 10;

    private GridPane entrancePane;
    private GridPane tablePane;
    private GridPane professorPane;
    private GridPane towersPane;

    public DashboardGUIComponent(GridPane entrancePane, GridPane tablePane, GridPane professorPane, GridPane towersPane) {
        this.entrancePane = entrancePane;
        this.tablePane = tablePane;
        this.professorPane = professorPane;
        this.towersPane = towersPane;

        // Initializing utils data structures
        COLOR_ROW_INDEX = new HashMap<>();

        int colorRow = 0;
        for (PawnColor color : PawnColor.values()) {
            COLOR_ROW_INDEX.put(color, colorRow);

            // Setting up professors pane
            ColoredImageView professorsImageView = new ColoredImageView();
            professorsImageView.setColor(color);
            professorPane.add(professorsImageView, 0, colorRow);

            // Setting up table pane
            for (int col = 0; col < TABLE_COLS; col++) {
                ColoredImageView tableImageView = new ColoredImageView();
                tableImageView.setColor(color);
                tablePane.add(tableImageView, col, colorRow);
            }
            colorRow++;
        }

        // Populating the entrance grid with ImageViews
        for (int row = 0; row < ENTRANCE_ROWS; row ++) {
            for (int col = 0; col < ENTRANCE_COLS; col++) {
                ColoredImageView coloredImageView = new ColoredImageView();
                entrancePane.add(coloredImageView, col, row);
            }
        }

    }

    public void addStudentToEntrance(PawnColor color) {
        Image studentImage = ImageFactory.studentsImages.get(color);
        for (Node child : entrancePane.getChildren()) {
            ColoredImageView coloredImageView = (ColoredImageView) child;
            if ( coloredImageView.getImage() == null) {
                coloredImageView.setImage(ImageFactory.studentsImages.get(color));
                coloredImageView.setColor(color);
                return;
            }
        }
    }

    public void removeStudentFromEntrance(PawnColor color) {
        for (Node child : entrancePane.getChildren()) {
            ColoredImageView coloredImageView = (ColoredImageView) child;
            if (coloredImageView.getColor() == color) {
                coloredImageView.setImage(null);
                coloredImageView.setColor(null);
                return;
            }
        }
    }

    public void addStudentToTable(PawnColor color) {
        for (Node child : tablePane.getChildren()) {
            ColoredImageView coloredImageView = (ColoredImageView) child;
            if (coloredImageView.getColor() == color && coloredImageView.getImage() == null) {
                coloredImageView.setImage(ImageFactory.studentsImages.get(color));
                return;
            }
        }
    }

    public void addProfessor(PawnColor color) {
        for (Node child : professorPane.getChildren()) {
            ColoredImageView coloredImageView = (ColoredImageView) child;
            if (coloredImageView.getColor() == color) {
                coloredImageView.setImage(ImageFactory.studentsImages.get(color));
                return;
            }
        }
    }

    public void removeProfessor(PawnColor color) {
        for (Node child : professorPane.getChildren()) {
            ColoredImageView coloredImageView = (ColoredImageView) child;
            if (coloredImageView.getColor() == color) {
                coloredImageView.setImage(null);
                return;
            }
        }
    }

    // TODO: towers

}
