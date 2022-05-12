package it.polimi.ingsw2022.eriantys.client.view.cli.scenes;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;

import java.io.IOException;

/**
 * This class represents scene of the cli. A scene contains all the components to visualize on the cli if the scene is set.
 * A scene also takes care of managing inputs based on it's state
 * @see CLIComponent
 * @see CLISceneState
 * @author Niccolò Nicolosi
 */
public abstract class CLIScene {

    private final EriantysCLI cli;
    private final int width, height;

    private CLISceneState state;

    /**
     * Constructs a scene associated to the given cli
     * @param cli the cli to associate to this scene
     * @param width the width of this scene
     * @param height the height of this scene
     */
    public CLIScene(EriantysCLI cli, int width, int height) {

        this.cli = cli;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the current state of the scene, exiting the previous and entering the given one
     * @param state the new state of the scene
     */
    public void setState(CLISceneState state) {

        if(this.state != null) this.state.exit();
        this.state = state;
        state.enter();
    }

    /**
     * Manages the given input based on the current state
     * @param input the input to manage
     * @throws IOException if an I/O exception occurs
     */
    public void manageInput(Input input) throws IOException {

        state.manageInput(input);
    }

    /**
     * Prints this scene to the given frame
     * @param frame the frame to print to
     */
    public abstract void printToFrame(Frame frame);

    /**
     * @return the cli associate to this scene
     */
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
