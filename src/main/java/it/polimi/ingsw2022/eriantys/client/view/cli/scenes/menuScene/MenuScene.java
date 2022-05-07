package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene;

import it.polimi.ingsw2022.eriantys.client.view.cli.EriantysCLI;
import it.polimi.ingsw2022.eriantys.client.view.cli.Frame;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.CLIScene;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components.*;
import it.polimi.ingsw2022.eriantys.client.view.cli.scenes.menuScene.states.Start;
import it.polimi.ingsw2022.eriantys.server.controller.Mode;

import java.util.Arrays;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

public class MenuScene extends CLIScene {

    private final BasicCLIComponent title;
    private final BasicCLIComponent skyBackground;
    private final AnimatedCLIComponent[] decorativeClouds;

    private CLIComponent prompt;
    private final BlinkingCLIComponent startPrompt;
    private final BasicCLIComponent connectingLabel;
    private final BasicCLIComponent enterUsernamePrompt;
    private final TextAreaCLIComponent usernameTextArea;
    private final BasicCLIComponent selectNumberOfPlayersPrompt;
    private final BasicCLIComponent[] playerNumbers;
    private final BasicCLIComponent selectGameModePrompt;
    private final BasicCLIComponent[] gameModes;

    public MenuScene(EriantysCLI cli, int width, int height) {

        super(cli, width, height);

        //build title component
        title = new BasicCLIComponent(73, ("\0________\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "|        \\\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "| $$$$$$$$\0\0______\0\0\0\\$$\0\0______\0\0\0_______\0\0_| $$_\0\0\0\0__\0\0\0\0__\0\0\0_______\0\n" + "| $$__\0\0\0\0\0/      \\\0|  \\\0|      \\\0|       \\|   $$ \\\0\0|  \\\0\0|  \\\0/       \\\n" + "| $$  \\\0\0\0|  $$$$$$\\| $$\0\0\\$$$$$$\\| $$$$$$$\\\\$$$$$$\0\0| $$\0\0| $$|  $$$$$$$\n" + "| $$$$$\0\0\0| $$\0\0\0\\$$| $$\0/      $$| $$\0\0| $$\0| $$\0__\0| $$\0\0| $$\0\\$$    \\\0\n" + "| $$_____\0| $$\0\0\0\0\0\0| $$|  $$$$$$$| $$\0\0| $$\0| $$|  \\| $$__/ $$\0_\\$$$$$$\\\n" + "| $$     \\| $$\0\0\0\0\0\0| $$\0\\$$\0\0\0\0$$| $$\0\0| $$\0\0\\$$  $$\0\\$$    $$|       $$\n" + "\0\\$$$$$$$$\0\\$$\0\0\0\0\0\0\0\\$$\0\0\\$$$$$$$\0\\$$\0\0\0\\$$\0\0\0\\$$$$\0\0_\\$$$$$$$\0\\$$$$$$$\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\__| $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$    $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$$$$$\0\0\0\0\0\0\0\0\0\0\0\n").split("\n"));
        title.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);

        //build sky background component
        String[] backgroundRows = new String[height];
        Arrays.fill(backgroundRows, " ".repeat(width));
        skyBackground = new BasicCLIComponent(width, backgroundRows);
        skyBackground.setColor(BLUE_BACKGROUND_BRIGHT);

        //build decorative cloud components
        String cloud1 = "\0\0\0\0._\0\0\0\0\0\n" + "\0.-(`  )\0\0\0\n" + ":(      ))\0\n" + "`(    )  ))\n" + "\0\0` __.:'\0\0\n";
        String cloud2 = "\0\0\0\0\0_\0\0\0\0\0\0\n" + "\0.:(`  )`.\0\0\n" + ":(   .    )\0\n" + "`.  (    ) )\n" + "\0\0` _`  ) )\0\n" + "\0\0\0\0\0(   )\0\0\n" + "\0\0\0\0\0\0`-'\0\0\0\n";
        String cloud3 = "\0\0\0\0.--\0\0\0\n" + "\0.+(   )\0\0\n" + "\0(   .  )\0\n" + "(   (   ))\n" + "\0`- __.'\0\0\n";
        String cloud4 = "\0\0\0\0_\0\0\0\0\0\0\0\n" + "\0\0(`  ).\0\0\0\0\n" + "\0(     ).\0\0\0\n" + "\0(       '`.\n" + "(      .   )\n" + "\0(..__.:'-'\0\n";
        decorativeClouds = new AnimatedCLIComponent[60];
        int maxSpeed = 2, minSpeed = 1;
        for (int n = 0; n < decorativeClouds.length; n++) {

            String[] rows = null;
            switch(n % 4) {

                case 0:
                    rows = cloud1.split("\n");
                    break;
                case 1:
                    rows = cloud2.split("\n");
                    break;
                case 2:
                    rows = cloud3.split("\n");
                    break;
                case 3:
                    rows = cloud4.split("\n");
                    break;
            }

            AnimatedCLIComponent decorativeCloud = new AnimatedCLIComponent(rows[0].length(), rows);
            decorativeCloud.setColor(WHITE + BLUE_BACKGROUND_BRIGHT);
            decorativeCloud.setSpeedX((float) ((Math.random() * (maxSpeed - minSpeed) + minSpeed) * (Math.random() >= 0.5 ? 1 : -1)));
            decorativeClouds[n] = decorativeCloud;
        }

        startPrompt = new BlinkingCLIComponent(29, new String[] {"Press enter to start the game"});
        startPrompt.setFirstColor(BLACK + BLUE_BACKGROUND_BRIGHT);
        startPrompt.setFirstColor(WHITE_BRIGHT + BLUE_BACKGROUND_BRIGHT);

        connectingLabel = new BasicCLIComponent(27, new String[] {"Connecting to the server..."});
        connectingLabel.setColor(BLACK + BLUE_BACKGROUND_BRIGHT);
        connectingLabel.setHidden(true);

        enterUsernamePrompt = new BasicCLIComponent(16, new String[] {"Enter a username"});
        enterUsernamePrompt.setColor(RESET);
        enterUsernamePrompt.setHidden(true);

        usernameTextArea = new TextAreaCLIComponent(20, 4);
        usernameTextArea.setHidden(true);
        usernameTextArea.setAnimated(false);

        selectNumberOfPlayersPrompt = new BasicCLIComponent(28, new String[] {"Select the number of players"});
        selectNumberOfPlayersPrompt.setColor(RESET);
        selectNumberOfPlayersPrompt.setHidden(true);

        playerNumbers = new BasicCLIComponent[3];
        for (int n = 0; n < playerNumbers.length; n++) {

            playerNumbers[n] = new BasicCLIComponent(3, new String[] { "|" + (n + 2) + "|"});
            playerNumbers[n].setHidden(true);
        }

        selectGameModePrompt = new BasicCLIComponent(20, new String[] {"Select the game mode"});
        selectGameModePrompt.setColor(RESET);
        selectGameModePrompt.setHidden(true);

        Mode[] modes = Mode.values();
        gameModes = new BasicCLIComponent[modes.length];
        for (int n = 0; n < gameModes.length; n++){

            gameModes[n] = new BasicCLIComponent(modes[n].toString().length(), new String[] { modes[n].toString() });
            gameModes[n].setHidden(true);
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
            decorativeCloud.setPosition(
                    (int) (Math.random() * getWidth() - decorativeCloud.getWidth()),
                    (int) (Math.random() * skyBackground.getHeight() - decorativeCloud.getHeight()));

        startPrompt.setPosition(getWidth() / 2f - startPrompt.getWidth() / 2f, title.getFrameY() + title.getHeight() + 2);
        connectingLabel.setPosition(getWidth() / 2f - connectingLabel.getWidth() / 2f, startPrompt.getY());
        enterUsernamePrompt.setPosition(getWidth() / 2f - enterUsernamePrompt.getWidth() / 2f, startPrompt.getY());
        selectNumberOfPlayersPrompt.setPosition(getWidth() / 2f - selectNumberOfPlayersPrompt.getWidth() / 2f, startPrompt.getY());
        selectGameModePrompt.setPosition(getWidth() / 2f - selectGameModePrompt.getWidth() / 2f, startPrompt.getY());

        usernameTextArea.setPosition(getWidth() / 2f - usernameTextArea.getWidth() / 2f, enterUsernamePrompt.getFrameY() + 2);

        for(int n = 0; n < playerNumbers.length; n++) playerNumbers[n].setPosition(
                getWidth() / 2f - (playerNumbers[0].getWidth() * playerNumbers.length + playerNumbers.length - 2) / 2f + (playerNumbers[0].getWidth() + 1) * n,
                selectNumberOfPlayersPrompt.getFrameY() + 4);

        for(int n = 0; n < gameModes.length; n++)
            gameModes[n].setPosition(getWidth() / 2f - 3, selectGameModePrompt.getFrameY() + 2 + n * 2);
    }

    /**
     * Updates the position of decorative clouds in order to make them appear on the other side of the terminal window when they are out of view
     */
    private void updateDecorativeCloudsPositions() {

        for (AnimatedCLIComponent decorativeCloud : decorativeClouds) {

            if (decorativeCloud.getX() < - decorativeCloud.getWidth()) decorativeCloud.setX(getWidth());
            else if (decorativeCloud.getX() > getWidth()) decorativeCloud.setX(- decorativeCloud.getWidth());
        }
    }

    @Override
    public void printToFrame(Frame frame) {

        updateDecorativeCloudsPositions();

        skyBackground.printToFrame(frame);
        for(AnimatedCLIComponent decorativeCloud : decorativeClouds) decorativeCloud.printToFrame(frame);
        title.printToFrame(frame);
        startPrompt.printToFrame(frame);
        connectingLabel.printToFrame(frame);
        enterUsernamePrompt.printToFrame(frame);
        selectNumberOfPlayersPrompt.printToFrame(frame);
        selectGameModePrompt.printToFrame(frame);
        usernameTextArea.printToFrame(frame);
        for(BasicCLIComponent playerNumber : playerNumbers) playerNumber.printToFrame(frame);
        for(BasicCLIComponent gameMode : gameModes) gameMode.printToFrame(frame);
        if(prompt != null) prompt.printToFrame(frame);
    }

    public void setPrompt(CLIComponent prompt) {

        this.prompt = prompt;
    }

    public CLIComponent getPrompt() {

        return prompt;
    }

    public BlinkingCLIComponent getStartPrompt() {

        return startPrompt;
    }

    public BasicCLIComponent getConnectingLabel() {

        return connectingLabel;
    }

    public BasicCLIComponent getEnterUsernamePrompt() {

        return enterUsernamePrompt;
    }

    public BasicCLIComponent getSelectNumberOfPlayersPrompt() {

        return selectNumberOfPlayersPrompt;
    }

    public BasicCLIComponent getSelectGameModePrompt() {

        return selectGameModePrompt;
    }

    public TextAreaCLIComponent getUsernameTextArea() {

        return usernameTextArea;
    }

    public BasicCLIComponent getPlayerNumber(int index) {

        return playerNumbers[index];
    }

    public int getNumberOfPlayerNumbers() {

        return playerNumbers.length;
    }

    public BasicCLIComponent getGameMode(int index) {

        return gameModes[index];
    }

    public int getNumberOfGameModes() {

        return gameModes.length;
    }
}
