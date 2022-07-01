package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.IslandSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.CharacterGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.IslandGUIComponent;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import javafx.application.Platform;

import java.util.List;

/**
 * This class represents the request of moving mother nature. The controller sets the attribute motherNatureMaxSteps
 * that is used by the client to display how much can mother nature move. The attribute additionalSteps is used
 * to increase by two the steps of mother nature due to the effect of a character card.
 * @author Emanuele Musto
 */
public class MoveMotherNatureRequest extends MoveRequest {
    
    private int motherNatureMaxSteps;
    
    public MoveMotherNatureRequest(int maxMotherNatureMaxSteps, boolean canPlayCharacter) {
        
        super("Choose how many steps mother nature will take");
        this.motherNatureMaxSteps = maxMotherNatureMaxSteps;
        setCanPlayCharacter(canPlayCharacter);
    }
    
    public int getMotherNatureMaxSteps() {
        
        return motherNatureMaxSteps;
    }
    
    public void setMotherNatureMaxSteps(int motherNatureMaxSteps) {
        
        this.motherNatureMaxSteps = motherNatureMaxSteps;
    }
    
    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {
        
        super.manage(cli, scene, requestMessage);
        scene.setState(new IslandSelection(cli, scene, requestMessage, motherNatureMaxSteps));
    }
    
    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {
        
        super.manage(controller, requestMessage);
        
        Platform.runLater(() -> {
            
            int compoundIslands = controller.getNumberOfCompoundIslands();
            int motherNatureCompoundIndex = 0;
            
            List<IslandGUIComponent> guiIslands = controller.getIslandGUIComponents();
            for (IslandGUIComponent guiIsland : guiIslands) {
                
                if (guiIsland.hasMotherNature()) {
                    
                    motherNatureCompoundIndex = guiIsland.getCompoundIslandIndex();
                    break;
                }
            }
            
            int currentCompoundIndex = motherNatureCompoundIndex;
            for (int n = 0; n < motherNatureMaxSteps; n++) {
                
                currentCompoundIndex++;
                currentCompoundIndex %= compoundIslands;
                if (currentCompoundIndex == motherNatureCompoundIndex) break;
                
                for (IslandGUIComponent island : controller.getIslandGUIComponentsOfIndex(currentCompoundIndex)) {
                    
                    island.listenToInput(requestMessage);
                }
            }
            
            if (EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT && canPlayCharacter()) {
                
                for (CharacterGUIComponent character : controller.getCharacterGUIComponents()) {
                    
                    character.stopListeningToInput();
                    character.listenToInput(requestMessage);
                }
            }
        });
    }
}
