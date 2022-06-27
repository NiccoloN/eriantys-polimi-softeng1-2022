package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.IslandSelection;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;

/**
 * This class represents the request of moving mother nature. The controller sets the attribute motherNatureMaxSteps
 * that is used by the client to display how much can mother nature move. The attribute additionalSteps is used
 * to increase by two the steps of mother nature due to the effect of a character card.
 * @author Emanuele Musto
 */
public class MoveMotherNatureRequest extends MoveRequest {

    public final int motherNatureMaxSteps;
    private static boolean additionalSteps = false;

    public MoveMotherNatureRequest(int maxMotherNatureMaxSteps) {

        super("Choose how many steps mother nature will take");
        if(!additionalSteps) this.motherNatureMaxSteps = maxMotherNatureMaxSteps;
        else this.motherNatureMaxSteps = maxMotherNatureMaxSteps + 2;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setState(new IslandSelection(cli, scene, requestMessage, motherNatureMaxSteps));
    }

    public static void setAdditionalSteps(boolean additionalSteps) {
        MoveMotherNatureRequest.additionalSteps = additionalSteps;
    }
}
