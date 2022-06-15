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
import java.util.NoSuchElementException;

/**
 * This class represents the movement of a student. It can be to the dining table or to an island.
 * If it's towards an island it specifies that island index.
 * @author Emanuele Musto
 */
public class MoveStudent extends Move {

    private int characterIndex;
    private final boolean toDining;
    private final int islandIndex;
    private final PawnColor studentColor;

    public MoveStudent(boolean toDining, int islandIndex, PawnColor studentColor) {

        this.characterIndex = 0;
        this.toDining = toDining;
        this.islandIndex = islandIndex;
        this.studentColor = studentColor;
    }

    public MoveStudent(boolean toDining, int islandIndex, PawnColor studentColor, int characterIndex) {

        this(toDining, islandIndex, studentColor);
        this.characterIndex = characterIndex;
    }

    @Override
    public boolean isValid(Game game) {

        SchoolDashboard school = game.getCurrentPlayer().getSchool();

        if(characterIndex > 0) {

            try { game.getCharacterOfIndex(characterIndex); }
            catch (NoSuchElementException e) {

                errorMessage = "There's no character of the given index on board";
                return false;
            }
        }

        else if(school.countEntranceStudents(studentColor) <= 0) {

            errorMessage = "There's no student of the selected color in your school entrance";
            return false;
        }

        if(toDining && school.countTableStudents(studentColor) >= 10) {

            errorMessage = "The table of the selected color is already full";
            return false;
        }

        return true;
    }

    @Override
    public void apply(Game game) {

        ColoredPawn studentToMove;

        if(characterIndex > 0) {
            studentToMove = game.getCharacterOfIndex(characterIndex).getStudent(studentColor);
            game.getCharacterOfIndex(characterIndex).removeStudent(studentToMove);
        }
        else studentToMove = game.getCurrentPlayer().getSchool().removeFromEntrance(studentColor);

        if (toDining) {

            SchoolDashboard school = game.getCurrentPlayer().getSchool();
            school.addToTable(studentToMove);
            game.checkAndUpdateProfessor(studentColor,false);
        }

        else game.getBoard().getIsland(islandIndex).addStudent(studentToMove);
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        //TODO character card update

        if (!toDining) {

            IslandChange islandChange = new IslandChange(islandIndex, game.getBoard().getIsland(islandIndex));
            update.addChange(islandChange);
        }

        for (Player player : game.getPlayers()) update.addChange(new SchoolChange(player.getSchool()));

        return(update);
    }
}
