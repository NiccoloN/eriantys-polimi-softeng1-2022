package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.messages.Message;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveMotherNature;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene state in which the user is asked to select an island
 * @author Niccolò Nicolosi
 */
public class IslandSelection extends GameSceneState {

    private int currentSelectedIndex;
    private IslandCLIComponent currentSelected;

    private int steps;
    private boolean movingStudent;
    private PawnColor studentColor;
    private int characterIndex;
    private int motherNatureIndex;
    private int motherNatureMaxSteps;

    /**
     * Constructs an island selection state
     * @param cli the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public IslandSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super(cli, scene, requestMessage);
        movingStudent = false;
        studentColor = null;
        characterIndex = 0;
        motherNatureIndex = -1;
        motherNatureMaxSteps = -1;

        prompt = new BlinkingCLIComponent(2, new String[] {"VV"});
        prompt.setFirstColor(GREEN_BRIGHT);
        prompt.setSecondColor(GREEN);
    }

    public IslandSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage, PawnColor studentColor) {

        this(cli, scene, requestMessage);
        this.movingStudent = true;
        this.studentColor = studentColor;
    }

    public IslandSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage, PawnColor studentColor, int characterIndex) {

        this(cli, scene, requestMessage, studentColor);
        this.characterIndex = characterIndex;
    }

    public IslandSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage, int motherNatureMaxSteps) {

        this(cli, scene, requestMessage);
        this.motherNatureIndex = scene.getMotherNatureIslandIndex();
        this.motherNatureMaxSteps = motherNatureMaxSteps;
    }

    @Override
    public void enter() {

        super.enter();
        steps = motherNatureIndex > -1 ? 1 : 0;
        currentSelectedIndex = motherNatureIndex > -1 ? motherNatureIndex + steps : 0;
        getScene().getHintTextArea().setText("Select an island:\nUse ← and → or a and d keys to change your selection and press Enter to confirm\n\n" +
                                      "Press ↓ or s to select a character card");
        updateCLI();
    }

    @Override
    public void exit() {

        super.exit();
        currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        getScene().getHintTextArea().setText("");
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(Action.UP) && movingStudent) {

            getScene().setState(new DiningRoomSelection(getCli(), getScene(), requestMessage, studentColor, this, Action.DOWN));
            return;
        }

        if(input.triggersAction(Action.DOWN) && getScene().gameMode == Mode.EXPERT) {

            getScene().setState(new CharacterSelection(getCli(), getScene(), requestMessage, this, Action.UP));
            return;
        }

        if(input.triggersAction(Action.SELECT)) {

            EriantysClient client = EriantysClient.getInstance();

            if(!movingStudent) client.sendToServer(new PerformedMoveMessage(requestMessage,
                            new MoveMotherNature(currentSelected.getIndex())));
            else if (characterIndex > 0) client.sendToServer(new PerformedMoveMessage(requestMessage,
                    new MoveStudent(false, currentSelected.getIndex(), studentColor)));
            else client.sendToServer(new PerformedMoveMessage(requestMessage,
                        new MoveStudent(false, currentSelected.getIndex(), studentColor)));

            getScene().setState(new ViewOnly(getCli(), getScene()));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) {

            currentSelectedIndex++;

            if(motherNatureIndex > -1) {

                steps++;
                if (steps > motherNatureMaxSteps) steps = 1;
                currentSelectedIndex = motherNatureIndex + steps;
            }

            currentSelectedIndex %= getScene().getNumberOfIslands();
        }

        else if (input.triggersAction(Action.LEFT)) {

            currentSelectedIndex--;
            if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfIslands() - 1;

            if(motherNatureIndex > -1) {

                steps--;
                if (steps < 1) steps = motherNatureMaxSteps;
                currentSelectedIndex = motherNatureIndex + steps;
            }

            currentSelectedIndex %= getScene().getNumberOfIslands();
        }

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        currentSelected = getScene().getIsland(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f - 1, currentSelected.getFrameY() - 1);
    }
}
