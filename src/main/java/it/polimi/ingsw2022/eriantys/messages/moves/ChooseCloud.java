package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.HelperCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents the choice of one cloud by a player
 * @author Emanuele Musto
 */
public class ChooseCloud implements Move, Serializable {

    int cloudIndex;

    public ChooseCloud(int cloudIndex) {

        this.cloudIndex = cloudIndex;
    }

    @Override
    public void apply(Game game, String playerUsername) {

        SchoolDashboard school = game.getPlayer(playerUsername).getSchool();
        List<ColoredPawn> students = game.getBoard().getCloud(cloudIndex).withdrawStudents();
        for (ColoredPawn student : students) school.addToEntrance(student);
    }

    @Override
    public Update getUpdate(Game game, String playerUsername) {

        Update update = new Update();

        CloudChange cloudChange = new CloudChange(cloudIndex, game.getBoard().getCloud(cloudIndex));
        SchoolChange schoolChange = new SchoolChange(game.getPlayer(playerUsername).getSchool());

        update.addChange(cloudChange);
        update.addChange(schoolChange);

        return update;
    }
}
