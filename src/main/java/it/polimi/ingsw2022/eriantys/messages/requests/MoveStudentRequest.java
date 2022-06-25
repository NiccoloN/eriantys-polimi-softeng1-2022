package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.ColorSelection;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.GameController;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.util.List;

public class MoveStudentRequest extends MoveRequest {

    private final List<PawnColor> availableColors;
    public final int characterIndex;
    public final List<ColoredPawnOriginDestination> toWhere;

    public MoveStudentRequest(List<PawnColor> availableColors, List<ColoredPawnOriginDestination> toWhere) {

        super("Move a student from your school's entrance to either an island or your dining room");
        this.availableColors = availableColors;
        characterIndex = 0;
        this.toWhere = toWhere;
    }

    public MoveStudentRequest(int characterIndex, List<PawnColor> availableColors, List<ColoredPawnOriginDestination> toWhere, String promptSentence) {

        super(promptSentence);
        this.availableColors = availableColors;
        this.characterIndex = characterIndex;
        this.toWhere = toWhere;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setColors(availableColors);

        if (characterIndex < 1) scene.setState(new ColorSelection(cli, scene, requestMessage));
        else scene.setState(new ColorSelection(cli, scene, requestMessage, characterIndex));
    }

    @Override
    public void manage(GameController controller, MoveRequestMessage requestMessage) {

        super.manage(controller, requestMessage);
    }
}
