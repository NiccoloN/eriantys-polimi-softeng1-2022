package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.SchoolGUIComponent;
import it.polimi.ingsw2022.eriantys.server.model.board.SchoolDashboard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a change in the school dashboard. Whenever it changes, the clients will be updated with this change.
 * @author Francesco Melegati Maccari
 */
public class SchoolChange implements Change, Serializable {
    
    private final SchoolDashboard schoolDashboard;
    
    public SchoolChange(SchoolDashboard schoolDashboard) {
        
        this.schoolDashboard = schoolDashboard;
    }
    
    @Override
    public void apply(GameScene scene) {
        
        PlayerStatusCLIComponent player = scene.getPlayer(schoolDashboard.player.getUsername());
        
        for (PawnColor color : PawnColor.values()) {
            
            player.setEntranceStudents(color, schoolDashboard.countEntranceStudents(color));
            player.setTableStudents(color, schoolDashboard.countTableStudents(color));
            player.setProfessor(color, schoolDashboard.containsProfessor(color));
            player.setTowers(schoolDashboard.getTowers());
        }
    }
    
    @Override
    public void apply(GameController controller) {
        
        List<PawnColor> entranceStudents = new ArrayList<>();
        
        Platform.runLater(() -> {
            
            SchoolGUIComponent schoolGUIComponent = controller.getSchoolGUIComponentOfPlayer(schoolDashboard.player.getUsername());
            
            for (PawnColor color : PawnColor.values()) {
                
                schoolGUIComponent.setTableStudents(schoolDashboard.countTableStudents(color), color);
                schoolGUIComponent.setProfessor(color, schoolDashboard.containsProfessor(color));
                
                for (int n = 0; n < schoolDashboard.countEntranceStudents(color); n++) entranceStudents.add(color);
            }
            
            schoolGUIComponent.setEntranceStudents(entranceStudents);
            schoolGUIComponent.setTowers(schoolDashboard.getTowers());
        });
    }
}
