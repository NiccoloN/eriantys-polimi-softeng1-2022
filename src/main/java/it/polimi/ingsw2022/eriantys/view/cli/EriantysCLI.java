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

import static it.polimi.ingsw2022.eriantys.view.cli.AnsiCodes.*;

/**
 * This class represents the command line interface (CLI) of the game
 * @author Niccolò Nicolosi
 */
public class EriantysCLI {

    private static final float FRAME_TIME = 1 / 60f;
    private static final int FRAME_WIDTH = 181, FRAME_HEIGHT = 58;

    private static final String TERMINAL_RESET = "\u001Bc\u001B[3J\u001Bc\u001B[H";
    private static final String TERMINAL_RESIZE = "\u001B[8;" + FRAME_HEIGHT + ";" + FRAME_WIDTH + "t";
    private static final String TERMINAL_HOME = "\u001B[H" + RESET;

    private static final String TEAM_WHITE_COLOR = WHITE_BRIGHT;
    private static final String TEAM_BLACK_COLOR = BLACK_BRIGHT;
    private static final String TEAM_GRAY_COLOR = WHITE;

    private final Terminal terminal;
    private int prevWidth, prevHeight;

    private boolean running;

    private final char[] inputChars;
    private boolean inputProcessed;

    private final Frame frame;

    private BasicCLIComponent prompt;
    private final BasicCLIComponent title;
    private final BasicCLIComponent line;
    private final BasicCLIComponent skyBackground;
    private final AnimatedCLIComponent[] decorativeClouds;
    private final TextAreaCLIComponent hintTextArea;
    private final TextAreaCLIComponent effectTextArea;
    private final IslandCLIComponent[] islands;
    private final List<CloudCLIComponent> clouds;
    private final List<PlayerStatusCLIComponent> players;
    private final List<HelperCardCLIComponent> helpers;
    private final CharacterCardCLIComponent[] characters;

    private CLIState state;

    /**
     * Constructs the CLI and all of its components
     * @throws IOException if the terminal instance could not be built
     */
    public EriantysCLI() throws IOException {

        //build terminal instance
        terminal = TerminalBuilder
                .builder()
                .system(true)
                .jna(true)
                .build();
        terminal.writer().print(TERMINAL_RESET + TERMINAL_RESIZE);
        terminal.flush();

        running = false;

        //build input buffer
        inputChars = new char[3];
        inputProcessed = false;

        //initialize frame
        frame = new Frame(FRAME_WIDTH, FRAME_HEIGHT);

        //build title component
        title = new BasicCLIComponent(73, ("\0________\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0__\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "|        \\\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n" + "| $$$$$$$$\0\0______\0\0\0\\$$\0\0______\0\0\0_______\0\0_| $$_\0\0\0\0__\0\0\0\0__\0\0\0_______\0\n" + "| $$__\0\0\0\0\0/      \\\0|  \\\0|      \\\0|       \\|   $$ \\\0\0|  \\\0\0|  \\\0/       \\\n" + "| $$  \\\0\0\0|  $$$$$$\\| $$\0\0\\$$$$$$\\| $$$$$$$\\\\$$$$$$\0\0| $$\0\0| $$|  $$$$$$$\n" + "| $$$$$\0\0\0| $$\0\0\0\\$$| $$\0/      $$| $$\0\0| $$\0| $$\0__\0| $$\0\0| $$\0\\$$    \\\0\n" + "| $$_____\0| $$\0\0\0\0\0\0| $$|  $$$$$$$| $$\0\0| $$\0| $$|  \\| $$__/ $$\0_\\$$$$$$\\\n" + "| $$     \\| $$\0\0\0\0\0\0| $$\0\\$$\0\0\0\0$$| $$\0\0| $$\0\0\\$$  $$\0\\$$    $$|       $$\n" + "\0\\$$$$$$$$\0\\$$\0\0\0\0\0\0\0\\$$\0\0\\$$$$$$$\0\\$$\0\0\0\\$$\0\0\0\\$$$$\0\0_\\$$$$$$$\0\\$$$$$$$\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0|  \\__| $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$    $$\0\0\0\0\0\0\0\0\0\0\n" + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\\$$$$$$\0\0\0\0\0\0\0\0\0\0\0\n").split("\n"));
        title.setColor(YELLOW + BLUE_BACKGROUND_BRIGHT);

        //build sky background component
        String[] backgroundRows = new String[title.getHeight() + 1];
        Arrays.fill(backgroundRows, " ".repeat(FRAME_WIDTH));
        skyBackground = new BasicCLIComponent(FRAME_WIDTH, backgroundRows);
        skyBackground.setColor(BLUE_BACKGROUND_BRIGHT);

        //build sky separator line
        line = new BasicCLIComponent(FRAME_WIDTH, new String[] {"_".repeat(FRAME_WIDTH) });
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
        effectTextArea = new TextAreaCLIComponent(hintTextArea.getWidth(), hintTextArea.getHeight(), "Effect");

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
        characters[0] = new CharacterCardCLIComponent(1, "Choose a type of Student: every player (including yourself) must return 3 Students of that type " +
                                                         "from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as " +
                                                         "many Students as they have", 1);
        characters[1] = new CharacterCardCLIComponent(1, "Choose a type of Student: every player (including yourself) must return 3 Students of that type " +
                                                         "from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as " +
                                                         "many Students as they have", 2);
        characters[2] = new CharacterCardCLIComponent(1, "Choose a type of Student: every player (including yourself) must return 3 Students of that type " +
                                                         "from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as " +
                                                         "many Students as they have", 3);

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

        //set first state
        setState(new HelperSelection(this));
    }

    /**
     * Sets every component to its starting position
     */
    private void setComponentsPositions() {

        title.setPosition(FRAME_WIDTH / 2f - title.getWidth() / 2f, 0);
        line.setPosition(0, title.getHeight());

        for(AnimatedCLIComponent decorativeCloud : decorativeClouds)
            decorativeCloud.setPosition(
                    (int) (Math.random() * FRAME_WIDTH - decorativeCloud.getWidth()),
                    (int) (Math.random() * skyBackground.getHeight() - decorativeCloud.getHeight() + 1));

        int offsetY = title.getHeight() + 2;

        players.get(0).setPosition(0, offsetY);
        players.get(1).setPosition(FRAME_WIDTH - players.get(0).getWidth(), offsetY);
        players.get(2).setPosition(0, offsetY + players.get(0).getHeight() + 1);
        players.get(3).setPosition(FRAME_WIDTH - players.get(0).getWidth(), offsetY + players.get(0).getHeight() + 1);

        int textAreasY = players.get(players.size() - 1).getFrameY() + players.get(0).getHeight() + 1;
        hintTextArea.setPosition(FRAME_WIDTH - hintTextArea.getWidth(), textAreasY);
        effectTextArea.setPosition(0, textAreasY);

        islands[0].setPosition(2 + FRAME_WIDTH / 2f, offsetY);
        islands[1].setPosition(2 + FRAME_WIDTH / 2f + islands[0].getWidth(), offsetY + islands[0].getHeight() / 2f);
        islands[2].setPosition(2 + FRAME_WIDTH / 2f + islands[0].getWidth() * 2, offsetY + islands[0].getHeight());
        islands[3].setPosition(2 + FRAME_WIDTH / 2f + islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + 1);
        islands[4].setPosition(2 + FRAME_WIDTH / 2f + islands[0].getWidth(), offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2f + 1);
        islands[5].setPosition(2 + FRAME_WIDTH / 2f, offsetY + islands[0].getHeight() * 3 + 1);
        islands[6].setPosition(-1 + FRAME_WIDTH / 2f - islands[0].getWidth(), offsetY + islands[0].getHeight() * 3 + 1);
        islands[7].setPosition(-1 + FRAME_WIDTH / 2f - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() * 2 + islands[0].getHeight() / 2f + 1);
        islands[8].setPosition(-1 + FRAME_WIDTH / 2f - islands[0].getWidth() * 3, offsetY + islands[0].getHeight() * 2 + 1);
        islands[9].setPosition(-1 + FRAME_WIDTH / 2f - islands[0].getWidth() * 3, offsetY + islands[0].getHeight());
        islands[10].setPosition(-1 + FRAME_WIDTH / 2f - islands[0].getWidth() * 2, offsetY + islands[0].getHeight() / 2f);
        islands[11].setPosition(-1 + FRAME_WIDTH / 2f - islands[0].getWidth(), offsetY);

        //arrange clouds in a row in the middle of the islands
        int cloudsY = islands[2].getFrameY() + islands[2].getHeight() - clouds.get(0).getHeight() / 2;
        for(int n = 0; n < clouds.size(); n++) clouds.get(n).setPosition(
                FRAME_WIDTH / 2f - (clouds.get(0).getWidth() * clouds.size() + clouds.size() - 2) / 2f + (clouds.get(0).getWidth() + 1) * n, cloudsY);

        //arrange character cards in a row below the islands
        int charactersY = islands[5].getFrameY() + islands[5].getHeight() + 2;
        for(int n = 0; n < characters.length; n++) characters[n].setPosition(
                FRAME_WIDTH / 2f - (characters[0].getWidth() * characters.length + characters.length - 2) / 2f + (characters[0].getWidth() + 1) * n, charactersY);

        //arrange helper cards in a row at the bottom of the screen
        for(int n = 0; n < helpers.size(); n++) helpers.get(n).setPosition(
                FRAME_WIDTH / 2f - (helpers.get(0).getWidth() * helpers.size() + helpers.size() - 2) / 2f + (helpers.get(0).getWidth() + 1) * n,
                FRAME_HEIGHT - helpers.get(0).getHeight() - 1);
    }

    /**
     * Starts the CLI. Once the CLI is started, the current state of the game is visualized in the terminal window and
     * inputs are passed to the controller to evolve the game state
     * @throws TimeoutException if the terminal window could not be correctly resized
     */
    public void start() throws TimeoutException {

        running = true;

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
                    Arrays.fill(inputChars, '\0');
                    inputChars[0] = (char) terminal.reader().read();

                    //if the fist character read is an escape character, waits for an ansi escape sequence
                    if (inputChars[0] == ESCAPE_CHAR) {

                        int c;
                        for (int n = 1; n < inputChars.length; n++) {

                            c = terminal.reader().read(1);
                            if (c < 0) break;
                            inputChars[n] = (char) c;
                        }
                    }
                    Input input = new Input(inputChars[0], inputChars[1], inputChars[2]);

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
        long lastFrameTime, delta;
        while(running) {

            lastFrameTime = System.nanoTime();
            delta = 0;
            while(delta < FRAME_TIME * 1000000000) {

                try {

                    Thread.sleep(0, 100);
                }
                catch(InterruptedException e) {

                    printException(e);
                }
                delta = System.nanoTime() - lastFrameTime;
            }
            synchronized(this) {

                if (running) update();
                inputProcessed = false;
                notify();
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
        while (!acceptableWindowSize()) {

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
                                           + "This application needs a terminal window of at least " + FRAME_WIDTH + " columns and " + FRAME_HEIGHT + " rows to run\n"
                                           + "Try to manually resize your terminal window or to reduce the font size of your terminal");
        }

        //reset terminal if size changed or buffer is too big
        if (terminal.getWidth() != prevWidth || terminal.getHeight() != prevHeight) terminal.writer().print(TERMINAL_RESET);
        prevWidth = terminal.getWidth();
        prevHeight = terminal.getHeight();

        updateDecorativeCloudsPositions();

        frame.clear();

        //print the components to the frame in the right order
        skyBackground.printToFrame(frame);
        for(AnimatedCLIComponent decorativeCloud : decorativeClouds) decorativeCloud.printToFrame(frame);
        title.printToFrame(frame);
        line.printToFrame(frame);
        for(IslandCLIComponent island : islands) island.printToFrame(frame);
        for(CloudCLIComponent cloud : clouds) cloud.printToFrame(frame);
        for(PlayerStatusCLIComponent player : players) player.printToFrame(frame);
        for(HelperCardCLIComponent helper : helpers) helper.printToFrame(frame);
        for(CharacterCardCLIComponent character : characters) character.printToFrame(frame);
        hintTextArea.printToFrame(frame);
        effectTextArea.printToFrame(frame);
        if(prompt != null) prompt.printToFrame(frame);

        //clear the terminal window
        terminal.writer().print(TERMINAL_HOME);

        //print the frame string to terminal
        terminal.writer().print(frame.getAnsiString(Math.max(terminal.getWidth(), FRAME_WIDTH), Math.max(terminal.getHeight(), FRAME_HEIGHT)));
        terminal.flush();
    }

    /**
     * Updates the position of decorative clouds in order to make them appear on the other side of the terminal window when they are out of view
     */
    private void updateDecorativeCloudsPositions() {

        for (AnimatedCLIComponent decorativeCloud : decorativeClouds) {

            if (decorativeCloud.getX() < - decorativeCloud.getWidth()) decorativeCloud.setX(FRAME_WIDTH);
            else if (decorativeCloud.getX() > FRAME_WIDTH) decorativeCloud.setX(- decorativeCloud.getWidth());
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
     * Forces clear and reset of the terminal window. Slow method: it is strongly recommended not to use it in loops
     */
    private void clear() {

        terminal.writer().print(TERMINAL_RESET);
        terminal.flush();
    }

    /**
     * @return whether the terminal window is of an acceptable size
     */
    private boolean acceptableWindowSize() {

        return terminal.getWidth() >= FRAME_WIDTH && terminal.getHeight() >= FRAME_HEIGHT;
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

    public void setPrompt(BasicCLIComponent prompt) {

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

        return helpers.size();
    }

    public CharacterCardCLIComponent getCharacter(int index) {

        return characters[index];
    }

    public int getNumberOfCharacters() {

        return characters.length;
    }

    public TextAreaCLIComponent getHintTextArea() {

        return hintTextArea;
    }

    public TextAreaCLIComponent getEffectTextArea() {

        return effectTextArea;
    }
}
