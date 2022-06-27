package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.messages.moves.MoveStudent;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveStudentRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.io.IOException;
import java.security.InvalidParameterException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a game scene state in which the user is asked to select his dining room or go back
 * to the previous state by triggering a specific action
 * @author Niccolò Nicolosi
 */
public class DiningRoomSelection extends GameSceneState {

    private final GameSceneState prevState;
    private final Action goBackAction;

    private final PawnColor studentColor;

    /**
     * Constructs a dining room selection state
     * @param cli   the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public DiningRoomSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage, PawnColor studentColor, GameSceneState prevState, Action goBackAction) {

        super(cli, scene, requestMessage);
        this.studentColor = studentColor;

        if(goBackAction == Action.SELECT)
            throw new InvalidParameterException("GoBackAction must not be SELECT, " +
                                                "because it is already associated to a different behaviour in this state");
        this.prevState = prevState;
        this.goBackAction = goBackAction;
    }

    @Override
    public void enter() {

        getScene().getPlayer().setColor(GREEN);
    }

    @Override
    public void exit() {

        getScene().getPlayer().setColor(RESET);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        if(input.triggersAction(goBackAction)) {

            getScene().setState(prevState);
            return;
        }

        if(input.triggersAction(Action.SELECT)) {

            EriantysClient.getInstance().sendToServer(new PerformedMoveMessage(requestMessage,
                    new MoveStudent(ColoredPawnOriginDestination.TABLE, ((MoveStudentRequest) requestMessage.moveRequest).toWhere, -1, studentColor)));

            getScene().setState(new ViewOnly(getCli(), getScene()));
            return;
        }
    }
}
