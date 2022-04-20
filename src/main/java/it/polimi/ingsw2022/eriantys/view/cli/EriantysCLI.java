package it.polimi.ingsw2022.eriantys.view.cli;

import it.polimi.ingsw2022.eriantys.view.cli.components.CLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.CloudCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.HelperCardCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.IslandCLIComponent;
import it.polimi.ingsw2022.eriantys.view.cli.components.player.PlayerStatusCLIComponent;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

/**
 * This class represents the command line interface (CLI) of the game
 * @author Niccolò Nicolosi
 */
public class EriantysCLI {

    private static final int TERMINAL_WIDTH = 181, TERMINAL_HEIGHT = 50;
    private static final String TERMINAL_RESIZE = "\u001B[8;" + TERMINAL_HEIGHT + ";" + TERMINAL_WIDTH + "t";
    private static final String TERMINAL_RESET = "\u001Bc" + "\u001B[3J" + RESET;
    
    private static final String TEAM_WHITE_COLOR = WHITE_BRIGHT;
    private static final String TEAM_BLACK_COLOR = BLACK_BRIGHT;
    private static final String TEAM_GRAY_COLOR = WHITE;

    private CLIState state;
    private boolean changed, inputProcessed;

    private final Terminal terminal;
    private final LineReader lineReader;

    private CLIComponent prompt;
    private final CLIComponent title;
    private final CLIComponent line;
    private final IslandCLIComponent[] islands;
    private final List<CloudCLIComponent> clouds;
    private final List<PlayerStatusCLIComponent> players;
    private final List<HelperCardCLIComponent> helpers;

    private final String[][] frame;

    /**
     * Constructs the CLI
     * @throws IOException if the terminal instance could not be built
     */
    public EriantysCLI() throws IOException {

        terminal = TerminalBuilder
                .builder()
                .jansi(true)
                .build();

        lineReader = LineReaderBuilder
                .builder()
                .terminal(terminal)
                .build();

        title = new CLIComponent(73, (" ________            __                       __                         \n" + "|        \\          |  \\                     |  \\                        \n" + "| $$$$$$$$  ______   \\$$  ______   _______  _| $$_    __    __   _______ \n" + "| $$__     /      \\ |  \\ |      \\ |       \\|   $$ \\  |  \\  |  \\ /       \\\n" + "| $$  \\   |  $$$$$$\\| $$  \\$$$$$$\\| $$$$$$$\\\\$$$$$$  | $$  | $$|  $$$$$$$\n" + "| $$$$$   | $$   \\$$| $$ /      $$| $$  | $$ | $$ __ | $$  | $$ \\$$    \\ \n" + "| $$_____ | $$      | $$|  $$$$$$$| $$  | $$ | $$|  \\| $$__/ $$ _\\$$$$$$\\\n" + "| $$     \\| $$      | $$ \\$$    $$| $$  | $$  \\$$  $$ \\$$    $$|       $$\n" + " \\$$$$$$$$ \\$$       \\$$  \\$$$$$$$ \\$$   \\$$   \\$$$$  _\\$$$$$$$ \\$$$$$$$ \n" + "                                                     |  \\__| $$          \n" + "                                                      \\$$    $$          \n" + "                                                       \\$$$$$$           \n")
                .split("\n"));
        title.setColor(YELLOW);

        line = new CLIComponent(TERMINAL_WIDTH, new String[] { "_".repeat(TERMINAL_WIDTH) });

        islands = new IslandCLIComponent[12];
        for(int n = 0; n < islands.length; n++) islands[n] = new IslandCLIComponent(n + 1);

        clouds = new ArrayList<>(4);
        for(int n = 0; n < 4; n++) clouds.add(new CloudCLIComponent(n + 1));

        players = new ArrayList<>(4);
        players.add(new PlayerStatusCLIComponent(1, "player_1", TEAM_WHITE_COLOR));
        players.add(new PlayerStatusCLIComponent(2, "player_2", TEAM_WHITE_COLOR));
        players.add(new PlayerStatusCLIComponent(3, "player_3", TEAM_BLACK_COLOR));
        players.add(new PlayerStatusCLIComponent(4, "player_4", TEAM_GRAY_COLOR));

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

        //TODO remove block
        islands[0].setTeamColor(TEAM_WHITE_COLOR);
        islands[0].setTower(true);
        islands[0].setMother(true);

        players.get(0).setRed(10);
        players.get(0).setBlue(2);
        players.get(0).setBlueEntrance(5);
        players.get(0).setRedProf(true);
        players.get(0).setGreen(4);
        //-------------------------

        frame = new String[TERMINAL_HEIGHT][TERMINAL_WIDTH];

        setComponentsPositions();

        setState(new HelperSelection(this));
        inputProcessed = false;
    }

    private void setComponentsPositions() {

        title.setPosition(TERMINAL_WIDTH / 2 - title.getWidth() / 2, 0);
        line.setPosition(0, title.getHeight() + 1);

        int offsetY = title.getHeight() + 2;
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

        for(int n = 0; n < clouds.size(); n++) clouds.get(n).setPosition(
                TERMINAL_WIDTH / 2 - (clouds.get(0).getWidth() * clouds.size() + clouds.size() - 2) / 2 + (clouds.get(0).getWidth() + 1) * n,
                islands[2].getY() + islands[2].getHeight() - clouds.get(0).getHeight() / 2);

        players.get(0).setPosition(0, 0);
        players.get(1).setPosition(TERMINAL_WIDTH - players.get(0).getWidth(), 0);
        players.get(2).setPosition(0, players.get(0).getHeight());
        players.get(3).setPosition(TERMINAL_WIDTH - players.get(0).getWidth(), players.get(0).getHeight());

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

        Thread inputThread = new Thread(() -> {

            terminal.enterRawMode();

            while(true) {

                try {

                    synchronized(this) {

                        if (inputProcessed) wait();

                        terminal.reader().skip(terminal.reader().available());
                        char input = (char) terminal.reader().read();
                        state.manageInput(input);
                        inputProcessed = true;
                        changed = true;

                        notify();
                    }
                }
                catch(IOException | InterruptedException e) {

                    clear();
                    e.printStackTrace();
                }
            }
        });
        inputThread.start();

        //update loop
        while(true) {

            try {

                synchronized(this) {

                    while(!changed) wait();

                    update();
                    changed = false;
                    inputProcessed = false;

                    notify();
                }
            }
            catch(InterruptedException e) {

                clear();
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the current state of the cli and notifies the thread in which the cli is running of the changes
     * @param state the new state of the cli
     */
    public void setState(CLIState state) {

        this.state = state;
        state.apply();

        synchronized(this) {

            changed = true;
            notify();
        }
    }

    /**
     * Updates the terminal window with the current state of the game
     * @throws TimeoutException if the terminal window could not be correctly resized
     */
    private void update() throws TimeoutException {

        clearFrame();

        title.printToFrame(frame);
        line.printToFrame(frame);
        for (int n = 0; n < islands.length; n++) islands[n].printToFrame(frame);
        for (int n = 0; n < clouds.size(); n++) clouds.get(n).printToFrame(frame);
        for (int n = 0; n < players.size(); n++) players.get(n).printToFrame(frame);
        for (int n = 0; n < helpers.size(); n++) helpers.get(n).printToFrame(frame);
        prompt.printToFrame(frame);

        StringBuilder frameBuilder = new StringBuilder();

        for(int i = 0; i < frame.length; i++) {

            for(int j = 0; j < frame[i].length; j++) frameBuilder.append(frame[i][j]);
            if(i != frame.length - 1) frameBuilder.append("\n");
        }

        if (!rightWindowSize()) {

            terminal.writer().print(TERMINAL_RESIZE);
            terminal.flush();

            long start;
            long time = 0;
            long timeout = 1000000000;

            while (!rightWindowSize()) {

                start = System.nanoTime();

                try {

                    Thread.sleep(1);
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }

                time += System.nanoTime() - start;
                if (time >= timeout)
                    throw new TimeoutException("Could not correctly resize terminal window (elapsed time: " + time / 1000000 + "ms)");
            }
        }

        frameBuilder.insert(0, TERMINAL_RESET);
        terminal.writer().print(frameBuilder);
        terminal.flush();
    }

    private void clearFrame() {

        for (int n = 0; n < frame.length; n++) Arrays.fill(frame[n], " ");
    }

    /**
     * Forces clear and reset of the terminal window. Slow method: it is strongly recommended not to use it in loops
     */
    private void clear() {

        terminal.writer().print(TERMINAL_RESET);
        terminal.flush();
    }

    private boolean rightWindowSize() {

        return terminal.getWidth() == TERMINAL_WIDTH && terminal.getHeight() == TERMINAL_HEIGHT;
    }

    public void setPrompt(CLIComponent prompt) {

        this.prompt = prompt;
    }

    /**
     * Sets the current cursor position at (x, y). Terminal's y-axis points downwards
     * @param x the position on the x-axis
     * @param y the position on the y-axis
     * @throws InvalidParameterException if the given position is not in the terminal window
     */
    private void setCursorPos(int x, int y) {

        if (x < 0 || x >= TERMINAL_WIDTH) {

            clear();
            throw new InvalidParameterException("Cursor x must be >= 0 and < " + TERMINAL_WIDTH);
        }
        if (y < 0 || y >= TERMINAL_HEIGHT) {

            clear();
            throw new InvalidParameterException("Cursor y must be >= 0 " + TERMINAL_HEIGHT);
        }
        terminal.writer().print("\u001B[" + (y + 1) + ";" + (x + 1) + "f");
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
