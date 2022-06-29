package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.*;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GameController extends SceneController implements Initializable {
    
    private final List<Player> playersList;
    
    private final Map<String, PlayerGUIComponent> playerComponents;
    private final Map<String, SchoolGUIComponent> dashboardComponents;
    private final List<IslandGUIComponent> islandGUIComponents;
    private final List<CloudGUIComponent> cloudGUIComponents;
    @FXML
    Group players, schools, islands;
    @FXML
    GridPane clouds, helpers, characters, colorButtons;
    @FXML
    TextArea effectsTextArea, hintsTextArea, winner;
    private List<CharacterGUIComponent> characterGUIComponents;
    private HelpersGUIComponent helpersGUIComponent;
    private ColorsGUIComponent colorsGUIComponent;
    
    public GameController(EriantysGUI gui, List<Player> players) {
        
        super(gui);
        islandGUIComponents = new ArrayList<>(12);
        cloudGUIComponents  = new ArrayList<>(2);
        dashboardComponents = new HashMap<>();
        playerComponents    = new HashMap<>();
        
        playersList = players;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        for(int n = 0; n < 4; n++) {
            
            if(n < playersList.size()) playerComponents.put(playersList.get(n).getUsername(), new PlayerGUIComponent((Group) players.getChildren().get(n), playersList.get(n).getUsername(), n));
            
            else players.getChildren().get(n).setVisible(false);
        }
        
        for(int n = 0; n < 4; n++) {
            
            if(n < playersList.size()) dashboardComponents.put(playersList.get(n).getUsername(), new SchoolGUIComponent((Group) schools.getChildren().get(n), playersList.get(n).team, this));
            
            else schools.getChildren().get(n).setVisible(false);
        }
        
        for(int n = 0; n < 12; n++)
            islandGUIComponents.add(new IslandGUIComponent((Group) islands.getChildren().get(n), this));
        
        helpersGUIComponent = new HelpersGUIComponent(helpers, this);
        
        for(int n = 0; n < clouds.getColumnCount(); n++) {
            
            if(n < playersList.size()) cloudGUIComponents.add(new CloudGUIComponent((Group) clouds.getChildren().get(n), n, this));
            else clouds.getChildren().get(n).setVisible(false);
        }
        
        clouds.setTranslateX((clouds.getChildren().get(0).getLayoutBounds().getWidth() + 10) / 2 * (clouds.getColumnCount() - playersList.size()));
        
        if(EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT) {
            
            characterGUIComponents = new ArrayList<>(3);
            for(int i = 0; i < 3; i++)
                characterGUIComponents.add(new CharacterGUIComponent((Group) characters.getChildren().get(i), effectsTextArea, this));
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
    
    public IslandGUIComponent getIslandGUIComponents(int index) {
        
        return islandGUIComponents.get(index);
    }
    
    public List<IslandGUIComponent> getIslandGUIComponentsOfIndex(int compoundIndex) {
        
        return islandGUIComponents.stream().filter((x) -> x.getCompoundIslandIndex() == compoundIndex).collect(Collectors.toList());
    }
    
    public List<IslandGUIComponent> getIslandGUIComponents() {
        
        return new ArrayList<>(islandGUIComponents);
    }
    
    public int getNumberOfCompoundIslands() {
        
        return islandGUIComponents.get(11).getCompoundIslandIndex() + 1;
    }
    
    public CloudGUIComponent getCloudGUIComponent(int index) {
        
        return cloudGUIComponents.get(index);
    }
    
    public SchoolGUIComponent getDashboardGUIComponentOfPlayer(String playerName) {
        
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
    
    public void endGame(Team team) {
        
        stopListeners();
        
        winner.setWrapText(true);
        winner.setPadding(new Insets(30, 0, 0, 0));
        
        String winner2 = "";
        if(team.getSize() > 1) winner2 = team.getPlayers().get(1).getUsername();
        
        winner.setStyle("-fx-font-size: 22px");
        winner.setText("THE WINNER IS TEAM:\n" + team.getTeamName() + "\n\n" + team.getLeader().getUsername() + "\n" + winner2);
        
        winner.setVisible(true);
    }
    
    public void stopListeners() {
        
        for(IslandGUIComponent island : islandGUIComponents) island.stopListeningToInput();
        for(CloudGUIComponent cloud : cloudGUIComponents) cloud.stopListeningToInput();
        if(EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT)
            for(CharacterGUIComponent character : characterGUIComponents) character.stopListeningToInput();
        helpersGUIComponent.stopListeningToInput();
        colorsGUIComponent.stopListeningToInput();
    }
    
    public DropShadow getBorderGlowEffect() {
        
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.LIGHTGOLDENRODYELLOW);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setWidth(80);
        borderGlow.setHeight(80);
        return borderGlow;
    }
}
