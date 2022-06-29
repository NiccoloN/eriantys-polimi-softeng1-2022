package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene;

import it.polimi.ingsw2022.eriantys.client.view.cli.AsciiArts;
import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.*;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states.Start;
import it.polimi.ingsw2022.eriantys.server.controller.GameMode;

import java.util.Arrays;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

/**
 * This class represents a menu scene
 * @author Niccolò Nicolosi
 */
public class MenuScene extends CLIScene {
    
    private final BasicCLIComponent title;
    private final BasicCLIComponent skyBackground;
    private final AnimatedCLIComponent[] decorativeClouds;
    private final BlinkingCLIComponent startPrompt;
    private final CLIComponent changeServerPrompt;
    private final CLIComponent enterUsernamePrompt;
    private final CLIComponent enterServerIpPrompt;
    private final TextAreaCLIComponent textArea;
    private final CLIComponent panel;
    private final CLIComponent newGameOption;
    private final CLIComponent loadGameOption;
    private final CLIComponent selectNumberOfPlayersPrompt;
    private final CLIComponent[] playerNumbers;
    private final CLIComponent selectGameModePrompt;
    private final CLIComponent[] gameModes;
    private CLIComponent prompt;
    private CLIComponent connectingLabel;
    private CLIComponent chosenGameMode;
    private CLIComponent[] playerUsernames;
    
    /**
     * Constructs a menu scene
     * @param cli    the cli to associate to this scene
     * @param width  the width of this scene
     * @param height the height of this scene
     */
    public MenuScene(EriantysCLI cli, int width, int height) {
        
        super(cli, width, height);
        
        //build title component
        title = new BasicCLIComponent(73, AsciiArts.getTitleString().split("\n"));
        title.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);
        
        //build sky background component
        String[] backgroundRows = new String[height];
        Arrays.fill(backgroundRows, " ".repeat(width));
        skyBackground = new BasicCLIComponent(width, backgroundRows);
        skyBackground.setColor(BLUE_BACKGROUND_BRIGHT);
        
        //build decorative cloud components
        decorativeClouds = new AnimatedCLIComponent[60];
        int maxSpeed = 2, minSpeed = 1;
        for(int n = 0; n < decorativeClouds.length; n++) {
            
            String[] rows = null;
            switch(n % 4) {
                
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
        
        startPrompt = new BlinkingCLIComponent(29, new String[] {"Press Enter to start the game"});
        startPrompt.setFirstColor(BLACK + BLUE_BACKGROUND_BRIGHT);
        startPrompt.setFirstColor(WHITE_BRIGHT + BLUE_BACKGROUND_BRIGHT);
        
        changeServerPrompt = new BasicCLIComponent(29, new String[] {"Press ESC to change server IP"});
        changeServerPrompt.setColor(BLACK + BLUE_BACKGROUND_BRIGHT);
        
        enterUsernamePrompt = new BasicCLIComponent(16, new String[] {"Enter a username"});
        enterUsernamePrompt.setColor(RESET);
        enterUsernamePrompt.setHidden(true);
        
        enterServerIpPrompt = new BasicCLIComponent(23, new String[] {"Enter server IP address"});
        enterServerIpPrompt.setColor(RESET);
        enterServerIpPrompt.setHidden(true);
        
        textArea = new TextAreaCLIComponent(22, 4);
        textArea.setHidden(true);
        textArea.setAnimated(false);
        
        int panelWidth = 35, panelHeight = 14;
        String[] panelRows = new String[panelHeight];
        panelRows[0] = " " + "_".repeat(panelWidth - 2) + " ";
        for(int n = 1; n < panelHeight - 2; n++) panelRows[n] = "|" + " ".repeat(panelWidth - 2) + "|";
        panelRows[panelHeight - 2] = "|" + "_".repeat(panelWidth - 2) + "|";
        panelRows[panelHeight - 1] = " ".repeat(panelWidth);
        panel                      = new BasicCLIComponent(panelWidth, panelRows);
        panel.setHidden(true);
        
        loadGameOption = new BasicCLIComponent(9, new String[] {"Load game"});
        loadGameOption.setColor(RESET);
        loadGameOption.setHidden(true);
        
        newGameOption = new BasicCLIComponent(8, new String[] {"New game"});
        newGameOption.setColor(RESET);
        newGameOption.setHidden(true);
        
        selectNumberOfPlayersPrompt = new BasicCLIComponent(28, new String[] {"Select the number of players"});
        selectNumberOfPlayersPrompt.setColor(RESET);
        selectNumberOfPlayersPrompt.setHidden(true);
        
        playerNumbers = new BasicCLIComponent[3];
        for(int n = 0; n < playerNumbers.length; n++) {
            
            playerNumbers[n] = new BasicCLIComponent(3, new String[] {"|" + (n + 2) + "|"});
            playerNumbers[n].setHidden(true);
        }
        
        selectGameModePrompt = new BasicCLIComponent(20, new String[] {"Select the game mode"});
        selectGameModePrompt.setColor(RESET);
        selectGameModePrompt.setHidden(true);
        
        GameMode[] gameModes = GameMode.values();
        this.gameModes = new BasicCLIComponent[gameModes.length];
        for(int n = 0; n < this.gameModes.length; n++) {
            
            this.gameModes[n] = new BasicCLIComponent(gameModes[n].toString().length(), new String[] {gameModes[n].toString()});
            this.gameModes[n].setHidden(true);
        }
        
        setComponentsPositions();
        
        setState(new Start(cli, this));
    }
    
    /**
     * Sets every component to its starting position
     */
    private void setComponentsPositions() {
        
        title.setPosition(getWidth() / 2f - title.getWidth() / 2f, getHeight() / 3f);
        
        for(AnimatedCLIComponent decorativeCloud : decorativeClouds)
            decorativeCloud.setPosition((int) (Math.random() * getWidth() - decorativeCloud.getWidth()), (int) (Math.random() * skyBackground.getHeight() - decorativeCloud.getHeight()));
        
        startPrompt.setPosition(getWidth() / 2f - startPrompt.getWidth() / 2f, title.getFrameY() + title.getHeight() + 2);
        changeServerPrompt.setPosition(getWidth() / 2f - changeServerPrompt.getWidth() / 2f, startPrompt.getY() + 3);
        enterUsernamePrompt.setPosition(getWidth() / 2f - enterUsernamePrompt.getWidth() / 2f, startPrompt.getY());
        enterServerIpPrompt.setPosition(getWidth() / 2f - enterServerIpPrompt.getWidth() / 2f, startPrompt.getY());
        
        textArea.setPosition(getWidth() / 2f - textArea.getWidth() / 2f, enterUsernamePrompt.getFrameY() + 2);
        
        panel.setPosition(getWidth() / 2f - panel.getWidth() / 2f, startPrompt.getY());
        
        loadGameOption.setPosition(getWidth() / 2f - loadGameOption.getWidth() / 2f, panel.getY() + 4);
        newGameOption.setPosition(getWidth() / 2f - newGameOption.getWidth() / 2f, panel.getY() + 6);
        
        selectNumberOfPlayersPrompt.setPosition(getWidth() / 2f - selectNumberOfPlayersPrompt.getWidth() / 2f, panel.getY() + 2);
        selectGameModePrompt.setPosition(getWidth() / 2f - selectGameModePrompt.getWidth() / 2f, panel.getY() + 2);
        
        for(int n = 0; n < playerNumbers.length; n++)
            playerNumbers[n].setPosition(getWidth() / 2f - (playerNumbers[0].getWidth() * playerNumbers.length + playerNumbers.length - 2) / 2f + (playerNumbers[0].getWidth() + 1) * n, selectNumberOfPlayersPrompt.getFrameY() + 4);
        
        for(int n = 0; n < gameModes.length; n++)
            gameModes[n].setPosition(getWidth() / 2f - 3, selectGameModePrompt.getFrameY() + 2 + n * 2);
    }
    
    @Override
    public void printToFrame(Frame frame) {
        
        updateDecorativeCloudsPositions();
        
        skyBackground.printToFrame(frame);
        for(AnimatedCLIComponent decorativeCloud : decorativeClouds) decorativeCloud.printToFrame(frame);
        title.printToFrame(frame);
        panel.printToFrame(frame);
        startPrompt.printToFrame(frame);
        changeServerPrompt.printToFrame(frame);
        if(connectingLabel != null) connectingLabel.printToFrame(frame);
        enterUsernamePrompt.printToFrame(frame);
        enterServerIpPrompt.printToFrame(frame);
        loadGameOption.printToFrame(frame);
        newGameOption.printToFrame(frame);
        selectNumberOfPlayersPrompt.printToFrame(frame);
        selectGameModePrompt.printToFrame(frame);
        textArea.printToFrame(frame);
        for(CLIComponent playerNumber : playerNumbers) playerNumber.printToFrame(frame);
        for(CLIComponent gameMode : gameModes) gameMode.printToFrame(frame);
        if(chosenGameMode != null) chosenGameMode.printToFrame(frame);
        if(playerUsernames != null) for(CLIComponent playerUsername : playerUsernames) playerUsername.printToFrame(frame);
        if(prompt != null) prompt.printToFrame(frame);
    }
    
    /**
     * Updates the position of decorative clouds in order to make them appear on the other side of the terminal window when they are out of view
     */
    private void updateDecorativeCloudsPositions() {
        
        for(AnimatedCLIComponent decorativeCloud : decorativeClouds) {
            
            if(decorativeCloud.getX() < -decorativeCloud.getWidth()) decorativeCloud.setX(getWidth());
            else if(decorativeCloud.getX() > getWidth()) decorativeCloud.setX(-decorativeCloud.getWidth());
        }
    }
    
    public CLIComponent getPrompt() {
        
        return prompt;
    }
    
    public void setPrompt(CLIComponent prompt) {
        
        this.prompt = prompt;
    }
    
    public BlinkingCLIComponent getStartPrompt() {
        
        return startPrompt;
    }
    
    public CLIComponent getChangeServerPrompt() {
        
        return changeServerPrompt;
    }
    
    public CLIComponent getConnectingLabel() {
        
        return connectingLabel;
    }
    
    public void setConnectingLabel(CLIComponent connectingLabel) {
        
        this.connectingLabel = connectingLabel;
        connectingLabel.setPosition(getWidth() / 2f - connectingLabel.getWidth() / 2f, startPrompt.getY());
    }
    
    public CLIComponent getPanel() {
        
        return panel;
    }
    
    public CLIComponent getEnterUsernamePrompt() {
        
        return enterUsernamePrompt;
    }
    
    public CLIComponent getEnterServerIpPrompt() {
        
        return enterServerIpPrompt;
    }
    
    public CLIComponent getNewGameOption() {
        
        return newGameOption;
    }
    
    public CLIComponent getLoadGameOption() {
        
        return loadGameOption;
    }
    
    public CLIComponent getSelectNumberOfPlayersPrompt() {
        
        return selectNumberOfPlayersPrompt;
    }
    
    public CLIComponent getSelectGameModePrompt() {
        
        return selectGameModePrompt;
    }
    
    public TextAreaCLIComponent getTextArea() {
        
        return textArea;
    }
    
    public CLIComponent getPlayerNumber(int index) {
        
        return playerNumbers[index];
    }
    
    public int getNumberOfPlayerNumbers() {
        
        return playerNumbers.length;
    }
    
    public CLIComponent getGameMode(int index) {
        
        return gameModes[index];
    }
    
    public int getNumberOfGameModes() {
        
        return gameModes.length;
    }
    
    public void setChosenGameMode(BasicCLIComponent chosenGameMode) {
        
        this.chosenGameMode = chosenGameMode;
        if(chosenGameMode != null) chosenGameMode.setPosition(getWidth() / 2f - chosenGameMode.getWidth() / 2f, panel.getY() + 2);
    }
    
    public void setPlayerUsernames(BasicCLIComponent[] playerUsernames) {
        
        this.playerUsernames = playerUsernames;
        if(chosenGameMode != null) {
            
            playerUsernames[0].setPosition(getWidth() / 2f - playerUsernames[0].getWidth() / 2f, panel.getY() + 4);
            for(int n = 1; n < playerUsernames.length - 1; n++) playerUsernames[n].setPosition(playerUsernames[0].getX(), panel.getY() + 5 + n);
            playerUsernames[playerUsernames.length - 1].setPosition(getWidth() / 2f - playerUsernames[playerUsernames.length - 1].getWidth() / 2f, panel.getY() + panel.getHeight() - 3);
        }
    }
}
