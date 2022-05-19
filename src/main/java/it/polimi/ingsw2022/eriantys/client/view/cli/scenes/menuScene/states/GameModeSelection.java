package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettings;
import it.polimi.ingsw2022.eriantys.messages.toServer.GameSettingsMessage;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a menu scene state in which the user is asked to select a game mode
 * @author Niccolò Nicolosi
 */
public class GameModeSelection extends MenuSceneState {

    private final int selectedNumberOfPlayers;
    private final Message requestMessage;

    private int currentSelectedIndex;
    private BasicCLIComponent currentSelected;

    /**
     * Constructs a game mode selection state
     * @param cli the cli associated to this state
     * @param scene the menu scene associated to this state
     */
    protected GameModeSelection(EriantysCLI cli, MenuScene scene, int selectedNumberOfPlayers, Message requestMessage) {

        super(cli, scene);
        this.selectedNumberOfPlayers = selectedNumberOfPlayers;
        this.requestMessage = requestMessage;
    }

    @Override
    public void enter() {

        BlinkingCLIComponent prompt = new BlinkingCLIComponent(1, new String[] {">"});
        prompt.setFirstColor(GREEN_BRIGHT);
        prompt.setSecondColor(GREEN);

        getScene().setPrompt(prompt);

        getScene().getPanel().setHidden(false);
        getScene().getSelectGameModePrompt().setHidden(false);
        for(int n = 0; n < getScene().getNumberOfGameModes(); n++) getScene().getGameMode(n).setHidden(false);

        currentSelectedIndex = 0;
        updateCLI();
    }

    @Override
    public void exit() {

        getScene().setPrompt(null);

        getScene().getPanel().setHidden(true);
        getScene().getSelectGameModePrompt().setHidden(true);
        for(int n = 0; n < getScene().getNumberOfGameModes(); n++) getScene().getGameMode(n).setHidden(true);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if (input.triggersAction(Action.SELECT)) {

            EriantysClient.getInstance().sendToServer(new GameSettingsMessage(requestMessage,
                    new GameSettings(selectedNumberOfPlayers, Mode.values()[currentSelectedIndex])));

            //TODO abort message
        }

        if (input.triggersAction(Action.UP)) currentSelectedIndex--;
        else if (input.triggersAction(Action.DOWN)) currentSelectedIndex++;

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfGameModes() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfGameModes() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = getScene().getGameMode(currentSelectedIndex);
        currentSelected.setColor(GREEN);

        getScene().getPrompt().setPosition(currentSelected.getFrameX() - 2, currentSelected.getFrameY());
    }
}
