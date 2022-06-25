package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.*;
import it.polimi.ingsw2022.eriantys.messages.requests.ChooseIslandRequest;
import it.polimi.ingsw2022.eriantys.messages.requests.MoveMotherNatureRequest;
import it.polimi.ingsw2022.eriantys.messages.toClient.MoveRequestMessage;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

public class GameController extends SceneController implements Initializable {

    private final List<String> playersUsernames;
    private GUIGamePhase gamePhase;
    private PawnColor chosenColor;

    private final Map<String, PlayerGUIComponent> playerComponents;
    private final Map<String, DashboardGUIComponent> dashboardComponents;
    private final List<IslandGUIComponent> islandGUIComponents;
    private final List<CloudGUIComponent> cloudGUIComponents;
    private HelpersGUIComponent helpersGUIComponent;
    private List<CharacterGUIComponent> characterGUIComponents;

    @FXML
    Group players, schools, islands;

    @FXML
    GridPane clouds, helpers, characters, colorButtons;

    @FXML
    TextArea effectsTextArea, hintsTextArea;

    public GameController(EriantysGUI gui, List<Player> players) {

        super(gui);

        playersUsernames = new ArrayList<>(2);
        islandGUIComponents = new ArrayList<>(12);
        cloudGUIComponents = new ArrayList<>(2);
        dashboardComponents = new HashMap<>();
        playerComponents = new HashMap<>();

        for(Player player : players) playersUsernames.add(player.getUsername());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(int n = 0; n < 4; n++) {

            if(n < playersUsernames.size()) playerComponents.put(playersUsernames.get(n),
                        new PlayerGUIComponent((Group) players.getChildren().get(n), playersUsernames.get(n), n));

            else players.getChildren().get(n).setVisible(false);
        }

        for(int n = 0; n < 4; n++) {
            if(n < playersUsernames.size()) {
                dashboardComponents.put(playersUsernames.get(n), new DashboardGUIComponent((Group) schools.getChildren().get(n)));
            }
            else schools.getChildren().get(n).setVisible(false);
        }

       for(int n = 0; n < 12; n++)
           islandGUIComponents.add(new IslandGUIComponent(n, (Group) islands.getChildren().get(n)));

       helpersGUIComponent = new HelpersGUIComponent(helpers);

        cloudGUIComponents.add(new CloudGUIComponent(0, (Group) clouds.getChildren().get(0)));
        cloudGUIComponents.add(new CloudGUIComponent(1, (Group) clouds.getChildren().get(1)));

        if(EriantysClient.getInstance().getGameSettings().gameMode == Mode.EXPERT) {

            characterGUIComponents = new ArrayList<>(3);
            for(int i=0; i<3; i++)
                characterGUIComponents.add(new CharacterGUIComponent((Group) characters.getChildren().get(i), effectsTextArea));
        }

        effectsTextArea.setWrapText(true);
        effectsTextArea.setPadding(new Insets(30, 0, 0, 0));
        hintsTextArea.setWrapText(true);
        hintsTextArea.setPadding(new Insets(30, 0, 0, 0));
    }

    public void setHintsText(String text) {

        hintsTextArea.setText(text);
    }

    public PlayerGUIComponent getPlayerGUIComponent(String username) {

        return playerComponents.get(username);
    }

    public IslandGUIComponent getIslandGUIComponentOfIndex(int index) {

        return islandGUIComponents.stream().filter((x) -> x.getIslandIndex() == index).findAny().orElseThrow();
    }
    
    public HelpersGUIComponent getHelpersGUIComponent() {

        return helpersGUIComponent;
    }
        
    public CloudGUIComponent getCloudGUIComponentOfIndex(int index) {

        return cloudGUIComponents.stream().filter((x) -> x.getCloudIndex() == index).findAny().orElseThrow();
    }

    public List<CharacterGUIComponent> getCharacterGUIComponents() {

        return characterGUIComponents;
    }

    public void setCharacters(List<CharacterCard> characters) {

        for(int i=0; i<3; i++) characterGUIComponents.get(i).setCharacter(characters.get(i));
    }

    public void setChosenColor(PawnColor color) { chosenColor = color; }

    public void setGamePhase(GUIGamePhase gamePhase, MoveRequestMessage requestMessage) {

        this.gamePhase = gamePhase;

        switch(gamePhase) {

            case STALL:
                for(CharacterGUIComponent character : characterGUIComponents) character.stopListeningToInput();
                break;
            case HELPER_CARD:
                helpersGUIComponent.listenToInput(requestMessage);
                break;
            case MOVE_STUDENT_ENTRANCE:
                break;
            case MOVE_STUDENT_TABLES:
                break;
            case MOVE_STUDENT_CHARACTER:
                break;
            case CHOOSE_ISLAND:
                if(requestMessage.moveRequest instanceof MoveMotherNatureRequest)
                    for(IslandGUIComponent island : islandGUIComponents) island.listenToInput(requestMessage, chosenColor);
                if(requestMessage.moveRequest instanceof ChooseIslandRequest)
                    for(IslandGUIComponent island : islandGUIComponents)
                        island.listenToInput(requestMessage, chosenColor, ((ChooseIslandRequest) requestMessage.moveRequest).characterCardIndex);
                break;
            case CHOOSE_CLOUD:
                for(CloudGUIComponent cloud : cloudGUIComponents) cloud.listenToInput(requestMessage);
                break;
        }
    }


}
