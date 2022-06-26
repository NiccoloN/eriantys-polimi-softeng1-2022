package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.*;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
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

    private final List<Player> playersList;

    private final Map<String, PlayerGUIComponent> playerComponents;
    private final Map<String, DashboardGUIComponent> dashboardComponents;
    private final List<IslandGUIComponent> islandGUIComponents;
    private final List<CloudGUIComponent> cloudGUIComponents;
    private List<CharacterGUIComponent> characterGUIComponents;
    private HelpersGUIComponent helpersGUIComponent;
    private ColorsGUIComponent colorsGUIComponent;

    @FXML
    Group players, schools, islands;

    @FXML
    GridPane clouds, helpers, characters, colorButtons;

    @FXML
    TextArea effectsTextArea, hintsTextArea;

    public GameController(EriantysGUI gui, List<Player> players) {

        super(gui);
        islandGUIComponents = new ArrayList<>(12);
        cloudGUIComponents = new ArrayList<>(2);
        dashboardComponents = new HashMap<>();
        playerComponents = new HashMap<>();

        playersList = players;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(int n = 0; n < 4; n++) {

            if(n < playersList.size()) playerComponents.put(playersList.get(n).getUsername(),
                        new PlayerGUIComponent((Group) players.getChildren().get(n), playersList.get(n).getUsername(), n));

            else players.getChildren().get(n).setVisible(false);
        }

        for(int n = 0; n < 4; n++) {

            if(n < playersList.size())
                dashboardComponents.put(
                        playersList.get(n).getUsername(),
                        new DashboardGUIComponent((Group) schools.getChildren().get(n), playersList.get(n).team, this));

            else schools.getChildren().get(n).setVisible(false);
        }

        for(int n = 0; n < 12; n++)
            islandGUIComponents.add(new IslandGUIComponent((Group) islands.getChildren().get(n), n, this));

        helpersGUIComponent = new HelpersGUIComponent(helpers);

        cloudGUIComponents.add(new CloudGUIComponent(0, (Group) clouds.getChildren().get(0)));
        cloudGUIComponents.add(new CloudGUIComponent(1, (Group) clouds.getChildren().get(1)));

        if(EriantysClient.getInstance().getGameSettings().gameMode == Mode.EXPERT) {

            characterGUIComponents = new ArrayList<>(3);
            for(int i=0; i<3; i++)
                characterGUIComponents.add(new CharacterGUIComponent((Group) characters.getChildren().get(i), effectsTextArea));
        }

        colorsGUIComponent = new ColorsGUIComponent(colorButtons, this);

        effectsTextArea.setWrapText(true);
        effectsTextArea.setPadding(new Insets(30, 0, 0, 0));
        hintsTextArea.setWrapText(true);
        hintsTextArea.setPadding(new Insets(30, 0, 0, 0));
    }

    public PlayerGUIComponent getPlayerGUIComponent(String username) {

        return playerComponents.get(username);
    }

    public IslandGUIComponent getIslandGUIComponentOfIndex(int index) {

        return islandGUIComponents.stream().filter((x) -> x.getIslandIndex() == index).findAny().orElseThrow();
    }

    public List<IslandGUIComponent> getIslandGUIComponents() {

        return new ArrayList<>(islandGUIComponents);
    }

    public CloudGUIComponent getCloudGUIComponentOfIndex(int index) {

        return cloudGUIComponents.stream().filter((x) -> x.getCloudIndex() == index).findAny().orElseThrow();
    }

    public DashboardGUIComponent getDashboardGUIComponentOfPlayer(String playerName) {
        return dashboardComponents.get(playerName);
    }

    public List<CloudGUIComponent> getCloudGUIComponents() {

        return new ArrayList<>(cloudGUIComponents);
    }

    public HelpersGUIComponent getHelpersGUIComponent() {

        return helpersGUIComponent;
    }

    public ColorsGUIComponent getColorsGUIComponent() {

        return colorsGUIComponent;
    }

    public List<CharacterGUIComponent> getCharacterGUIComponents() {

        return new ArrayList<>(characterGUIComponents);
    }

    public void setCharacters(List<CharacterCard> characters) {

        for(int i = 0; i < 3; i++) characterGUIComponents.get(i).setCharacter(characters.get(i));
    }

    public void setHintsText(String text) {

        hintsTextArea.setText(text);
    }

    public void stopListeners() {

        for(IslandGUIComponent island : islandGUIComponents) island.stopListeningToInput();
        for(CloudGUIComponent cloud : cloudGUIComponents) cloud.stopListeningToInput();
        if(EriantysClient.getInstance().getGameSettings().gameMode == Mode.EXPERT)
                for(CharacterGUIComponent character : characterGUIComponents) character.stopListeningToInput();
        helpersGUIComponent.stopListeningToInput();
        colorsGUIComponent.stopListeningToInput();
    }
}
