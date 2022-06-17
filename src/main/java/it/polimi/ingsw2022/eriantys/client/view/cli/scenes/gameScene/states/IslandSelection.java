package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BlinkingCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseIsland;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveMotherNature;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveMotherNatureRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
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
        currentSelectedIndex = 0;
        if(motherNatureIndex > -1) {

            currentSelectedIndex = motherNatureIndex;
            stepForwardMotherNature();
        }
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
                            new MoveMotherNature(currentSelected.getIndex(), ((MoveMotherNatureRequest) requestMessage.moveRequest).motherNatureMaxSteps)));
            else if (characterIndex > 0) manageCharacters(client);
            else client.sendToServer(new PerformedMoveMessage(requestMessage,
                        new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) requestMessage.moveRequest).toWhere ,currentSelected.getIndex(), studentColor)));

            getScene().setState(new ViewOnly(getCli(), getScene()));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) {

            if(motherNatureIndex > -1) stepForwardMotherNature();
            else currentSelectedIndex++;
        }

        else if (input.triggersAction(Action.LEFT)) {

            if(motherNatureIndex > -1) stepBackwardsMotherNature();
            else currentSelectedIndex--;
        }

        updateCLI();
    }

    private void stepForwardMotherNature() {

        int compoundIslands = getScene().getIsland(11).getIndex() + 1;
        int motherCompoundIndex = getScene().getIsland(getScene().getMotherNatureIslandIndex()).getIndex();

        if (currentSelectedIndex + 1 > motherNatureIndex + motherNatureMaxSteps) currentSelectedIndex = motherNatureIndex;

        int prevSelectedIndex = currentSelectedIndex;
        do {

            currentSelectedIndex++;
            if(currentSelectedIndex > motherNatureIndex + compoundIslands) currentSelectedIndex = motherNatureIndex;
        }
        while(getScene().getIsland(modValue(currentSelectedIndex)).getIndex() == motherCompoundIndex ||
              getScene().getIsland(modValue(currentSelectedIndex)).getIndex() ==
              getScene().getIsland(modValue(prevSelectedIndex)).getIndex());
    }

    private void stepBackwardsMotherNature() {

        int motherCompoundIndex = getScene().getIsland(getScene().getMotherNatureIslandIndex()).getIndex();
        int motherCompoundSize = 0;

        int currentIndex = motherNatureIndex;
        while(getScene().getIsland(currentIndex).getIndex() == motherCompoundIndex) {

            motherCompoundSize++;
            currentIndex++;
        }

        if (currentSelectedIndex - 1 < motherNatureIndex + motherCompoundSize) {

            currentSelectedIndex = motherNatureIndex;
            for(int n = 0; n < motherNatureMaxSteps; n++) {

                stepForwardMotherNature();
                if(getScene().getIsland(modValue(currentSelectedIndex + 1)).getIndex() == motherCompoundIndex) break;
            }
        }
        else {

            int prevSelectedIndex = currentSelectedIndex;
            do { currentSelectedIndex--; }
            while(getScene().getIsland(modValue(currentSelectedIndex)).getIndex() ==
                  getScene().getIsland(modValue(prevSelectedIndex)).getIndex());
        }
    }

    private int modValue(int index) {

        int modValue = index;
        int mod = getScene().getNumberOfIslands();
        while(modValue < 0) modValue += mod;
        return modValue % mod;
    }

    private void manageCharacters(EriantysClient client) throws IOException {

        if(studentColor != null) client.sendToServer(new PerformedMoveMessage(requestMessage,
                new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) requestMessage.moveRequest).toWhere, currentSelected.getIndex(), studentColor, characterIndex)));
        else client.sendToServer(new PerformedMoveMessage(requestMessage,
                new ChooseIsland(currentSelected.getIndex(), characterIndex)));
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        currentSelected = getScene().getIsland(modValue(currentSelectedIndex));
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f - 1, currentSelected.getFrameY() - 1);
    }
}
