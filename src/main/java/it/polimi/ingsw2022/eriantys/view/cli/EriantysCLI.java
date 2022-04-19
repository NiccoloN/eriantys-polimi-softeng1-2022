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
import org.jline.utils.AttributedString;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw2022.eriantys.view.cli.components.AnsiColorCodes.*;

/**
 * This class represents the command line interface (CLI) of the game
 */
public class EriantysCLI {

    private static final int TERMINAL_WIDTH = 181, TERMINAL_HEIGHT = 50;
    
    private static final String TEAM_WHITE_COLOR = WHITE_BRIGHT + UNDERLINED;
    private static final String TEAM_BLACK_COLOR = BLACK_BRIGHT + UNDERLINED;
    private static final String TEAM_GRAY_COLOR = WHITE + UNDERLINED;

    private final Terminal terminal;
    private final LineReader lineReader;
    private final AttributedString prompt;

    private final CLIComponent title;
    private final IslandCLIComponent[] islands;
    private final List<CloudCLIComponent> clouds;
    private final List<PlayerStatusCLIComponent> players;
    private final List<HelperCardCLIComponent> helpers;

    /**
     * Constructs the CLI
     * @throws IOException if the terminal instance could not be built
     */
    public EriantysCLI() throws IOException {

        terminal = TerminalBuilder
                .builder()
                .build();

        lineReader = LineReaderBuilder
                .builder()
                .terminal(terminal)
                .build();

        prompt = new AttributedString(GREEN + ">" + RESET);

        title = new CLIComponent(73, (" ________            __                       __                         \n" + "|        \\          |  \\                     |  \\                        \n" + "| $$$$$$$$  ______   \\$$  ______   _______  _| $$_    __    __   _______ \n" + "| $$__     /      \\ |  \\ |      \\ |       \\|   $$ \\  |  \\  |  \\ /       \\\n" + "| $$  \\   |  $$$$$$\\| $$  \\$$$$$$\\| $$$$$$$\\\\$$$$$$  | $$  | $$|  $$$$$$$\n" + "| $$$$$   | $$   \\$$| $$ /      $$| $$  | $$ | $$ __ | $$  | $$ \\$$    \\ \n" + "| $$_____ | $$      | $$|  $$$$$$$| $$  | $$ | $$|  \\| $$__/ $$ _\\$$$$$$\\\n" + "| $$     \\| $$      | $$ \\$$    $$| $$  | $$  \\$$  $$ \\$$    $$|       $$\n" + " \\$$$$$$$$ \\$$       \\$$  \\$$$$$$$ \\$$   \\$$   \\$$$$  _\\$$$$$$$ \\$$$$$$$ \n" + "                                                     |  \\__| $$          \n" + "                                                      \\$$    $$          \n" + "                                                       \\$$$$$$           \n")
                .split("\n"));
        title.setColor(YELLOW);

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

        islands[0].setTeamColor(TEAM_WHITE_COLOR);
        islands[0].setTower(true);
        islands[0].setMother(true);

        players.get(0).setRed(10);
        players.get(0).setBlue(2);
        players.get(0).setBlueEntrance(5);
        players.get(0).setRedProf(true);
        players.get(0).setGreen(4);

        setComponentsPositions();
    }

    private void setComponentsPositions() {

        title.setPosition(TERMINAL_WIDTH / 2 - title.getWidth() / 2, 0);

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
     * inputs are managed to evolve the game state
     */
    public void start() {

        while (true) {

            terminal.writer().print("\u001B[8;" + TERMINAL_HEIGHT + ";" + TERMINAL_WIDTH + "t");
            terminal.flush();

            if (terminal.getWidth() == TERMINAL_WIDTH && terminal.getHeight() == TERMINAL_HEIGHT) {

                clear();

                printComponent(title);
                lineDown(1);
                printLine(TERMINAL_WIDTH);
                for (IslandCLIComponent island : islands) printComponent(island);
                for (CloudCLIComponent cloud : clouds) printComponent(cloud);
                for (PlayerStatusCLIComponent player : players) printComponent(player);
                for (HelperCardCLIComponent helper : helpers) printComponent(helper);
                terminal.flush();

                String line = lineReader.readLine(prompt.toAnsi());
            }
        }
    }

    /**
     * Prints a component at its position on the terminal window
     * @param component the component to print
     */
    private void printComponent(CLIComponent component) {

        int rowNum = 0;
        for (String row : component.getRows()) {

            setCursorPos(component.getX(), component.getY() + rowNum);
            terminal.writer().print(row);
            rowNum++;
        }
    }

    /**
     * Prints a horizontal line at the current cursor position on the terminal window
     * @param length the length of the line to print
     */
    private void printLine(int length) {

        if (length < 0) {

            clear();
            throw new InvalidParameterException("Length must be >= 0");
        }
        terminal.writer().print("_".repeat(length));
    }

    /**
     * Clears and resets the terminal window
     */
    private void clear() {

        terminal.writer().print("\u001Bc" + RESET);
        for (int n = 0; n < terminal.getHeight(); n++) terminal.writer().println(" ".repeat(terminal.getWidth()));
        terminal.flush();
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

    /**
     * Moves the cursor up the given number of lines
     * @param lines the number of lines to move up
     * @throws InvalidParameterException if lines < 0
     */
    private void lineUp(int lines) {

        if (lines < 0) {

            clear();
            throw new InvalidParameterException("Lines must be >= 0");
        }
        for (int n = 0; n < lines; n++) terminal.puts(InfoCmp.Capability.cursor_up);
    }

    /**
     * Moves the cursor down the given number of lines and to the most-left
     * @param lines the number of lines to move down
     * @throws InvalidParameterException if lines < 0
     */
    private void lineDown(int lines) {

        if (lines < 0) {

            clear();
            throw new InvalidParameterException("Lines must be >= 0");
        }
        for (int n = 0; n < lines; n++) terminal.puts(InfoCmp.Capability.cursor_down);
    }

    /**
     * Moves the cursor right the given number of chars
     * @param chars the number of chars to move right
     * @throws InvalidParameterException if chars < 0
     */
    private void charRight(int chars) {

        if (chars < 0) {

            clear();
            throw new InvalidParameterException("Chars must be >= 0");
        }
        for (int n = 0; n < chars; n++) terminal.puts(InfoCmp.Capability.cursor_right);
    }

    /**
     * Moves the cursor left the given number of chars
     * @param chars the number of chars to move left
     * @throws InvalidParameterException if chars < 0
     */
    private void charLeft(int chars) {

        if (chars < 0) {

            clear();
            throw new InvalidParameterException("Chars must be >= 0");
        }
        for (int n = 0; n < chars; n++) terminal.puts(InfoCmp.Capability.cursor_left);
    }
}
