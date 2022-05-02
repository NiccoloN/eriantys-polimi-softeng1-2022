package it.polimi.ingsw2022.eriantys.client.view.cli.states;

import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.components.CharacterCardCLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a cli state in which the user is asked to select a character card. A character selection state is an optional state
 * of the cli that will only be set by another and can get back to the previous state if a specific action is triggered
 * @author Niccolò Nicolosi
 */
public class CharacterSelection extends CLIState {

    private final CLIState prevState;
    private final Action goBackAction;
    private int currentSelectedIndex;
    private CharacterCardCLIComponent currentSelected, prevSelected;

    /**
     * Constructs a character selection state
     * @param cli the cli this state is linked to
     * @param prevState the previous state from which this state was set
     * @param goBackAction the action that makes the cli go back to the previous state if triggered
     */
    CharacterSelection(EriantysCLI cli, CLIState prevState, Action goBackAction) {

        super(cli, new BasicCLIComponent(1, new String[] {GREEN + BLINKING + "V" + RESET }));
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
        cli.getHintTextArea().setText("Select a character card:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↓ or s to return to your previous selection");
        updateCLI();
    }

    @Override
    public void exit() {

        currentSelected.setColor(RESET);
        cli.getHintTextArea().setText("");
        cli.getEffectTextArea().setText("");
        cli.setPrompt(null);
    }

    @Override
    public void manageInput(Input input) {

        if(input.triggersAction(goBackAction)) {

            cli.setState(prevState);
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = cli.getNumberOfCharacters() - 1;
        else if (currentSelectedIndex > cli.getNumberOfCharacters() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = cli.getCharacter(currentSelectedIndex);
        currentSelected.setColor(GREEN);

        if(currentSelected != prevSelected)
            cli.getEffectTextArea().setText(currentSelected.getEffect() + "\n\n" + YELLOW + "Cost" + BLACK + ": " + currentSelected.getCost());
        prevSelected = currentSelected;

        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 1);
    }
}
