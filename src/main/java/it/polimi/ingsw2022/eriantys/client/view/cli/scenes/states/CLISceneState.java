package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;

import java.io.IOException;

/**
 * This class represents a state of a cli scene
 * @see CLIScene
 * @author Niccolò Nicolosi
 */
public abstract class CLISceneState {

    private final EriantysCLI cli;
    private final CLIScene scene;

    /**
     * Constructs a scene state
     * @param cli the cli this state is associated to
     * @param scene the scene this state is associated to
     */
    public CLISceneState(EriantysCLI cli, CLIScene scene) {

        this.cli = cli;
        this.scene = scene;
    }

    /**
     * Makes the associated scene enter this state
     */
    public abstract void enter();

    /**
     * Makes the associated scene exit this state
     */
    public abstract void exit();

    /**
     * Makes the associated scene react to the given input (if the given input is client-side) and notifies the server of the resulting events if needed
     * @param input the received input
     */
    public abstract void manageInput(Input input) throws IOException;

    /**
     * @return the cli associated to this state
     */
    public EriantysCLI getCli() {

        return cli;
    }

    /**
     * @return the scene associated to this state
     */
    public CLIScene getScene() {

        return scene;
    }
}
