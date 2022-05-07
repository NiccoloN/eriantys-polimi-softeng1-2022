package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

public abstract class MenuSceneState extends CLISceneState {

    public MenuSceneState(EriantysCLI cli, MenuScene scene) {

        super(cli, scene);
    }

    @Override
    public MenuScene getScene() {

        return (MenuScene) super.getScene();
    }
}
