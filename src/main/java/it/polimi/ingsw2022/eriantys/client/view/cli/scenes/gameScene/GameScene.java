package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene;

import it.polimi.ingsw2022.eriantys.client.EriantysClient;
import it.polimi.ingsw2022.eriantys.client.view.cli.AsciiArts;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.AnimatedCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.BasicCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.TextAreaCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.*;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.gameScene.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.states.ViewOnly;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;
import it.polimi.ingsw2022.eriantys.server.model.board.CompoundIslandTile;
import it.polimi.ingsw2022.eriantys.server.model.cards.CharacterCard;
import it.polimi.ingsw2022.eriantys.server.model.cards.HelperCard;
import it.polimi.ingsw2022.eriantys.server.model.pawns.PawnColor;
import it.polimi.ingsw2022.eriantys.server.model.players.Player;
import it.polimi.ingsw2022.eriantys.server.model.players.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents the game scene
 * @author Niccolò Nicolosi
 */
public class GameScene extends CLIScene {
    
    public final GameMode gameMode;
    private final BasicCLIComponent title;
    private final BasicCLIComponent line;
    private final BasicCLIComponent skyBackground;
    private final AnimatedCLIComponent[] decorativeClouds;
    private final TextAreaCLIComponent hintTextArea;
    private final TextAreaCLIComponent effectTextArea;
    private final WinnerMessageCLIComponent winnerMessage;
    private final IslandCLIComponent[] islands;
    private final List<CloudCLIComponent> clouds;
    private final List<PlayerStatusCLIComponent> players;
    private List<CompoundIslandTile> compoundIslands;
    private CLIComponent prompt;
    private List<HelperCardCLIComponent> helpers;
    private List<CharacterCardCLIComponent> characters;
    private List<ColorCLIComponent> colors;
    
    /**
     * Constructs a game scene
     * @param cli      the cli to associate to this scene
     * @param width    the width of this scene
     * @param height   the height of this scene
     * @param players  the players of the game
     * @param gameMode the mode of the game
     */
    public GameScene(EriantysCLI cli, int width, int height, List<Player> players, GameMode gameMode) {
        
        super(cli, width, height);
        
        this.gameMode = gameMode;
        
        //build title component
        title = new BasicCLIComponent(73, AsciiArts.getTitleString().split("\n"));
        title.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);
        
        //build sky background component
        String[] backgroundRows = new String[title.getHeight() + 1];
        Arrays.fill(backgroundRows, " ".repeat(width));
        skyBackground = new BasicCLIComponent(width, backgroundRows);
        skyBackground.setColor(BLUE_BACKGROUND_BRIGHT);
        
        //build sky separator line
        line = new BasicCLIComponent(width, new String[]{"_".repeat(width)});
        line.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);
        
        //build decorative cloud components
        decorativeClouds = new AnimatedCLIComponent[20];
        int maxSpeed = 2, minSpeed = 1;
        for (int n = 0; n < decorativeClouds.length; n++) {
            
            String[] rows = null;
            switch (n % 4) {
                
                case 0:
                    rows = AsciiArts.getCloud1String().split("\n");
                    break;
                case 1:
                    rows = AsciiArts.getCloud2String().split("\n");
                    break;
                case 2:
                    rows = AsciiArts.getCloud3String().split("\n");
                    break;
                case 3:
                    rows = AsciiArts.getCloud4String().split("\n");
                    break;
            }
            
            AnimatedCLIComponent decorativeCloud = new AnimatedCLIComponent(rows[0].length(), rows);
            decorativeCloud.setColor(WHITE + BLUE_BACKGROUND_BRIGHT);
            decorativeCloud.setSpeedX((float) ((Math.random() * (maxSpeed - minSpeed) + minSpeed) * (Math.random() >= 0.5 ? 1 : -1)));
            decorativeClouds[n] = decorativeCloud;
        }
        
        winnerMessage = new WinnerMessageCLIComponent();
        winnerMessage.setHidden(true);
        
        //build island components
        islands = new IslandCLIComponent[12];
        for (int n = 0; n < islands.length; n++) islands[n] = new IslandCLIComponent(n);
        
        //build cloud components
        clouds = new ArrayList<>(players.size());
        for (int n = 0; n < players.size(); n++) clouds.add(new CloudCLIComponent(n));
        
        //build player dashboard components
        this.players = new ArrayList<>(players.size());
        for (int n = 0; n < players.size(); n++)
            this.players.add(new PlayerStatusCLIComponent(players.get(n).getUsername(), players.get(n).team.ansiColor, n % 2 != 0, gameMode));
        
        //build text area components
        hintTextArea = new TextAreaCLIComponent(this.players.get(0).getWidth(), 15, "Hints");
        effectTextArea = new TextAreaCLIComponent(hintTextArea.getWidth(), hintTextArea.getHeight(), "Effect");
        
        setComponentsPositions();
        
        //set first state
        setState(new ViewOnly(cli, this));
    }
    
    @Override
    public void printToFrame(Frame frame) {
        
        updateDecorativeCloudsPositions();
        
        skyBackground.printToFrame(frame);
        for (AnimatedCLIComponent decorativeCloud : decorativeClouds) decorativeCloud.printToFrame(frame);
        title.printToFrame(frame);
        line.printToFrame(frame);
        
        for (IslandCLIComponent island : islands) island.printToFrame(frame);
        for (CloudCLIComponent cloud : clouds) cloud.printToFrame(frame);
        for (PlayerStatusCLIComponent player : players) player.printToFrame(frame);
        
        synchronized (this) {
            
            if (helpers != null) for (HelperCardCLIComponent helper : helpers) helper.printToFrame(frame);
            if (characters != null)
                for (CharacterCardCLIComponent character : characters) character.printToFrame(frame);
            if (colors != null) for (BasicCLIComponent color : colors) color.printToFrame(frame);
        }
        
        hintTextArea.printToFrame(frame);
        effectTextArea.printToFrame(frame);
        if (prompt != null) prompt.printToFrame(frame);
        winnerMessage.printToFrame(frame);
    }
    
    /**
     * @return the list of compound islands in game at the moment of the last update received
     */
    public List<CompoundIslandTile> getCompoundIslands() {
        
        return compoundIslands;
    }
    
    /**
     * Sets the list of compound islands in game
     * @param compoundIslands the updated list of compound islands
     */
    public void setCompoundIslands(List<CompoundIslandTile> compoundIslands) {
        
        this.compoundIslands = compoundIslands;
    }
    
    /**
     * Sets the list of helpers in game (only the helpers in the hand of the player represented by this client)
     * @param helperCards the updated list of helpers
     */
    public synchronized void setHelpers(List<HelperCard> helperCards) {
        
        if (helpers == null) helpers = new ArrayList<>(helperCards.size());
        else helpers.clear();
        
        for (HelperCard card : helperCards)
            helpers.add(new HelperCardCLIComponent(card.index, card.priority, card.movement));
        
        //arrange helper cards in a row at the bottom of the screen
        for (int n = 0; n < helpers.size(); n++)
            helpers.get(n).setPosition(getWidth() / 2f - (helpers.get(0).getWidth() * helpers.size() + helpers.size() - 2) / 2f + (helpers.get(0).getWidth() + 1) * n, getHeight() - helpers.get(0).getHeight() - 1);
    }
    
    /**
     * Sets the list of characters in game
     * @param characterCards the updated list of characters
     */
    public synchronized void setCharacters(List<CharacterCard> characterCards) {
        
        if (characters == null) characters = new ArrayList<>(characterCards.size());
        else characters.clear();
        
        for (CharacterCard card : characterCards) characters.add(new CharacterCardCLIComponent(card));
        
        //arrange character cards in a row below the islands
        int charactersY = islands[5].getFrameY() + islands[5].getHeight() + 2;
        for (int n = 0; n < characters.size(); n++)
            characters.get(n).setPosition(getWidth() / 2f - (characters.get(0).getWidth() * characters.size() + characters.size() - 2) / 2f + (characters.get(0).getWidth() + 1) * n, charactersY);
    }
    
    /**
     * Sets the list of currently selectable colors
     * @param colors the list of selectable colors
     */
    public synchronized void setColors(List<PawnColor> colors) {
        
        if (this.colors == null) this.colors = new ArrayList<>(colors.size());
        else this.colors.clear();
        
        for (PawnColor color : colors) this.colors.add(new ColorCLIComponent(color));
        
        //arrange colors in a row at the bottom of the screen
        for (int n = 0; n < this.colors.size(); n++)
            this.colors.get(n).setPosition(getWidth() / 2f - (this.colors.get(0).getWidth() * this.colors.size() + this.colors.size() - 2) / 2f + (this.colors.get(0).getWidth() + 1) * n, getHeight() - this.colors.get(0).getHeight() - 3);
    }
    
    /**
     * Sets the prompt cli component to visualize
     * @param prompt the new prompt component
     */
    public void setPrompt(CLIComponent prompt) {
        
        this.prompt = prompt;
    }
    
    public IslandCLIComponent getIsland(int index) {
        
        return islands[index];
    }
    
    public int getNumberOfIslands() {
        
        return islands.length;
    }
    
    public CloudCLIComponent getCloud(int index) {
        
        return clouds.get(index);
    }
    
    public int getNumberOfClouds() {
        
        return clouds.size();
    }
    
    public HelperCardCLIComponent getHelper(int index) {
        
        return helpers.get(index);
    }
    
    public int getNumberOfHelpers() {
        
        return helpers != null ? helpers.size() : 0;
    }
    
    public CharacterCardCLIComponent getCharacter(int index) {
        
        return characters.get(index);
    }
    
    public int getNumberOfCharacters() {
        
        return characters != null ? characters.size() : 0;
    }
    
    public ColorCLIComponent getColor(int index) {
        
        return colors.get(index);
    }
    
    public int getNumberOfColors() {
        
        return colors != null ? colors.size() : 0;
    }
    
    public TextAreaCLIComponent getHintTextArea() {
        
        return hintTextArea;
    }
    
    public TextAreaCLIComponent getEffectTextArea() {
        
        return effectTextArea;
    }
    
    public PlayerStatusCLIComponent getPlayer() {
        
        return getPlayer(EriantysClient.getInstance().getUsername());
    }
    
    public PlayerStatusCLIComponent getPlayer(String username) {
        
        return players.stream().filter((x) -> x.getNickname().equals(username)).findAny().orElseThrow();
    }
    
    /**
     * @return the cli side index (not the compound index) of the island that currently contains mother nature
     * @throws NoSuchElementException if no island containing mother nature is found in this scene
     */
    public int getMotherNatureIslandIndex() {
        
        for (int n = 0; n < islands.length; n++) if (islands[n].hasMother()) return n;
        throw new NoSuchElementException();
    }
    
    /**
     * Sets the winner of the game, visualizing the winner cli component
     * @param winnerTeam the winner team
     */
    public void setWinner(Team winnerTeam) {
        
        winnerMessage.setWinnerTeam(winnerTeam);
        winnerMessage.setHidden(false);
    }
    
    /**
     * Sets every component to its starting position
     */
    private void setComponentsPositions() {
        
        title.setPosition(getWidth() / 2f - title.getWidth() / 2f, 0);
        line.setPosition(0, title.getHeight());
        
        for (AnimatedCLIComponent decorativeCloud : decorativeClouds)
            decorativeCloud.setPosition((int) (Math.random() * getWidth() - decorativeCloud.getWidth()), (int) (Math.random() * skyBackground.getHeight() - decorativeCloud.getHeight() + 1));
        
        int offsetY = title.getHeight() + 2;
        
        players.get(0).setPosition(0, offsetY);
        players.get(1).setPosition(getWidth() - players.get(0).getWidth(), offsetY);
        if (players.size() >= 3) players.get(2).setPosition(0, offsetY + players.get(0).getHeight() + 1);
        if (players.size() >= 4)
            players.get(3).setPosition(getWidth() - players.get(0).getWidth(), offsetY + players.get(0).getHeight() + 1);
        
        int textAreasY = players.get(players.size() - 1).getFrameY() + players.get(0).getHeight() + 1;
        hintTextArea.setPosition(getWidth() - hintTextArea.getWidth(), textAreasY);
        effectTextArea.setPosition(0, textAreasY);
        
        winnerMessage.setPosition(getWidth() / 2f - winnerMessage.getWidth() / 2f, getHeight() / 2f - winnerMessage.getHeight() / 2f - 1);
        
        islands[0].setPosition(2 + getWidth() / 2f, offsetY);
        islands[1].setPosition(2 + getWidth() / 2f + islands[0].getWidth(), offsetY + islands[0].getHeight() / 2f);
        islands[2].setPosition(2 + getWidth() / 2f + islands[0].getWidth() * 2, offsetY + islands[0].getHeight());
        islands[3].setPosition(2 + getWidth() / 2f + islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + 1);
        islands[4].setPosition(2 + getWidth() / 2f + islands[0].getWidth(), offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2f + 1);
        islands[5].setPosition(2 + getWidth() / 2f, offsetY + islands[0].getHeight() * 3 + 1);
        islands[6].setPosition(-1 + getWidth() / 2f - islands[0].getWidth(), offsetY + islands[0].getHeight() * 3 + 1);
        islands[7].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2f + 1);
        islands[8].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 3, offsetY + islands[0].getHeight() * 2 + 1);
        islands[9].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 3, offsetY + islands[0].getHeight());
        islands[10].setPosition(-1 + getWidth() / 2f - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() / 2f);
        islands[11].setPosition(-1 + getWidth() / 2f - islands[0].getWidth(), offsetY);
        
        //arrange clouds in a row in the middle of the islands
        int cloudsY = islands[2].getFrameY() + islands[2].getHeight() - clouds.get(0).getHeight() / 2;
        for (int n = 0; n < clouds.size(); n++)
            clouds.get(n).setPosition(getWidth() / 2f - (clouds.get(0).getWidth() * clouds.size() + clouds.size() - 2) / 2f + (clouds.get(0).getWidth() + 1) * n, cloudsY);
    }
    
    /**
     * Updates the position of decorative clouds in order to make them appear on the other side of the terminal window when they are out of view
     */
    private void updateDecorativeCloudsPositions() {
        
        for (AnimatedCLIComponent decorativeCloud : decorativeClouds) {
            
            if (decorativeCloud.getX() < -decorativeCloud.getWidth()) decorativeCloud.setX(getWidth());
            else if (decorativeCloud.getX() > getWidth()) decorativeCloud.setX(-decorativeCloud.getWidth());
        }
    }
}
