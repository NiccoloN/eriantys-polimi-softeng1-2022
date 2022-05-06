package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

public class Connecting extends CLISceneState{

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

    @Override
    public MenuScene getScene() {

        return (MenuScene) super.getScene();
    }
}
