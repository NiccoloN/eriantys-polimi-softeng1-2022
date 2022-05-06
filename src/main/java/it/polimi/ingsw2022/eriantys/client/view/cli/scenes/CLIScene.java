package it.polimi.ingsw2022.eriantys.client.view.cli.scenes;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

import java.io.IOException;

public abstract class CLIScene {

    private final EriantysCLI cli;
    private final int width, height;

    private CLISceneState state;

    public CLIScene(EriantysCLI cli, int width, int height) {

        this.cli = cli;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the current state of the cli, exiting the previous and entering the given one
     * @param state the new state of the cli
     */
    public void setState(CLISceneState state) {

        if(this.state != null) this.state.exit();
        this.state = state;
        state.enter();
    }

    public void manageInput(Input input) throws IOException {

        state.manageInput(input);
    }

    public abstract void printToFrame(Frame frame);

    public EriantysCLI getCli() {

        return cli;
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }
}
