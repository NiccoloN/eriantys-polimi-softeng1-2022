package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.CloudCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseCloud;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.GREEN;

/**
 * This class represents a game scene state in which the user is asked to select a cloud
 *
 * @author Niccolò Nicolosi
 */
public class CloudSelection extends GameSceneState {
    
    private int currentSelectedIndex;
    private CloudCLIComponent currentSelected;
    
    /**
     * Constructs a cloud selection state
     *
     * @param cli            the cli to associate to this state
     * @param scene          the game scene to associate to this state
     * @param requestMessage the message that requested this state or the previous one
     */
    public CloudSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {
        
        super(cli, scene, requestMessage);
    }
    
    @Override
    public void enter() {
        
        super.enter();
        currentSelectedIndex = 0;
        updateCLI();
    }
    
    @Override
    public void exit() {
        
        super.exit();
        currentSelected.setColor(CloudCLIComponent.DEFAULT_COLOR);
    }
    
    @Override
    public void manageInput(Input input) throws IOException {
        
        if (input.triggersAction(Action.DOWN) && getScene().gameMode == GameMode.EXPERT) {
            
            getScene().setState(new CharacterSelection(getCli(), getScene(), requestMessage, this, Action.UP));
            return;
        }
        
        if (input.triggersAction(Action.SELECT)) {
            
            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage, new ChooseCloud(currentSelected.getIndex())));
            
            getScene().setState(new ViewOnly(getCli(), getScene()));
            return;
        }
        
        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;
        
        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfClouds() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfClouds() - 1) currentSelectedIndex = 0;
        
        updateCLI();
    }
    
    private void updateCLI() {
        
        if (currentSelected != null) currentSelected.setColor(CloudCLIComponent.DEFAULT_COLOR);
        currentSelected = getScene().getCloud(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 1);
    }
}
