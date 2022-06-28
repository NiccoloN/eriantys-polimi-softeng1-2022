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
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.IOException;
import java.util.List;

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
    private int motherNatureCompoundIndex;
    private int motherNatureMaxSteps;
    private int motherNatureSteps;

    /**
     * Constructs an island selection state
     * @param cli the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public IslandSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super(cli, scene, requestMessage);
        movingStudent = false;
        studentColor = null;
        characterIndex            = 0;
        motherNatureCompoundIndex = -1;
        motherNatureMaxSteps      = -1;
        motherNatureSteps = -1;

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
        this.motherNatureCompoundIndex = scene.getCompoundIslands().stream().filter(CompoundIslandTile::hasMotherNature).findAny().orElseThrow().getIndex();
        this.motherNatureMaxSteps      = motherNatureMaxSteps;
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        if(motherNatureCompoundIndex > -1) {

            currentSelectedIndex = getScene().getMotherNatureIslandIndex();
            motherNatureSteps = 1;
            updateSelectedIndex();
        }

        updateCLI();
    }

    @Override
    public void exit() {

        super.exit();
        currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
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
                            new MoveMotherNature(currentSelected.getCompoundIndex(), ((MoveMotherNatureRequest) requestMessage.moveRequest).getMotherNatureMaxSteps())));
            else if (characterIndex > 0) manageCharacters(client);
            else client.sendToServer(new PerformedMoveMessage(requestMessage,
                        new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) requestMessage.moveRequest).toWhere ,currentSelected.getCompoundIndex(), studentColor)));

            getScene().setState(new ViewOnly(getCli(), getScene()));
            return;
        }

        if (input.triggersAction(Action.RIGHT)) {

            if(motherNatureCompoundIndex > -1) {

                motherNatureSteps++;
                updateSelectedIndex();
            }
            else currentSelectedIndex = modValue(currentSelectedIndex + 1, getScene().getNumberOfIslands());
        }

        else if (input.triggersAction(Action.LEFT)) {

            if(motherNatureCompoundIndex > -1) {

                motherNatureSteps--;
                updateSelectedIndex();
            }
            else currentSelectedIndex = modValue(currentSelectedIndex - 1, getScene().getNumberOfIslands());

        }

        updateCLI();
    }

    private void updateSelectedIndex() {

        List<CompoundIslandTile> compoundIslands = getScene().getCompoundIslands();

        if(motherNatureSteps > Math.min(motherNatureMaxSteps, compoundIslands.size() - 1)) motherNatureSteps = 1;
        else if(motherNatureSteps < 1) motherNatureSteps = Math.min(motherNatureMaxSteps, compoundIslands.size() - 1);

        int compoundIndex = modValue(motherNatureCompoundIndex + motherNatureSteps, compoundIslands.size());

        for(int n = 0; n < getScene().getNumberOfIslands(); n++) {

            if(getScene().getIsland(n).getCompoundIndex() == compoundIndex) {

                int prevCliIndex = modValue(n - 1, getScene().getNumberOfIslands());
                while(getScene().getIsland(prevCliIndex).getCompoundIndex() == compoundIndex) {

                    n = prevCliIndex;
                    prevCliIndex = modValue(n - 1, getScene().getNumberOfIslands());
                }

                currentSelectedIndex = n;
                break;
            }
        }
    }

    private int modValue(int index, int mod) {

        int modValue = index;
        while(modValue < 0) modValue += mod;
        return modValue % mod;
    }

    private void manageCharacters(EriantysClient client) throws IOException {

        if(studentColor != null) client.sendToServer(new PerformedMoveMessage(requestMessage,
                new MoveStudent(ColoredPawnOriginDestination.ISLAND, ((MoveStudentRequest) requestMessage.moveRequest).toWhere, currentSelected.getCompoundIndex(), studentColor, characterIndex)));
        else client.sendToServer(new PerformedMoveMessage(requestMessage,
                new ChooseIsland(currentSelected.getCompoundIndex(), characterIndex)));
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(IslandCLIComponent.DEFAULT_COLOR);
        currentSelected = getScene().getIsland(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f - 1, currentSelected.getFrameY() - 1);
    }
}
