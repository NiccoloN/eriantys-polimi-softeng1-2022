package it.polimi.ingsw2022.eriantys.messages.changes;

import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;

/**
 * This class is a generic change. It's a representation of a portion of the model, that will be added to an update when
 * necessary, in order to update the view of the clients.
 * @see Update
 * @author Francesco Melegati Maccari
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public interface Change {

    /**
     * Calls the right method of the view in order to apply the change happened in the model, capture in this change.
     * @param scene the scene that will receive the change.
     */
    void apply(GameScene scene);

    void apply(GameController controller);
}
