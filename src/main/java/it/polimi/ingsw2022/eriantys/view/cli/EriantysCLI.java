package it.polimi.ingsw2022.eriantys.view.cli;

import it.polimi.ingsw2022.eriantys.view.cli.components.*;
import it.polimi.ingsw2022.eriantys.view.cli.components.player.PlayerStatusCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.states.CLIState;
import it.polimi.ingsw2022.eriantys.view.cli.states.HelperSelection;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiCodes.*;

/**
 * This class represents the command line interface (CLI) of the game
 * @author Niccolò Nicolosi
 */
public class EriantysCLI {

    private static final float FRAME_TIME = 1 / 60f;
    private static final float INPUT_TIME = FRAME_TIME / 2;

    private static final int TERMINAL_WIDTH = 181, TERMINAL_HEIGHT = 58;
    private static final String TERMINAL_RESIZE = "\u001Bc\u001B[3J\u001Bc\u001B[8;" + TERMINAL_HEIGHT + ";" + TERMINAL_WIDTH + "t";
    private static final String TERMINAL_CLEAR = "\u001B[H" + RESET;

    private static final String TEAM_WHITE_COLOR = WHITE_BRIGHT;
    private static final String TEAM_BLACK_COLOR = BLACK_BRIGHT;
    private static final String TEAM_GRAY_COLOR = WHITE;

    private boolean running;
    private boolean goNextFrame;

    private final Terminal terminal;
    private final char[] input;
    private boolean inputProcessed;

    private CLIState state;

    private CLIComponent prompt;
    private final CLIComponent title;
    private final CLIComponent line;
    private final CLIComponent skyBackground;
    private final AnimatedCLIComponent[] decorativeClouds;
    private final TextAreaCLIComponent hintTextArea;
    private final TextAreaCLIComponent effectTextArea;
    private final IslandCLIComponent[] islands;
    private final List<CloudCLIComponent> clouds;
    private final List<PlayerStatusCLIComponent> players;
    private final List<HelperCardCLIComponent> helpers;
    private final CharacterCardCLIComponent[] characters;

    private final String[][] frame;

    /**
     * Constructs the CLI and all of its components
     * @throws IOException if the terminal instance could not be built
     */
    public EriantysCLI() throws IOException {

        running = false;

        //build terminal instance
        terminal = TerminalBuilder
                .builder()
                .system(true)
                .jna(true)
                .build();
        terminal.writer().print(TERMINAL_RESIZE);
        terminal.flush();

        //build input buffer
        input = new char[3];

        //build title component
        title = new CLIComponent(73, ("\0________\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "|        \\\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "| $$$$$$$$\0\0______\0\0\0\\$$\0\0______\0\0\0_______\0\0_| $$_\0\0\0\0__\0\0\0\0__\0\0\0_______\0\n" + "| $$__\0\0\0\0\0/      \\\0|  \\\0|      \\\0|       \\|   $$ \\\0\0|  \\\0\0|  \\\0/       \\\n" + "| $$  \\\0\0\0|  $$$$$$\\| $$\0\0\\$$$$$$\\| $$$$$$$\\\\$$$$$$\0\0| $$\0\0| $$|  $$$$$$$\n" + "| $$$$$\0\0\0| $$\0\0\0\\$$| $$\0/      $$| $$\0\0| $$\0| $$\0__\0| $$\0\0| $$\0\\$$    \\\0\n" + "| $$_____\0| $$\0\0\0\0\0\0| $$|  $$$$$$$| $$\0\0| $$\0| $$|  \\| $$__/ $$\0_\\$$$$$$\\\n" + "| $$     \\| $$\0\0\0\0\0\0| $$\0\\$$\0\0\0\0$$| $$\0\0| $$\0\0\\$$  $$\0\\$$    $$|       $$\n" + "\0\\$$$$$$$$\0\\$$\0\0\0\0\0\0\0\\$$\0\0\\$$$$$$$\0\\$$\0\0\0\\$$\0\0\0\\$$$$\0\0_\\$$$$$$$\0\\$$$$$$$\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\__| $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$    $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$$$$$\0\0\0\0\0\0\0\0\0\0\0\n").split("\n"));
        title.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);

        //build sky background component
        String[] backgroundRows = new String[title.getHeight() + 1];
        Arrays.fill(backgroundRows, " ".repeat(TERMINAL_WIDTH));
        skyBackground = new CLIComponent(TERMINAL_WIDTH, backgroundRows);
        skyBackground.setColor(BLUE_BACKGROUND_BRIGHT);

        //build sky separator line
        line = new CLIComponent(TERMINAL_WIDTH, new String[] { "_".repeat(TERMINAL_WIDTH) });
        line.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);

        //build decorative cloud components
        String cloud1 = "\0\0\0\0._\0\0\0\0\0\n" + "\0.-(`  )\0\0\0\n" + ":(      ))\0\n" + "`(    )  ))\n" + "\0\0` __.:'\0\0\n";
        String cloud2 = "\0\0\0\0\0_\0\0\0\0\0\0\n" + "\0.:(`  )`.\0\0\n" + ":(   .    )\0\n" + "`.  (    ) )\n" + "\0\0` _`  ) )\0\n" + "\0\0\0\0\0(   )\0\0\n" + "\0\0\0\0\0\0`-'\0\0\0\n";
        String cloud3 = "\0\0\0\0.--\0\0\0\n" + "\0.+(   )\0\0\n" + "\0(   .  )\0\n" + "(   (   ))\n" + "\0`- __.'\0\0\n";
        String cloud4 = "\0\0\0\0_\0\0\0\0\0\0\0\n" + "\0\0(`  ).\0\0\0\0\n" + "\0(     ).\0\0\0\n" + "\0(       '`.\n" + "(      .   )\n" + "\0(..__.:'-'\0\n";
        decorativeClouds = new AnimatedCLIComponent[20];
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
            decorativeCloud.setSpeedX((float) (Math.random() * (maxSpeed - minSpeed) * (Math.random() >= 0.5 ? 1 : -1) + minSpeed));
            decorativeClouds[n] = decorativeCloud;
        }

        //build island components
        islands = new IslandCLIComponent[12];
        for(int n = 0; n < islands.length; n++) islands[n] = new IslandCLIComponent(n + 1);

        //build cloud components
        clouds = new ArrayList<>(4);
        for(int n = 0; n < 4; n++) clouds.add(new CloudCLIComponent(n + 1));

        //build player dashboard components
        players = new ArrayList<>(4);
        players.add(new PlayerStatusCLIComponent(1, "player_1", TEAM_WHITE_COLOR));
        players.add(new PlayerStatusCLIComponent(2, "player_2", TEAM_WHITE_COLOR));
        players.add(new PlayerStatusCLIComponent(3, "player_3", TEAM_BLACK_COLOR));
        players.add(new PlayerStatusCLIComponent(4, "player_4", TEAM_GRAY_COLOR));

        //build text area components
        hintTextArea = new TextAreaCLIComponent(players.get(0).getWidth(), 15, "Hints");
        hintTextArea.setText("Ciaooooooooo oooooooo ooooooooo ooooooooooo");
        effectTextArea = new TextAreaCLIComponent(hintTextArea.getWidth(), hintTextArea.getHeight(), "Effect");
        effectTextArea.setText("Choose a type of Student: every player (including yourself) must return 3 Students of that type " +
                               "from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as " +
                               "many Students as they have\n\n" + YELLOW + "Cost" + BLACK + ": 03");

        //build helper card components
        helpers = new ArrayList<>(10);
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));
        helpers.add(new HelperCardCLIComponent(1, 3, 7));

        //build character card components
        characters = new CharacterCardCLIComponent[3];
        characters[0] = new CharacterCardCLIComponent(1);
        characters[1] = new CharacterCardCLIComponent(2);
        characters[2] = new CharacterCardCLIComponent(3);

        //TODO remove template block
        islands[0].setTeamColor(TEAM_WHITE_COLOR);
        islands[0].setTower(true);
        islands[0].setMother(true);

        players.get(0).setRed(10);
        players.get(0).setBlue(2);
        players.get(0).setBlueEntrance(5);
        players.get(0).setRedProf(true);
        players.get(0).setGreen(4);
        //-------------------------

        setComponentsPositions();

        //initialize frame
        frame = new String[TERMINAL_HEIGHT][TERMINAL_WIDTH];
        goNextFrame    = false;
        inputProcessed = false;

        //set first state
        setState(new HelperSelection(this));
    }

    /**
     * Sets every component to its starting position
     */
    private void setComponentsPositions() {

        title.setPosition(TERMINAL_WIDTH / 2 - title.getWidth() / 2, 0);
        line.setPosition(0, title.getHeight());

        for(AnimatedCLIComponent decorativeCloud : decorativeClouds)
            decorativeCloud.setPosition(
                    (int) (Math.random() * TERMINAL_WIDTH - decorativeCloud.getWidth()),
                    (int) (Math.random() * skyBackground.getHeight() - decorativeCloud.getHeight() + 1));

        int offsetY = title.getHeight() + 2;

        players.get(0).setPosition(0, offsetY);
        players.get(1).setPosition(TERMINAL_WIDTH - players.get(0).getWidth(), offsetY);
        players.get(2).setPosition(0, offsetY + players.get(0).getHeight() + 1);
        players.get(3).setPosition(TERMINAL_WIDTH - players.get(0).getWidth(), offsetY + players.get(0).getHeight() + 1);

        int textAreasY = players.get(players.size() - 1).getFrameY() + players.get(0).getHeight() + 1;
        hintTextArea.setPosition(TERMINAL_WIDTH - hintTextArea.getWidth(), textAreasY);
        effectTextArea.setPosition(0, textAreasY);

        islands[0].setPosition(2 + TERMINAL_WIDTH / 2, offsetY);
        islands[1].setPosition(2 + TERMINAL_WIDTH / 2 + islands[0].getWidth(), offsetY + islands[0].getHeight() / 2);
        islands[2].setPosition(2 + TERMINAL_WIDTH / 2 + islands[0].getWidth() * 2, offsetY + islands[0].getHeight());
        islands[3].setPosition(2 + TERMINAL_WIDTH / 2 + islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + 1);
        islands[4].setPosition(2 + TERMINAL_WIDTH / 2 + islands[0].getWidth(), offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2 + 1);
        islands[5].setPosition(2 + TERMINAL_WIDTH / 2, offsetY + islands[0].getHeight() * 3 + 1);
        islands[6].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth(), offsetY + islands[0].getHeight() * 3 + 1);
        islands[7].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2 + 1);
        islands[8].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 3, offsetY + islands[0].getHeight() * 2 + 1);
        islands[9].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 3, offsetY + islands[0].getHeight());
        islands[10].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() / 2);
        islands[11].setPosition(-1 + TERMINAL_WIDTH / 2 - islands[0].getWidth(), offsetY);

        //arrange clouds in a row in the middle of the islands
        int cloudsY = islands[2].getFrameY() + islands[2].getHeight() - clouds.get(0).getHeight() / 2;
        for(int n = 0; n < clouds.size(); n++) clouds.get(n).setPosition(
                TERMINAL_WIDTH / 2 - (clouds.get(0).getWidth() * clouds.size() + clouds.size() - 2) / 2 + (clouds.get(0).getWidth() + 1) * n, cloudsY);

        //arrange character cards in a row below the islands
        int charactersY = islands[5].getFrameY() + islands[5].getHeight() + 2;
        for(int n = 0; n < characters.length; n++) characters[n].setPosition(
                TERMINAL_WIDTH / 2 - (characters[0].getWidth() * characters.length + characters.length - 2) / 2 + (characters[0].getWidth() + 1) * n, charactersY);

        //arrange helper cards in a row at the bottom of the screen
        for(int n = 0; n < helpers.size(); n++) helpers.get(n).setPosition(
                TERMINAL_WIDTH / 2 - (helpers.get(0).getWidth() * helpers.size() + helpers.size() - 2) / 2 + (helpers.get(0).getWidth() + 1) * n,
                TERMINAL_HEIGHT - helpers.get(0).getHeight() - 1);
    }

    /**
     * Starts the CLI. Once the CLI is started, the current state of the game is visualized in the terminal window and
     * inputs are passed to the controller to evolve the game state
     * @throws TimeoutException if the terminal window could not be correctly resized
     */
    public void start() throws TimeoutException {

        running = true;

        //the time thread will take care of notifying the main cli thread whenever it is time to render a new frame
        Thread timeThread = new Thread(() -> {

            long start = System.nanoTime();
            long time = 0;

            while(true) {

                synchronized(this) {

                    time += System.nanoTime() - start;
                    if (time >= FRAME_TIME * 1000000000) {

                        goNextFrame = true;
                        time        = 0;
                        notify();
                    }
                    start = System.nanoTime();

                    while (goNextFrame) {

                        try {

                            wait();
                        }
                        catch(InterruptedException e) {

                            printException(e);
                        }
                    }
                }
            }
        });
        timeThread.start();

        //the input thread will take care of accepting new inputs at the right time and passing them to the controller
        Thread inputThread = new Thread(() -> {

            synchronized(this) { terminal.enterRawMode(); }

            while(true) {

                try {

                    synchronized(this) {

                        if (inputProcessed) wait();
                        notify();
                    }

                    //waits for new input
                    Arrays.fill(input, '\0');
                    input[0] = (char) terminal.reader().read();

                    //if the fist character read is an escape character, waits for an ansi escape sequence
                    if (input[0] == ESCAPE_CHAR) {

                        int c;
                        for (int n = 1; n < input.length; n++) {

                            c = terminal.reader().read(1);
                            if (c < 0) break;
                            input[n] = (char) c;
                        }
                    }

                    synchronized(this) {

                        //manages the input based on the current state
                        state.manageInput(input);
                        inputProcessed = true;
                        notify();
                    }
                }
                catch(IOException | InterruptedException e) {

                    printException(e);
                }
            }
        });
        inputThread.start();

        //update loop
        while(running) {

            try {

                synchronized(this) {

                    while(!goNextFrame) wait();

                    if (running) update();
                    goNextFrame    = false;
                    inputProcessed = false;
                    notify();
                }
            }
            catch(InterruptedException e) {

                printException(e);
            }
        }
    }

    /**
     * Updates the terminal window with the current state of the game
     * @throws TimeoutException if the terminal window could not be correctly resized
     */
    private void update() throws TimeoutException {

        //resize terminal window to the correct size
        long start = System.nanoTime();
        long time;
        long timeout = 5000000000L;
        while (!rightWindowSize()) {

            terminal.writer().print(TERMINAL_RESIZE);

            try {

                Thread.sleep(1);
            }
            catch (InterruptedException e) {

                e.printStackTrace();
            }

            time = System.nanoTime() - start;
            if (time >= timeout)
                throw new TimeoutException("Could not correctly resize terminal window (elapsed time: " + time / 1000000 + "ms)\n"
                        + "This application needs a terminal window of " + TERMINAL_WIDTH + " columns and " + TERMINAL_HEIGHT + " rows to run\n"
                        + "Try to manually resize your terminal window or to reduce the font size of your terminal");
        }

        clearFrame();

        updateDecorativeCloudsPositions();

        //print the components on the frame in the right order
        skyBackground.printToFrame(frame);
        for (int n = 0; n < decorativeClouds.length; n++) decorativeClouds[n].printToFrame(frame);
        title.printToFrame(frame);
        line.printToFrame(frame);
        for (int n = 0; n < islands.length; n++) islands[n].printToFrame(frame);
        for (int n = 0; n < clouds.size(); n++) clouds.get(n).printToFrame(frame);
        for (int n = 0; n < players.size(); n++) players.get(n).printToFrame(frame);
        for (int n = 0; n < helpers.size(); n++) helpers.get(n).printToFrame(frame);
        for (int n = 0; n < characters.length; n++) characters[n].printToFrame(frame);
        hintTextArea.printToFrame(frame);
        effectTextArea.printToFrame(frame);
        prompt.printToFrame(frame);

        //build new frame string
        StringBuilder frameBuilder = new StringBuilder();
        for(int i = 0; i < frame.length; i++) {

            for(int j = 0; j < frame[i].length; j++) frameBuilder.append(frame[i][j]);
            if(i != frame.length - 1) frameBuilder.append("\n");
        }

        //insert a clear screen ansi code at the start of the frame string
        frameBuilder.insert(0, TERMINAL_CLEAR);

        //print the frame string to terminal
        terminal.writer().print(frameBuilder);
        terminal.flush();
    }

    /**
     * Updates the position of decorative clouds in order to make them appear on the other side of the terminal window when they are out of view
     */
    private void updateDecorativeCloudsPositions() {

        for (AnimatedCLIComponent decorativeCloud : decorativeClouds) {

            if (decorativeCloud.getX() < - decorativeCloud.getWidth()) decorativeCloud.setX(TERMINAL_WIDTH);
            else if (decorativeCloud.getX() > TERMINAL_WIDTH) decorativeCloud.setX(- decorativeCloud.getWidth());
        }
    }

    /**
     * Sets the current state of the cli, exiting the previous and entering the given one
     * @param state the new state of the cli
     */
    public void setState(CLIState state) {

        if(this.state != null) this.state.exit();
        this.state = state;
        state.enter();
    }

    /**
     * Clears the frame, filling every cell with a space
     */
    private void clearFrame() {

        for (int n = 0; n < frame.length; n++) Arrays.fill(frame[n], " ");
    }

    /**
     * Forces clear and reset of the terminal window. Slow method: it is strongly recommended not to use it in loops
     */
    private void clear() {

        terminal.writer().print(TERMINAL_CLEAR);
        terminal.flush();
    }

    /**
     * @return whether the terminal window is of the correct size
     */
    private boolean rightWindowSize() {

        return terminal.getWidth() == TERMINAL_WIDTH && terminal.getHeight() == TERMINAL_HEIGHT;
    }

    /**
     * Stops cli updating, clears the terminal window and prints the given exception
     * @param e the exception to print
     */
    private void printException(Exception e) {

        running = false;
        clear();
        e.printStackTrace();
    }

    public void setPrompt(CLIComponent prompt) {

        this.prompt = prompt;
    }

    public IslandCLIComponent getIsland(int index) {

        return Arrays.stream(islands).filter((x) -> x.getIndex() == index).findAny().orElseThrow();
    }

    public CloudCLIComponent getCloud(int index) {

        return clouds.stream().filter((x) -> x.getIndex() == index).findAny().orElseThrow();
    }

    public HelperCardCLIComponent getHelper(int index) {

        return helpers.get(index);
    }

    public int getNumberOfHelpers() {

        return helpers.size();
    }
}
