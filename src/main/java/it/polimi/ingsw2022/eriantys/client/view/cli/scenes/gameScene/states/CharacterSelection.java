package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.CharacterCardCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.CLISceneState;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.UsernameChoiceMessage;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene state in which the user is asked to select a character card. A character selection state
 * is an optional state of the game scene that will only be set by another and can get back to the previous state if a specific
 * action is triggered
 * @author Niccolò Nicolosi
 */
public class CharacterSelection extends GameSceneState {

    private final CLISceneState prevState;
    private final Action goBackAction;
    private int currentSelectedIndex;
    private CharacterCardCLIComponent currentSelected, prevSelected;

    /**
     * Constructs a character selection state
     * @param cli the cli this state is associated to
     * @param scene the scene this state is associated to
     * @param prevState the previous state from which this state was set
     * @param goBackAction the action that makes the game scene go back to the previous state if triggered
     */
    public CharacterSelection(EriantysCLI cli, GameScene scene, CLISceneState prevState, Action goBackAction) {

        super(cli, scene);

        if(goBackAction == Action.RIGHT || goBackAction == Action.LEFT || goBackAction == Action.SELECT)
            throw new InvalidParameterException("GoBackAction must not be RIGHT, LEFT or SELECT, " +
                                                "because they are already associated to a different behaviour in this state");
        this.prevState = prevState;
        this.goBackAction = goBackAction;
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        prevSelected = null;
        getScene().getHintTextArea().setText("Select a character card:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↓ or s to return to your previous selection");
        updateCLI();
    }

    @Override
    public void exit() {

        currentSelected.setColor(RESET);
        getScene().getHintTextArea().setText("");
        getScene().getEffectTextArea().setText("");
        getScene().setPrompt(null);
    }

    @Override
    public void manageInput(Input input) {

        if(input.triggersAction(goBackAction)) {

            getScene().setState(prevState);
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if(input.triggersAction(Action.SELECT)){
            //TODO
        }

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfCharacters() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfCharacters() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = getScene().getCharacter(currentSelectedIndex);
        currentSelected.setColor(GREEN);

        if(currentSelected != prevSelected)
            getScene().getEffectTextArea().setText(currentSelected.getEffect() + "\n\n" + YELLOW + "Cost" + BLACK + ": " + currentSelected.getCost());
        prevSelected = currentSelected;

        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 1);
    }
}
