package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;
import java.util.Map;

public class SchoolDashboardChange implements Change, Serializable {

    private int schoolDashboardIndex;
    private Map<PawnColor, Integer> entranceStudents;
    private Map<PawnColor, Integer> tableStudents;
    private Map<PawnColor, Boolean> professors;
    private Integer numberOfTowers;

    public SchoolDashboardChange(int schoolDashboardIndex) {
        this.schoolDashboardIndex = schoolDashboardIndex;
    }

    public int getSchoolDashboardIndex() {
        return schoolDashboardIndex;
    }

    public void setEntranceStudents(PawnColor color, int numberOfStudents) {
        entranceStudents.put(color, numberOfStudents);
    }

    public void setTableStudents(PawnColor color, int numberOfStudents) {
        tableStudents.put(color, numberOfStudents);
    }

    public void setProfessors(PawnColor professorColor) {
        professors.put(professorColor, true);
    }

    public void setNumberOfTowers(Integer numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    @Override
    public void apply(View view) {
    view.applyChange(this);
    }

}
