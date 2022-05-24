package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;

import java.io.IOException;

/**
 * This class represents the first menu scene state
 * @author Niccolò Nicolosi
 */
public class Start extends MenuSceneState {

    /**
     * Constructs a start state
     * @param cli the cli associated to this state
     * @param scene the menu scene associated to this state
     */
    public Start(EriantysCLI cli, MenuScene scene) {

        super(cli, scene);
    }

    @Override
    public void enter() {

        getScene().getStartPrompt().setHidden(false);
        getScene().getChangeServerPrompt().setHidden(false);
    }

    @Override
    public void exit() {

        getScene().getStartPrompt().setHidden(true);
        getScene().getChangeServerPrompt().setHidden(true);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(Action.SELECT)) {

            getScene().setState(new Connecting(getCli(), getScene()));
            return;
        }

        if(input.triggersAction(Action.ESC)) {

            getScene().setState(new EnterServerIp(getCli(), getScene()));
        }
    }
}
