package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.Serializable;

public class SchoolChange implements Change, Serializable {

    private final SchoolDashboard schoolDashboard;

    public SchoolChange(SchoolDashboard schoolDashboard) {

        this.schoolDashboard = schoolDashboard;
        for (ColoredPawn color: this.schoolDashboard.getEntranceStudents())
            System.out.println(color.color.toString());
    }

    @Override
    public void apply(GameScene scene) {

        PlayerStatusCLIComponent player = scene.getPlayer(schoolDashboard.player.username);

        for (PawnColor color : PawnColor.values()) {

            player.setEntranceStudents(color, schoolDashboard.countEntranceStudents(color));
            player.setTableStudents(color, schoolDashboard.countTableStudents(color));
            player.setProfessor(color, schoolDashboard.containsProfessor(color));
            player.setTowers(schoolDashboard.getTowers());
        }
    }
}
