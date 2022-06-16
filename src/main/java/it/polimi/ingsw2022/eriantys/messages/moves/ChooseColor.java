package it.polimi.ingsw2022.eriantys.messages.moves;

import it.polimi.ingsw2022.eriantys.messages.changes.CharacterCardsChange;
import it.polimi.ingsw2022.eriantys.messages.changes.SchoolChange;
import it.polimi.ingsw2022.eriantys.messages.changes.Update;
import it.polimi.ingsw2022.eriantys.messages.requests.ColoredPawnOriginDestination;
import it.polimi.ingsw2022.eriantys.server.model.Game;
import it.polimi.ingsw2022.eriantys.server.model.influence.InfluenceCalculatorNoColor;
import it.polimi.ingsw2022.eriantys.server.model.pawns.ColoredPawn;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;

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
    public boolean isValid(Game game) {

        switch(characterCardIndex) {

            case 7:
                if(fromWhere.equals(ColoredPawnOriginDestination.CHARACTER)) {
                    errorMessage = "The chosen color isn't available on the character card";
                    return game.getCharacterOfIndex(characterCardIndex).getStudentsColors().contains(chosenColor);
                }
                else if(fromWhere.equals(ColoredPawnOriginDestination.ENTRANCE)) {
                    errorMessage = "The chosen color isn't available on school entrance.";
                    return game.getPlayer(game.getCurrentPlayer().username).getSchool().getAvailableEntranceColors().contains(chosenColor);
                }
                else {
                    errorMessage = "Invalid parameters.";
                    return false;
                }

            case 10:
                if(fromWhere.equals(ColoredPawnOriginDestination.TABLE)) {
                    errorMessage = "The chosen color isn't available on the students' table";
                    return game.getPlayer(game.getCurrentPlayer().username).getSchool().countTableStudents(chosenColor) > 0;
                }
                else if(fromWhere.equals(ColoredPawnOriginDestination.ENTRANCE)) {
                    errorMessage = "The chosen color isn't available on school entrance.";
                    return game.getPlayer(game.getCurrentPlayer().username).getSchool().getAvailableEntranceColors().contains(chosenColor);
                }
                else {
                    errorMessage = "Invalid parameters.";
                    return false;
                }
            case 9:
            case 12:
                return true;
            case 11:
                if(fromWhere.equals(ColoredPawnOriginDestination.CHARACTER)) {
                    errorMessage = "The chosen color isn't available on the character card";
                    return game.getCharacterOfIndex(characterCardIndex).getStudentsColors().contains(chosenColor);
                }
                else {
                    errorMessage = "Invalid parameters.";
                    return false;
                }
            default:
                errorMessage = "The chosen character card is not available.";
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
            default: break;
        }
    }

    @Override
    public Update getUpdate(Game game) {

        Update update = new Update();

        CharacterCardsChange characterCardsChange;
        SchoolChange schoolChange;

        switch(characterCardIndex) {

            case 7:
                characterCardsChange = new CharacterCardsChange();
                schoolChange = new SchoolChange(game.getCurrentPlayer().getSchool());
                for(int i=0; i<game.getNumberOfCharacters(); i++) characterCardsChange.addCharacterCard(game.getCharacter(i));
                update.addChange(characterCardsChange);
                update.addChange(schoolChange);
                break;
            case 10:
            case 12:
            case 11:
                schoolChange = new SchoolChange(game.getCurrentPlayer().getSchool());
                update.addChange(schoolChange);
                break;
            default: break;
        }

        return update;
    }
}
