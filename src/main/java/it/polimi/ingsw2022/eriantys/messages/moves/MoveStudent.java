package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.IslandChange;
import it.polimi.ingsw2022.eriantys.messages.changes.PlayerChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.Board;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class represents the movement of a student. It can be to the dining table or to an island.
 * If it's towards an island it specifies that island index.
 * @author Emanuele Musto
 */
public class MoveStudent extends Move {
    
    private final List<ColoredPawnOriginDestination> toWhere;
    private final ColoredPawnOriginDestination destination;
    private final int islandIndex;
    private final PawnColor studentColor;
    private int characterIndex;
    
    /**
     * Constructor used when the move is caused by a character card, of which the index is saved.
     * @param destination    where the player moved the student.
     * @param toWhere        list of possible places where the student can be placed for the specific move.
     * @param islandIndex    the island index if the student is placed on an island, -1 if not.
     * @param studentColor   the color of the moved student
     * @param characterIndex the index of the character card that caused this move
     */
    public MoveStudent(ColoredPawnOriginDestination destination, List<ColoredPawnOriginDestination> toWhere, int islandIndex, PawnColor studentColor, int characterIndex) {
        
        this(destination, toWhere, islandIndex, studentColor);
        this.characterIndex = characterIndex;
    }
    
    /**
     * @param destination  where the player moved the student.
     * @param toWhere      list of possible places where the student can be placed for the specific move.
     * @param islandIndex  the island index if the student is placed on an island, -1 if not.
     * @param studentColor the color of the moved student
     */
    public MoveStudent(ColoredPawnOriginDestination destination, List<ColoredPawnOriginDestination> toWhere, int islandIndex, PawnColor studentColor) {
        
        this.characterIndex = 0;
        this.destination = destination;
        this.toWhere = toWhere;
        this.islandIndex = islandIndex;
        this.studentColor = studentColor;
    }
    
    @Override
    public boolean isValid(Game game, MoveRequest request) {
        
        if (!(request instanceof MoveStudentRequest)) {
            
            errorMessage = "Move not requested";
            return false;
        }
        
        SchoolDashboard school = game.getCurrentPlayer().getSchool();
        
        if (characterIndex > 0) {
            
            try {
                game.getCharacterOfIndex(characterIndex);
            }
            catch (NoSuchElementException e) {
                
                errorMessage = "There's no character of the given index on board";
                return false;
            }
        }
        else if (school.countEntranceStudents(studentColor) <= 0) {
            
            errorMessage = "There's no student of the selected color in your school entrance";
            return false;
        }
        
        if (destination == ColoredPawnOriginDestination.TABLE && school.countTableStudents(studentColor) >= 10) {
            
            errorMessage = "The table of the selected color is already full";
            return false;
        }
        
        if (!toWhere.contains(destination)) {
            
            errorMessage = "The chosen destination is not valid for the current move";
            return false;
        }
        
        return true;
    }
    
    @Override
    public void apply(Game game) {
        
        ColoredPawn studentToMove;
        
        if (characterIndex > 0) {
            studentToMove = game.getCharacterOfIndex(characterIndex).getStudent(studentColor);
            game.getCharacterOfIndex(characterIndex).removeStudent(studentToMove);
        }
        else studentToMove = game.getCurrentPlayer().getSchool().removeFromEntrance(studentColor);
        
        if (destination == ColoredPawnOriginDestination.TABLE) {
            
            SchoolDashboard school = game.getCurrentPlayer().getSchool();
            school.addToTable(studentToMove);
            game.checkAndUpdateProfessor(studentColor, false);
        }
        else game.getBoard().getIsland(islandIndex).addStudent(studentToMove);
    }
    
    @Override
    public Update getUpdate(Game game) {
        
        Board board = game.getBoard();
        Update update = new Update();
        
        if (destination != ColoredPawnOriginDestination.TABLE) {
            
            IslandChange islandChange = new IslandChange(board.getIslands(), board.getIslandTiles());
            update.addChange(islandChange);
        }
        
        for (Player player : game.getPlayers()) update.addChange(new SchoolChange(player.getSchool()));
        update.addChange(new PlayerChange(game.getCurrentPlayer()));
        
        return (update);
    }
}
