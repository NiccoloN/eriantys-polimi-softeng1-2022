package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.MenuScene;
import it.polimi.ingsw2022.eriantys.messages.Message;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

public class NumberOfPlayersSelection extends MenuSceneState {

    private final Message requestMessage;
    private int currentSelectedIndex;
    private BasicCLIComponent currentSelected;

    public NumberOfPlayersSelection(EriantysCLI cli, MenuScene scene, Message requestMessage) {

        super(cli, scene);
        this.requestMessage = requestMessage;
    }

    @Override
    public void enter() {

        BlinkingCLIComponent prompt = new BlinkingCLIComponent(1, new String[] {"V"});
        prompt.setFirstColor(GREEN_BRIGHT + BLUE_BACKGROUND_BRIGHT);
        prompt.setSecondColor(GREEN + BLUE_BACKGROUND_BRIGHT);

        getScene().setPrompt(prompt);

        getScene().getSelectNumberOfPlayersPrompt().setHidden(false);
        for(int n = 0; n < getScene().getNumberOfPlayerNumbers(); n++) getScene().getPlayerNumber(n).setHidden(false);

        currentSelectedIndex = 0;
        updateCLI();
    }

    @Override
    public void exit() {

        getScene().setPrompt(null);

        getScene().getSelectNumberOfPlayersPrompt().setHidden(true);
        for(int n = 0; n < getScene().getNumberOfPlayerNumbers(); n++) getScene().getPlayerNumber(n).setHidden(true);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if (input.triggersAction(Action.SELECT)) {

            getScene().setState(new GameModeSelection(getCli(), getScene(), currentSelectedIndex + 2, requestMessage));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfPlayerNumbers() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfPlayerNumbers() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = getScene().getPlayerNumber(currentSelectedIndex);
        currentSelected.setColor(GREEN);

        getScene().getPrompt().setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 2);
    }
}
