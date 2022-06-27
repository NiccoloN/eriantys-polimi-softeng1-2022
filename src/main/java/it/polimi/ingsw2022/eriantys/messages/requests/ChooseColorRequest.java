package it.polimi.ingsw2022.eriantys.messages.requests;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.GameScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.states.ColorSelection;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import java.util.List;

/**
 * This class represents the request of choosing a color amongst the ones available, saved in the attribute availableColors.
 * It's mainly used due to the effect of a character card.
 * The attribute fromWhere indicates where the chosen color is going to be taken from.
 * @see ColoredPawnOriginDestination
 * @author Emanuele Musto
 */
public class ChooseColorRequest extends MoveRequest {

    public final ColoredPawnOriginDestination fromWhere;
    private final List<PawnColor> availableColors;
    public final int characterCardIndex;

    public ChooseColorRequest(int characterCardIndex, List<PawnColor> availableColors, ColoredPawnOriginDestination fromWhere, String promptSentence) {

        super(promptSentence);
        this.characterCardIndex = characterCardIndex;
        this.availableColors = availableColors;
        this.fromWhere = fromWhere;
    }

    @Override
    public void manage(EriantysCLI cli, GameScene scene, MoveRequestMessage requestMessage) {

        super.manage(cli, scene, requestMessage);
        scene.setColors(availableColors);
        scene.setState(new ColorSelection(cli, scene, requestMessage, characterCardIndex));
    }
}
