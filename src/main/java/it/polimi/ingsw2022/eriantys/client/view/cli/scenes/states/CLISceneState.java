package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;

import java.io.IOException;

/**
 * This class represents a state of the cli
 * @see EriantysCLI
 * @author Niccolò Nicolosi
 */
public abstract class CLISceneState {

    private final EriantysCLI cli;
    private final CLIScene scene;

    /**
     * Constructs a cli state, setting its prompt component
     * @param cli the cli this state is linked to
     * @param prompt the prompt component of this state
     */
    protected CLISceneState(EriantysCLI cli, CLIScene scene) {

        this.cli = cli;
        this.scene = scene;
    }

    /**
     * Makes the cli enter this state
     */
    public abstract void enter();

    /**
     * Makes the cli exit this state
     */
    public abstract void exit();

    /**
     * Makes the cli react to the given input (if the given input is client-side) and notifies the controller of the resulting events if needed
     * @param input the received input
     */
    public abstract void manageInput(Input input) throws IOException;

    public EriantysCLI getCli() {

        return cli;
    }

    public CLIScene getScene() {

        return scene;
    }
}
