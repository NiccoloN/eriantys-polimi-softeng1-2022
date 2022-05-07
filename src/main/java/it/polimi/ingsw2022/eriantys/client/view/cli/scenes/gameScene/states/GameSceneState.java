package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.GREEN;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.GREEN_BRIGHT;

public abstract class GameSceneState extends CLISceneState {

    protected BlinkingCLIComponent prompt;

    public GameSceneState(EriantysCLI cli, GameScene scene) {

        super(cli, scene);
        prompt = new BlinkingCLIComponent(1, new String[] {"V"});
        prompt.setFirstColor(GREEN_BRIGHT);
        prompt.setSecondColor(GREEN);
    }

    @Override
    public void enter() {

        getScene().setPrompt(prompt);
    }

    @Override
    public void exit() {

        getScene().setPrompt(null);
    }

    @Override
    public GameScene getScene() {

        return (GameScene) super.getScene();
    }
}
