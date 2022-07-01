package it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.gui.EriantysGUI;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.SceneController;
import it.polimi.ingsw2022.eriantys.client.view.gui.controllers.game.components.*;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
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

/**
 * This class represents the gui controller of the game scene, that contains all the components that manage various javafx nodes
 * in order to represent te current state of the game
 * @author Niccolò Nicolosi
 * @author Emanuele Musto
 */
public class GameController extends SceneController implements Initializable {
    
    private final List<Player> playersList;
    
    private final Map<String, PlayerGUIComponent> playerComponents;
    private final Map<String, SchoolGUIComponent> schoolGUIComponents;
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
    
    /**
     * Constructs the game scene controller
     * @param gui     the gui associated to this controller
     * @param players the list of players of the game
     */
    public GameController(EriantysGUI gui, List<Player> players) {
        
        super(gui);
        islandGUIComponents = new ArrayList<>(12);
        cloudGUIComponents = new ArrayList<>(2);
        schoolGUIComponents = new HashMap<>();
        playerComponents = new HashMap<>();
        
        playersList = players;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        for (int n = 0; n < 4; n++) {
            
            if (n < playersList.size())
                playerComponents.put(playersList.get(n).getUsername(), new PlayerGUIComponent((Group) players.getChildren().get(n), playersList.get(n).getUsername(), n));
            
            else players.getChildren().get(n).setVisible(false);
        }
        
        for (int n = 0; n < 4; n++) {
            
            if (n < playersList.size())
                schoolGUIComponents.put(playersList.get(n).getUsername(), new SchoolGUIComponent((Group) schools.getChildren().get(n), playersList.get(n).team, this));
            
            else schools.getChildren().get(n).setVisible(false);
        }
        
        for (int n = 0; n < 12; n++)
            islandGUIComponents.add(new IslandGUIComponent((Group) islands.getChildren().get(n), this));
        
        helpersGUIComponent = new HelpersGUIComponent(helpers, this);
        
        for (int n = 0; n < clouds.getColumnCount(); n++) {
            
            if (n < playersList.size())
                cloudGUIComponents.add(new CloudGUIComponent((Group) clouds.getChildren().get(n), n, this));
            else clouds.getChildren().get(n).setVisible(false);
        }
        
        clouds.setTranslateX((clouds.getChildren().get(0).getLayoutBounds().getWidth() + 10) / 2 * (clouds.getColumnCount() - playersList.size()));
        
        if (EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT) {
            
            characterGUIComponents = new ArrayList<>(3);
            for (int i = 0; i < 3; i++)
                characterGUIComponents.add(new CharacterGUIComponent((Group) characters.getChildren().get(i), effectsTextArea, this));
        }
        
        colorsGUIComponent = new ColorsGUIComponent(colorButtons, this);
        
        effectsTextArea.setWrapText(true);
        effectsTextArea.setPadding(new Insets(30, 0, 0, 0));
        hintsTextArea.setWrapText(true);
        hintsTextArea.setPadding(new Insets(30, 0, 0, 0));
    }
    
    /**
     * @param username the username associated to a player gui component
     * @return the player gui component associated to the given username
     */
    public PlayerGUIComponent getPlayerGUIComponent(String username) {
        
        return playerComponents.get(username);
    }
    
    /**
     * @param guiIndex the gui index (not the compound one) of an island gui component
     * @return the island gui component of the given index
     */
    public IslandGUIComponent getIslandGUIComponent(int guiIndex) {
        
        return islandGUIComponents.get(guiIndex);
    }
    
    /**
     * @param compoundIndex the compound index of an island
     * @return the list of island gui components with the given compound index
     */
    public List<IslandGUIComponent> getIslandGUIComponentsOfIndex(int compoundIndex) {
        
        return islandGUIComponents.stream().filter((x) -> x.getCompoundIslandIndex() == compoundIndex).collect(Collectors.toList());
    }
    
    public List<IslandGUIComponent> getIslandGUIComponents() {
        
        return new ArrayList<>(islandGUIComponents);
    }
    
    /**
     * @return the number of compound islands currently in game
     */
    public int getNumberOfCompoundIslands() {
        
        return islandGUIComponents.get(11).getCompoundIslandIndex() + 1;
    }
    
    public CloudGUIComponent getCloudGUIComponent(int index) {
        
        return cloudGUIComponents.get(index);
    }
    
    public SchoolGUIComponent getSchoolGUIComponentOfPlayer(String playerName) {
        
        return schoolGUIComponents.get(playerName);
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
    
    /**
     * Sets the text to visualize as hint
     * @param text the text to visualize
     */
    public void setHintsText(String text) {
        
        hintsTextArea.setText(text);
    }
    
    /**
     * Makes the gui visualize the winner in a dedicated text area
     * @param team the winner team
     */
    public void endGame(Team team) {
        
        stopListeningToInputs();
        
        winner.setWrapText(true);
        winner.setPadding(new Insets(30, 0, 0, 0));
        
        String winner2 = "";
        if (team.getSize() > 1) winner2 = team.getPlayers().get(1).getUsername();
        
        winner.setStyle("-fx-font-size: 22px");
        winner.setText("THE WINNER IS TEAM:\n" + team.getTeamName() + "\n\n" + team.getLeader().getUsername() + "\n" + winner2);
        
        winner.setVisible(true);
    }
    
    /**
     * Makes the gui stop listening to click and keyboard inputs
     */
    public void stopListeningToInputs() {
        
        for (IslandGUIComponent island : islandGUIComponents) island.stopListeningToInput();
        for (CloudGUIComponent cloud : cloudGUIComponents) cloud.stopListeningToInput();
        if (EriantysClient.getInstance().getGameSettings().gameMode == GameMode.EXPERT)
            for (CharacterGUIComponent character : characterGUIComponents) character.stopListeningToInput();
        helpersGUIComponent.stopListeningToInput();
        colorsGUIComponent.stopListeningToInput();
    }
    
    /**
     * @return a preset border glow effect applicable to javafx nodes
     */
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
