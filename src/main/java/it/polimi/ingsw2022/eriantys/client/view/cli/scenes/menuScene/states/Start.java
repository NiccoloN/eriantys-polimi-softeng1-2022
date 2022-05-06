package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

public class Start extends CLISceneState {

    public Start(EriantysCLI cli, MenuScene scene) {

        super(cli, scene);
    }

    @Override
    public void enter() {

        getScene().getPromptLabel().setHidden(false);
    }

    @Override
    public void exit() {

        getScene().getPromptLabel().setHidden(true);
    }

    @Override
    public void manageInput(Input input) {

        if(input.triggersAction(Action.SELECT)) getScene().setState(new Connecting(getCli(), getScene()));
    }

    @Override
    public MenuScene getScene() {

        return (MenuScene) super.getScene();
    }
}
