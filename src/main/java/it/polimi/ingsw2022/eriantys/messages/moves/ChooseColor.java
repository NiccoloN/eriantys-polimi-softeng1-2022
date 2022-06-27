package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseColorRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveRequest;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoColor;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

/**
 * This class represents the player's choice of a color. It's mainly used for the character cards effects.
 * @author Emanuele Musto
 */
public class ChooseColor extends Move{

    private final PawnColor chosenColor;
    private final int characterCardIndex;
    private final ColoredPawnOriginDestination fromWhere;

    public ChooseColor(PawnColor chosenColor, int characterCardIndex, ColoredPawnOriginDestination fromWhere) {

        this.chosenColor = chosenColor;
        this.characterCardIndex = characterCardIndex;
        this.fromWhere = fromWhere;
    }

    @Override
    public boolean isValid(Game game, MoveRequest request) {

        if(!(request instanceof ChooseColorRequest)) {

            errorMessage = "Move not requested";
            return false;
        }

        switch(characterCardIndex) {

            case 7:
                if(fromWhere.equals(ColoredPawnOriginDestination.CHARACTER)) {

                    if(!game.getCharacterOfIndex(characterCardIndex).getStudentsColors().contains(chosenColor)) {

                        errorMessage = "The chosen color isn't available on the character card";
                        return false;
                    }

                    return true;
                }
                else if(fromWhere.equals(ColoredPawnOriginDestination.ENTRANCE)) {

                    if(!game.getPlayer(game.getCurrentPlayer().getUsername()).getSchool().getAvailableEntranceColors().contains(chosenColor)) {

                        errorMessage = "The chosen color isn't available on school entrance";
                        return false;
                    }

                    return true;
                }
                else {

                    errorMessage = "Invalid parameters";
                    return false;
                }

            case 10:
                if(fromWhere.equals(ColoredPawnOriginDestination.TABLE)) {

                    if(game.getCurrentPlayer().getSchool().countTableStudents(chosenColor) <= 0) {

                        errorMessage = "The chosen color isn't available on the students' table";
                        return false;
                    }

                    return true;
                }
                else if(fromWhere.equals(ColoredPawnOriginDestination.ENTRANCE)) {

                    if(!game.getPlayer(game.getCurrentPlayer().getUsername()).getSchool().getAvailableEntranceColors().contains(chosenColor)) {

                        errorMessage = "The chosen color isn't available on school entrance";
                        return false;
                    }

                    return true;
                }
                else {

                    errorMessage = "Invalid parameters";
                    return false;
                }
            case 9:
            case 12:
                return true;
            case 11:
                if(fromWhere.equals(ColoredPawnOriginDestination.CHARACTER)) {

                    if(!game.getCharacterOfIndex(characterCardIndex).getStudentsColors().contains(chosenColor)) {

                        errorMessage = "The chosen color isn't available on the character card";
                        return false;
                    }
                    return true;
                }
                else {

                    errorMessage = "Invalid parameters";
                    return false;
                }
            default:
                errorMessage = "The chosen character card is not available";
                return characterCardIndex >= 1 && characterCardIndex <= 12;
        }
    }

    @Override
    public void apply(Game game) {

        switch(characterCardIndex) {

            case 7:
                if(fromWhere.equals(ColoredPawnOriginDestination.CHARACTER))
                    game.setExchanges(ColoredPawnOriginDestination.CHARACTER, chosenColor);
                else if(fromWhere.equals(ColoredPawnOriginDestination.ENTRANCE))
                    game.setExchanges(ColoredPawnOriginDestination.ENTRANCE, chosenColor);
                break;
            case 9:
                game.setInfluenceCalculator(new InfluenceCalculatorNoColor(chosenColor));
                break;
            case 10:
                if(fromWhere.equals(ColoredPawnOriginDestination.TABLE))
                    game.setExchanges(ColoredPawnOriginDestination.TABLE, chosenColor);
                else if(fromWhere.equals(ColoredPawnOriginDestination.ENTRANCE))
                    game.setExchanges(ColoredPawnOriginDestination.ENTRANCE, chosenColor);
                break;
            case 11:
                game.setExchanges(ColoredPawnOriginDestination.CHARACTER, chosenColor);
                break;
            case 12:
                for(Player player : game.getPlayers()) {

                    for(int i = 0; i<3; i++) {

                        if(player.getSchool().countTableStudents(chosenColor) > 0) {
                            ColoredPawn student = player.getSchool().removeFromTable(chosenColor);
                            game.getStudentsBag().addStudent(student);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        CharacterCardsChange characterCardsChange;

        switch(characterCardIndex) {

            case 7:
                characterCardsChange = new CharacterCardsChange();
                for(int i=0; i<game.getNumberOfCharacters(); i++) characterCardsChange.addCharacterCard(game.getCharacter(i));
                update.addChange(characterCardsChange);
                update.addChange(new SchoolChange(game.getCurrentPlayer().getSchool()));
                break;
            case 10:
            case 11:
            case 12:
                for(Player player : game.getPlayers()) update.addChange(new SchoolChange(game.getPlayer(player.getUsername()).getSchool()));
                break;
        }

        return update;
    }
}
