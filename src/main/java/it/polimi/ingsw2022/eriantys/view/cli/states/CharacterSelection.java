package it.polimi.ingsw2022.eriantys.view.cli.states;

import it.polimi.ingsw2022.eriantys.view.cli.Action;
import it.polimi.ingsw2022.eriantys.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.view.cli.Input;
import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.CharacterCardCLIComponent;

import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

public class CharacterSelection extends CLIState {

    private final CLIState prevState;
    private final Action goBackAction;
    private int currentSelectedIndex;
    private CharacterCardCLIComponent currentSelected, prevSelected;

    /**
     * Constructs a character selection state. A character selection state is an optional state of the game that will only be set by another
     * and can get back to the previous state if a specific action is triggered
     * @param cli the cli this state is linked to
     * @param prevState the previous state from which this state was set
     * @param goBackAction the action that goes back to the previous state if triggered
     */
    CharacterSelection(EriantysCLI cli, CLIState prevState, Action goBackAction) {

        super(cli, new CLIComponent(1, new String[] {GREEN + BLINKING + "V" + RESET }));
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
        updateCLI();
    }

    @Override
    public void exit() {

        currentSelected.setColor(RESET);
        cli.setPrompt(null);
        cli.getEffectTextArea().setText("");
    }

    @Override
    public void manageInput(Input input) {

        if(input.ofAction(goBackAction)) {

            cli.setState(prevState);
            return;
        }

        if (input.ofAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.ofAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = cli.getNumberOfCharacters() - 1;
        else if (currentSelectedIndex > cli.getNumberOfCharacters() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = cli.getCharacter(currentSelectedIndex);
        currentSelected.setColor(GREEN);

        if(currentSelected != prevSelected) cli.getEffectTextArea().setText("Choose a type of Student: every player (including yourself) must return 3 Students of that type " +
                                        "from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as " +
                                        "many Students as they have\n\n" + YELLOW + "Cost" + BLACK + ": 03");
        prevSelected = currentSelected;

        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2, currentSelected.getFrameY() - 1);
    }
}
