package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.Action;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Input;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.ColorCLIComponent;
import it.polimi.ingsw2022.eriantys.messages.moves.Abort;
import it.polimi.ingsw2022.eriantys.messages.moves.ChooseColor;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseColorRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.messages.toServer.AbortMessage;
import it.polimi.ingsw2022.eriantys.messages.toServer.PerformedMoveMessage;
import java.io.IOException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.GREEN;
import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.RESET;

/**
 * This class represents a game scene state in which the user is asked to select a color
 * @author Niccolò Nicolosi
 */
public class ColorSelection extends GameSceneState {

    private int currentSelectedIndex;
    private ColorCLIComponent currentSelected;
    private final int characterIndex;

    /**
     * Constructs a color selection state
     * @param cli   the cli to associate to this state
     * @param scene the game scene to associate to this state
     */
    public ColorSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super(cli, scene, requestMessage);
        characterIndex = 0;
    }

    public ColorSelection(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage, int characterIndex) {

        super(cli, scene, requestMessage);
        this.characterIndex = characterIndex;
    }

    @Override
    public void enter() {

        super.enter();
        currentSelectedIndex = 0;
        for(int n = 0; n < getScene().getNumberOfHelpers(); n++) getScene().getHelper(n).setHidden(true);
        for(int n = 0; n < getScene().getNumberOfColors(); n++) getScene().getColor(n).setHidden(false);
        updateCLI();
    }

    @Override
    public void exit() {

        super.exit();
        currentSelected.setColor(RESET);
        for(int n = 0; n < getScene().getNumberOfHelpers(); n++) getScene().getHelper(n).setHidden(false);
        for(int n = 0; n < getScene().getNumberOfColors(); n++) getScene().getColor(n).setHidden(true);
    }

    @Override
    public void manageInput(Input input) throws IOException {

        EriantysClient client = EriantysClient.getInstance();

        if((characterIndex == 7 || characterIndex == 10) && input.triggersAction(Action.ESC)) {

            client.sendToServer(new PerformedMoveMessage(requestMessage, new Abort()));
            return;
        }

        if(input.triggersAction(Action.UP) && getScene().gameMode == Mode.EXPERT) {

            getScene().setState(new CharacterSelection(getCli(), getScene(), requestMessage, this, Action.DOWN));
            return;
        }

        if(input.triggersAction(Action.SELECT)){

            if(characterIndex <= 0) getScene().setState(new IslandSelection(getCli(), getScene(), requestMessage, currentSelected.pawnColor));
            else if(characterIndex == 1) getScene().setState(new IslandSelection(getCli(), getScene(), requestMessage, currentSelected.pawnColor, characterIndex));
            else {
                ChooseColorRequest colorRequest = (ChooseColorRequest) requestMessage.moveRequest;
                client.sendToServer(new PerformedMoveMessage(requestMessage, new ChooseColor
                        (currentSelected.pawnColor, characterIndex, colorRequest.fromWhere)));
            }
            return;
        }

        if (input.triggersAction(Action.RIGHT)) currentSelectedIndex++;
        else if (input.triggersAction(Action.LEFT)) currentSelectedIndex--;

        if (currentSelectedIndex < 0) currentSelectedIndex = getScene().getNumberOfColors() - 1;
        else if (currentSelectedIndex > getScene().getNumberOfColors() - 1) currentSelectedIndex = 0;

        updateCLI();
    }

    private void updateCLI() {

        if (currentSelected != null) currentSelected.setColor(RESET);
        currentSelected = getScene().getColor(currentSelectedIndex);
        currentSelected.setColor(GREEN);
        prompt.setPosition(currentSelected.getFrameX() + currentSelected.getWidth() / 2f, currentSelected.getFrameY() - 2);
    }
}
