package it.polimi.ingsw2022.eriantys.client.view.gui.components;

import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.ImageFactory;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardGUIComponent {

    private final int MAX_TABLE_STUDENTS = 10;
    private final HashMap<PawnColor, Integer> COLOR_ROW_INDEX;
    private final HashMap<PawnColor, String> COLOR_TO_ID;

    GridPane entrancePane;
    GridPane tablePane;
    GridPane professorPane;
    GridPane towersPane;

    private final HashMap<PawnColor, Integer> entranceStudents;
    private final HashMap<PawnColor, Integer> tableStudents;
    private final HashMap<PawnColor, Boolean> professors;
    int towers;

    public DashboardGUIComponent(GridPane entrancePane, GridPane tablePane, GridPane professorsPane, GridPane towersPane) {
        COLOR_ROW_INDEX = new HashMap<>();
        COLOR_TO_ID = new HashMap<>();

        this.entrancePane = entrancePane;
        this.tablePane = tablePane;
        this.professorPane = professorsPane;
        this.towersPane = towersPane;

        entranceStudents = new HashMap<>();
        tableStudents = new HashMap<>();
        professors = new HashMap<>();

        int colorRow = 0;
        for (PawnColor color : PawnColor.values()) {
            entranceStudents.put(color, 0);
            tableStudents.put(color, 0);
            professors.put(color, false);
            COLOR_ROW_INDEX.put(color, colorRow);
            colorRow++;
        }

        towers = 0;

        COLOR_TO_ID.put(PawnColor.RED, "#RED");
        COLOR_TO_ID.put(PawnColor.YELLOW, "#YELLOW");
        COLOR_TO_ID.put(PawnColor.BLUE, "#BLUE");
        COLOR_TO_ID.put(PawnColor.PINK, "#PINK");
        COLOR_TO_ID.put(PawnColor.GREEN, "#GREEN");
    }

    private int[] getEmptyEntranceNode() {
        for (Node node : entrancePane.getChildren()) {
            if (node.getId() == null) {
                int[] coords = {GridPane.getColumnIndex(node), GridPane.getRowIndex(node)};
                return coords;
            }
        }
        return new int[] {-1, -1};
    }

    public void addStudentToEntrance(PawnColor color) {
        entranceStudents.put(color, entranceStudents.get(color) + 1);

        Image studentImage = ImageFactory.studentsImages.get(color);
        ImageView studentImageView = new ImageView(studentImage);
        studentImageView.setId(COLOR_TO_ID.get(color));

        int [] emptyNodeCoords = getEmptyEntranceNode();
        entrancePane.add(studentImageView, emptyNodeCoords[0], emptyNodeCoords[1]);
    }

    public void removeStudentFromEntrance(PawnColor color) {
        entranceStudents.put(color, entranceStudents.get(color) - 1);

        entrancePane.getChildren().remove(entrancePane.lookup(COLOR_TO_ID.get(color)));
    }

    public void addStudentToTable(PawnColor color) {
        if (tableStudents.get(color) < MAX_TABLE_STUDENTS) {
            int newStudentIndex = tableStudents.get(color);
            tableStudents.put(color, tableStudents.get(color) + 1);

            Image studentImage = ImageFactory.studentsImages.get(color);
            tablePane.add(new ImageView(studentImage), newStudentIndex, COLOR_ROW_INDEX.get(color));
        } else {
            throw new RuntimeException("Max number of students to a table reached");
        }
    }

    public void addProfessor(PawnColor color) {
        professors.put(color, true);

        Image professorImage = ImageFactory.professorsImages.get(color);
        professorPane.add(new ImageView(professorImage), 0, COLOR_ROW_INDEX.get(color));
    }

    public void removeProfessor(PawnColor color) {
        professors.remove(color, false);

        professorPane.getChildren().removeIf(
                node -> GridPane.getColumnIndex(node) == 0 &&
                        GridPane.getRowIndex(node) == COLOR_ROW_INDEX.get(color));
    }

    public void addTower() {
        towers++;
    }

    public void removeTower() {
        towers--;
    }
}
