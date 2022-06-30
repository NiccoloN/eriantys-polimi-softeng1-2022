package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CloudChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseCloudRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;

import java.util.List;

/**
 * This class represents the choice of one cloud by a player
 *
 * @author Emanuele Musto
 */
public class ChooseCloud extends Move {
    
    int cloudIndex;
    
    public ChooseCloud(int cloudIndex) {
        
        this.cloudIndex = cloudIndex;
    }
    
    @Override
    public boolean isValid(Game game, MoveRequest request) {
        
        if (!(request instanceof ChooseCloudRequest)) {
            
            errorMessage = "Move not requested";
            return false;
        }
        
        if (game.getBoard().getCloud(cloudIndex).isEmpty()) {
            
            errorMessage = "Cannot choose an empty cloud";
            return false;
        }
        
        return true;
    }
    
    @Override
    public void apply(Game game) {
        
        SchoolDashboard school = game.getCurrentPlayer().getSchool();
        List<ColoredPawn> students = game.getBoard().getCloud(cloudIndex).withdrawStudents();
        for (ColoredPawn student : students) school.addToEntrance(student);
    }
    
    @Override
    public Update getUpdate(Game game) {
        
        Update update = new Update();
        
        CloudChange cloudChange = new CloudChange(cloudIndex, game.getBoard().getCloud(cloudIndex));
        SchoolChange schoolChange = new SchoolChange(game.getCurrentPlayer().getSchool());
        
        update.addChange(cloudChange);
        update.addChange(schoolChange);
        
        return update;
    }
}
