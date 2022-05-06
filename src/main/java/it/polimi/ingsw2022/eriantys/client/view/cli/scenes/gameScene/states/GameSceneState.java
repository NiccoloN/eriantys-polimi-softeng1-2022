package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

public class GameSceneState extends CLISceneState {

    protected CLIComponent prompt;

    public GameSceneState(EriantysCLI cli, GameScene scene) {

        super(cli, scene);
    }

    public void setPrompt(CLIComponent prompt) {

        this.prompt = prompt;
    }

    @Override
    public void enter() {

        getScene().setPrompt(prompt);
    }

    @Override
    public void exit() {

    }

    @Override
    public void manageInput(Input input) {

    }

    @Override
    public GameScene getScene() {

        return (GameScene) super.getScene();
    }
}
