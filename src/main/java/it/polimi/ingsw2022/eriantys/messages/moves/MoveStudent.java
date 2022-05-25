package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.Serializable;

/**
 * This class represents the movement of a student. It can be to the dining table or to an island.
 * If it's towards an island it specifies that island index.
 * @author Emanuele Musto
 */
public class MoveStudent implements Move, Serializable {

    private final boolean toDining, toIsland;
    private final int islandIndex;
    private final PawnColor studentColor;


    public MoveStudent(boolean toDining, boolean toIsland, int islandIndex, PawnColor studentColor) {

        this.toDining = toDining;
        this.toIsland = toIsland;
        this.islandIndex = islandIndex;
        this.studentColor = studentColor;
    }

    @Override
    public String apply(Game game) {

        ColoredPawn movedStudent = game.getCurrentPlayer().getSchool().removeFromEntrance(studentColor);

        if (toDining) {

            SchoolDashboard school = game.getCurrentPlayer().getSchool();
            school.addToTable(movedStudent);
            game.checkAndUpdateProfessor(studentColor);
        }

        if (toIsland) game.getBoard().getIsland(islandIndex).addStudent(movedStudent);
        return null;
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        if (toIsland) {

            IslandChange islandChange = new IslandChange(islandIndex, game.getBoard().getIsland(islandIndex));
            update.addChange(islandChange);
        }

        for (Player player : game.getPlayers()) update.addChange(new SchoolChange(player.getSchool()));

        return(update);
    }
}
