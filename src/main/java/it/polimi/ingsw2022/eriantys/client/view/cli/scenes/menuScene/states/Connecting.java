package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;

/**
 * This class represents a menu scene state in which the user is notified that the client is trying to connect to the server
 * @author Niccolò Nicolosi
 */
public class Connecting extends MenuSceneState {

    /**
     * Constructs a connecting state
     * @param cli the cli associated to this state
     * @param scene the menu scene associated to this state
     */
    protected Connecting(EriantysCLI cli, MenuScene scene) {

        super(cli, scene);
    }

    @Override
    public void enter() {

        getScene().getConnectingLabel().setHidden(false);
        EriantysClient.getInstance().connectToServer();
    }

    @Override
    public void exit() {

        getScene().getConnectingLabel().setHidden(true);
    }

    @Override
    public void manageInput(Input input) {}
}
