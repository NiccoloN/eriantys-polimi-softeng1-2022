package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.gameController.components.*;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

public class GameController extends SceneController implements Initializable {

    private final List<String> playersUsernames;
    
    private final Map<String, PlayerGUIComponent> playerComponents;
    private final Map<String, DashboardGUIComponent> dashboardComponents;
    private final List<IslandGUIComponent> islandGUIComponents;
    private final List<CloudGUIComponent> cloudGUIComponents;
    private HelpersGUIComponent helpersGUIComponent;
    private List<CharacterGUIComponent> characterGUIComponents;

    @FXML
    Group players, schools, islands;

    @FXML
    GridPane clouds, helpers, characters;

    @FXML
    TextArea effectsTextArea, hintsTextArea;

    public GameController(EriantysGUI gui, Mode gameMode, List<Player> players) {

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
                        new PlayerGUIComponent((Group) players.getChildren().get(n), playersUsernames.get(n)));

            else players.getChildren().get(n).setVisible(false);
        }

        for(int n = 0; n < 4; n++) {

            /*if(n < dashboardComponents.size()) /*TODO;
            else schools.getChildren().get(n).setVisible(false);*/
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

    public void setCharacters(List<CharacterCard> characters) {

        for(int i=0; i<3; i++) characterGUIComponents.get(i).setCharacter(characters.get(i));
    }
}
