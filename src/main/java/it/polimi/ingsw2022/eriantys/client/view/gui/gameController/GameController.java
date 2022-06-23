package it.polimi.ingsw2022.eriantys.client.view.gui.gameController;

import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.DashboardGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.HelpersGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.IslandGUIComponent;
import it.polimi.ingsw2022.eriantys.client.view.gui.components.PlayerGUIComponent;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

public class GameController extends SceneController implements Initializable {

    private final List<String> playersUsernames;
    private final Map<String, PlayerGUIComponent> playerComponents;
    private final Map<String, DashboardGUIComponent> dashboardComponents;
    private final List<IslandGUIComponent> islandGUIComponents;
    private HelpersGUIComponent helpersGUIComponent;

    private IslandGUIComponent islandController;
    //private HelperCardController helperCardController;
    //private CharacterCardController characterCardController;
    //private CloudController cloudController;

    @FXML
    Group players, schools, islands;

    @FXML
    GridPane clouds, characters, helpers;

    public GameController(EriantysGUI gui, Mode gameMode, List<Player> players) {

        super(gui);

        playersUsernames = new ArrayList<>(2);
        islandGUIComponents = new ArrayList<>(12);
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

        //if(gameMode == Mode.EXPERT) characterCardController = new CharacterCardController();
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
}
