package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.ColorSelection;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;

import java.util.List;

public class MoveStudentRequest extends MoveRequest {

    private final List<PawnColor> availableColors;

    public MoveStudentRequest(List<PawnColor> availableColors) {

        super("Move a student from your school's entrance to either an island or your dining room");
        this.availableColors = availableColors;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setColors(availableColors);
        scene.setState(new ColorSelection(cli, scene, requestMessage));
    }
}
