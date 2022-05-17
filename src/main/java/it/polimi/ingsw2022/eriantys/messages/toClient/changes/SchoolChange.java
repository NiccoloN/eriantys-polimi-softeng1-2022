package it.polimi.ingsw2022.eriantys.messages.toClient.changes;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.View;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;
import java.util.Arrays;

public class SchoolChange implements Change, Serializable {

    private final SchoolDashboard schoolDashboard;

    public SchoolChange(SchoolDashboard schoolDashboard) {

        this.schoolDashboard = schoolDashboard;
        for (ColoredPawn color: this.schoolDashboard.getEntranceStudents())
            System.out.println(color.color.toString());
    }

    public String getPlayerUsername() {

        return schoolDashboard.player.username;
    }

    public int getEntranceStudents(PawnColor color) {

        return schoolDashboard.countEntranceStudents(color);
    }

    public int getTableStudents(PawnColor color) {

        return schoolDashboard.countTableStudents(color);
    }

    public boolean hasProfessor(PawnColor color) {

        return schoolDashboard.containsProfessor(color);
    }

    public int getTowers() {

        return schoolDashboard.getTowers();
    }

    @Override
    public void apply(View view) {
        view.applyChange(this);
    }
}
